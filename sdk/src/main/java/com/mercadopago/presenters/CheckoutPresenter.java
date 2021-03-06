package com.mercadopago.presenters;

import com.mercadopago.callbacks.FailureRecovery;
import com.mercadopago.constants.PaymentMethods;
import com.mercadopago.controllers.Timer;
import com.mercadopago.core.CheckoutStore;
import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.core.MercadoPagoComponents;
import com.mercadopago.exceptions.CheckoutPreferenceException;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.hooks.Hook;
import com.mercadopago.hooks.HookHelper;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.Campaign;
import com.mercadopago.model.Card;
import com.mercadopago.model.Cause;
import com.mercadopago.model.Customer;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.Payer;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentData;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearch;
import com.mercadopago.model.PaymentRecovery;
import com.mercadopago.model.PaymentResult;
import com.mercadopago.model.Token;
import com.mercadopago.mvp.MvpPresenter;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;
import com.mercadopago.plugins.PaymentResultAction;
import com.mercadopago.plugins.model.PaymentMethodInfo;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.preferences.FlowPreference;
import com.mercadopago.preferences.PaymentResultScreenPreference;
import com.mercadopago.preferences.ReviewScreenPreference;
import com.mercadopago.preferences.ServicePreference;
import com.mercadopago.providers.CheckoutProvider;
import com.mercadopago.util.ApiUtil;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.TextUtil;
import com.mercadopago.util.TextUtils;
import com.mercadopago.views.CheckoutView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CheckoutPresenter extends MvpPresenter<CheckoutView, CheckoutProvider> {

    private static final String INTERNAL_SERVER_ERROR_FIRST_DIGIT = "5";

    private CheckoutPreference mCheckoutPreference;
    private PaymentResultScreenPreference mPaymentResultScreenPreference;
    private ReviewScreenPreference mReviewScreenPreference;
    private FlowPreference mFlowPreference;
    private ServicePreference mServicePreference;
    private Boolean mBinaryMode;
    private Discount mDiscount;
    private Boolean mDirectDiscountEnabled;
    private PaymentData mPaymentDataInput;
    private PaymentResult mPaymentResultInput;
    private Integer mRequestedResult;

    private PaymentMethodSearch mPaymentMethodSearch;
    private Issuer mSelectedIssuer;
    private PayerCost mSelectedPayerCost;
    private Token mCreatedToken;
    private Card mSelectedCard;
    private PaymentMethod mSelectedPaymentMethod;
    private Payment mCreatedPayment;
    private Payer mCollectedPayer;

    private Boolean mPaymentMethodEdited = false;
    private boolean mPaymentMethodEditionRequested = false;
    private PaymentRecovery mPaymentRecovery;

    private String mCustomerId;
    private String mIdempotencyKeySeed;
    private String mCurrentPaymentIdempotencyKey;

    private transient FailureRecovery failureRecovery;
    private transient Timer mCheckoutTimer;

    public CheckoutPresenter() {
        if (mFlowPreference == null) {
            mFlowPreference = new FlowPreference.Builder().build();
        }
        if (mServicePreference == null) {
            mServicePreference = new ServicePreference.Builder().build();
        }
    }

    public void initialize() {
        getView().showProgress();
        try {
            validateParameters();
            if (mCheckoutPreference.getId() != null) {
                retrieveCheckoutPreference();
            } else {
                startCheckoutForPreference();
            }
        } catch (IllegalStateException e) {
            String userMessage = getResourcesProvider().getCheckoutExceptionMessage(e);
            String exceptionDetail = e.getMessage();
            getView().showError(new MercadoPagoError(userMessage, exceptionDetail, false));
        }
    }

    private void startCheckoutForPreference() {
        try {
            validatePreference();
            getView().initializeMPTracker();
            if(isNewFlow()) {
                getView().trackScreen();
            }
            startCheckout();
        } catch (CheckoutPreferenceException e) {
            String message = getResourcesProvider().getCheckoutExceptionMessage(e);
            getView().showError(new MercadoPagoError(message, false));
        }
    }

    private boolean isNewFlow() {
        return mPaymentDataInput == null && mPaymentResultInput == null;
    }

    private void validateParameters() throws IllegalStateException {
        if (mCheckoutPreference == null) {
            throw new IllegalStateException("preference not set");
        }
    }

    private void validatePreference() throws CheckoutPreferenceException {
        mCheckoutPreference.validate();
    }

    private void startCheckout() {
        resolvePreSelectedData();
        setCheckoutTimer();
        boolean shouldGetDiscounts = mDiscount == null && isDiscountEnabled();
        if (shouldGetDiscounts) {
            getDiscountCampaigns();
        } else {
            retrievePaymentMethodSearch();
        }
    }

    private void setCheckoutTimer() {
        if (mFlowPreference.isCheckoutTimerEnabled()) {
            mCheckoutTimer.start(mFlowPreference.getCheckoutTimerInitialTime());
            mCheckoutTimer.setOnFinishListener(new Timer.FinishListener() {
                @Override
                public void onFinish() {
                    mCheckoutTimer.finishCheckout();
                }
            });
        }
    }

    private void resolvePreSelectedData() {
        if (mPaymentDataInput != null) {
            mSelectedIssuer = mPaymentDataInput.getIssuer();
            mSelectedPayerCost = mPaymentDataInput.getPayerCost();
            mCreatedToken = mPaymentDataInput.getToken();
            mSelectedPaymentMethod = mPaymentDataInput.getPaymentMethod();
            if (mDiscount == null) {
                mDiscount = mPaymentDataInput.getDiscount();
            }
        } else if (mPaymentResultInput != null && mPaymentResultInput.getPaymentData() != null) {
            mSelectedPaymentMethod = mPaymentResultInput.getPaymentData().getPaymentMethod();
            mSelectedPayerCost = mPaymentResultInput.getPaymentData().getPayerCost();
            mSelectedIssuer = mPaymentResultInput.getPaymentData().getIssuer();
            mCreatedToken = mPaymentResultInput.getPaymentData().getToken();
            if (mDiscount == null) {
                mDiscount = mPaymentResultInput.getPaymentData().getDiscount();
            }
        }
    }

    private void getDiscountCampaigns() {
        getResourcesProvider().getDiscountCampaigns(onCampaignsRetrieved());
    }

    private OnResourcesRetrievedCallback<List<Campaign>> onCampaignsRetrieved() {
        return new OnResourcesRetrievedCallback<List<Campaign>>() {
            @Override
            public void onSuccess(List<Campaign> campaigns) {
                if (isViewAttached()) {
                    analyzeCampaigns(campaigns);
                }
            }

            @Override
            public void onFailure(final MercadoPagoError error) {
                if (isViewAttached()) {
                    mFlowPreference.disableDiscount();
                    retrievePaymentMethodSearch();
                }
            }
        };
    }

    private void analyzeCampaigns(final List<Campaign> campaigns) {
        boolean directDiscountFound = false;
        boolean couponDiscountFound = false;

        if (campaigns == null) {
            mFlowPreference.disableDiscount();
            retrievePaymentMethodSearch();
        } else {
            for (Campaign campaign : campaigns) {
                if (campaign.isDirectDiscountCampaign()) {
                    directDiscountFound = true;
                } else if (campaign.isCodeDiscountCampaign()) {
                    couponDiscountFound = true;
                }
            }

            if (directDiscountFound) {
                getDirectDiscount(couponDiscountFound);
            } else {
                if (couponDiscountFound) {
                    mDirectDiscountEnabled = false;
                } else {
                    mFlowPreference.disableDiscount();
                }
                retrievePaymentMethodSearch();
            }
        }
    }

    private void getDirectDiscount(final boolean couponDiscountFount) {
        String payerEmail = mCheckoutPreference.getPayer() == null ? "" : mCheckoutPreference.getPayer().getEmail();
        getResourcesProvider().getDirectDiscount(mCheckoutPreference.getAmount(), payerEmail, new OnResourcesRetrievedCallback<Discount>() {
            @Override
            public void onSuccess(final Discount discount) {
                if (isViewAttached()) {
                    mDiscount = discount;
                    retrievePaymentMethodSearch();
                }
            }

            @Override
            public void onFailure(final MercadoPagoError error) {
                if (isViewAttached()) {
                    mDirectDiscountEnabled = false;
                    if (couponDiscountFount) {
                        retrievePaymentMethodSearch();
                    } else {
                        mFlowPreference.disableDiscount();
                        retrievePaymentMethodSearch();
                    }
                }
            }
        });
    }

    private void retrievePaymentMethodSearch() {
        getView().showProgress();
        Payer payer = new Payer();
        payer.setAccessToken(mCheckoutPreference.getPayer().getAccessToken());
        getResourcesProvider().getPaymentMethodSearch(getTransactionAmount(), mCheckoutPreference.getExcludedPaymentTypes(), mCheckoutPreference.getExcludedPaymentMethods(), payer, mCheckoutPreference.getSite(), onPaymentMethodSearchRetrieved(), onCustomerRetrieved());
    }

    public BigDecimal getTransactionAmount() {
        BigDecimal amount;

        if (mDiscount != null && isDiscountEnabled() && mDiscount.isValid()) {
            amount = mDiscount.getAmountWithDiscount(mCheckoutPreference.getAmount());
        } else {
            amount = mCheckoutPreference.getAmount();
        }

        return amount;
    }

    private OnResourcesRetrievedCallback<PaymentMethodSearch> onPaymentMethodSearchRetrieved() {
        return new OnResourcesRetrievedCallback<PaymentMethodSearch>() {
            @Override
            public void onSuccess(final PaymentMethodSearch paymentMethodSearch) {
                if (isViewAttached()) {
                    mPaymentMethodSearch = paymentMethodSearch;
                    startFlow();
                }
            }

            @Override
            public void onFailure(final MercadoPagoError error) {
                if (isViewAttached()) {
                    getView().showError(error);
                }
            }
        };
    }

    private OnResourcesRetrievedCallback<Customer> onCustomerRetrieved() {
        return new OnResourcesRetrievedCallback<Customer>() {
            @Override
            public void onSuccess(final Customer customer) {
                if (customer != null) {
                    mCustomerId = customer.getId();
                }
            }

            @Override
            public void onFailure(final MercadoPagoError error) {
                //Do nothing
            }
        };
    }

    private void startFlow() {
        if (mPaymentDataInput != null) {
            showReviewAndConfirm();
        } else if (mPaymentResultInput != null && mPaymentResultInput.getPaymentData() != null) {
            checkStartPaymentResultActivity(mPaymentResultInput);
        } else {
            getView().showPaymentMethodSelection();
        }
    }

    private void showReviewAndConfirm() {
        getView().showReviewAndConfirm();
        mPaymentMethodEditionRequested = false;
    }

    private void checkStartPaymentResultActivity(final PaymentResult paymentResult) {
        if (hasToDeleteESC(paymentResult)) {
            deleteESC(paymentResult.getPaymentData());
        }
        if (hasToContinuePaymentWithoutESC(paymentResult)) {
            continuePaymentWithoutESC();
        } else {
            if (hasToStoreESC(paymentResult)) {
                getResourcesProvider().saveESC(paymentResult.getPaymentData().getToken().getCardId(),
                        paymentResult.getPaymentData().getToken().getEsc());
            }
            if (hasToSkipPaymentResultScreen(paymentResult)) {
                finishCheckout();
            } else {
                getView().showPaymentResult(paymentResult);
            }
        }
    }

    private boolean hasToStoreESC(final PaymentResult paymentResult) {
        return hasValidParametersForESC(paymentResult) &&
                paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_APPROVED) &&
                paymentResult.getPaymentData().getToken().getEsc() != null &&
                !paymentResult.getPaymentData().getToken().getEsc().isEmpty();
    }

    private boolean hasValidParametersForESC(final PaymentResult paymentResult) {
        return paymentResult != null && paymentResult.getPaymentData() != null &&
                paymentResult.getPaymentData().getToken() != null &&
                paymentResult.getPaymentData().getToken().getCardId() != null &&
                !paymentResult.getPaymentData().getToken().getCardId().isEmpty() &&
                paymentResult.getPaymentStatus() != null &&
                paymentResult.getPaymentStatusDetail() != null;
    }

    private boolean hasToDeleteESC(final PaymentResult paymentResult) {
        return hasValidParametersForESC(paymentResult) &&
                !paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_APPROVED);
    }

    private boolean hasToContinuePaymentWithoutESC(final PaymentResult paymentResult) {
        return hasValidParametersForESC(paymentResult) &&
                paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_REJECTED) &&
                paymentResult.getPaymentStatusDetail().equals(Payment.StatusCodes.STATUS_DETAIL_INVALID_ESC);
    }

    private boolean hasToSkipPaymentResultScreen(final PaymentResult paymentResult) {
        String status = paymentResult == null ? "" : paymentResult.getPaymentStatus();
        return shouldSkipResult(status);
    }

    private boolean shouldSkipResult(final String paymentStatus) {
        return !mFlowPreference.isPaymentResultScreenEnabled()
                || (mFlowPreference.getCongratsDisplayTime() != null && mFlowPreference.getCongratsDisplayTime() == 0 && Payment.StatusCodes.STATUS_APPROVED.equals(paymentStatus))
                || Payment.StatusCodes.STATUS_APPROVED.equals(paymentStatus) && !mFlowPreference.isPaymentApprovedScreenEnabled()
                || Payment.StatusCodes.STATUS_REJECTED.equals(paymentStatus) && !mFlowPreference.isPaymentRejectedScreenEnabled()
                || Payment.StatusCodes.STATUS_PENDING.equals(paymentStatus) && !mFlowPreference.isPaymentPendingScreenEnabled();
    }

    private boolean isReviewAndConfirmEnabled() {
        return mFlowPreference.isReviewAndConfirmScreenEnabled();
    }

    public boolean isDiscountEnabled() {
        return mFlowPreference.isDiscountEnabled();
    }

    public boolean isInstallmentsReviewScreenEnabled() {
        return mFlowPreference.isInstallmentsReviewScreenEnabled();
    }

    public boolean isESCEnabled() {
        return mFlowPreference.isESCEnabled();
    }

    public Card getSelectedCard() {
        return mSelectedCard;
    }

    public ServicePreference getServicePreference() {
        return mServicePreference;
    }

    private void retrieveCheckoutPreference() {
        getView().showProgress();
        getResourcesProvider().getCheckoutPreference(mCheckoutPreference.getId(), new OnResourcesRetrievedCallback<CheckoutPreference>() {

            @Override
            public void onSuccess(final CheckoutPreference checkoutPreference) {
                mCheckoutPreference = checkoutPreference;
                startCheckoutForPreference();
            }

            @Override
            public void onFailure(final MercadoPagoError error) {
                getView().showError(error);
                setFailureRecovery(new FailureRecovery() {
                    @Override
                    public void recover() {
                        retrieveCheckoutPreference();
                    }
                });
            }
        });
    }

    public void onErrorCancel(final MercadoPagoError mercadoPagoError) {
        if (isIdentificationInvalidInPayment(mercadoPagoError)) {
            getView().backToPaymentMethodSelection();
        } else if (noUserInteractionReached() || !isReviewAndConfirmEnabled()) {
            getView().cancelCheckout(mercadoPagoError);
        } else {
            showReviewAndConfirm();
        }
    }

    private boolean isIdentificationInvalidInPayment(final MercadoPagoError mercadoPagoError) {
        boolean identificationInvalid = false;
        if (mercadoPagoError != null && mercadoPagoError.isApiException()) {
            List<Cause> causeList = mercadoPagoError.getApiException().getCause();
            if (causeList != null && !causeList.isEmpty()) {
                Cause cause = causeList.get(0);
                if (cause.getCode().equals(ApiException.ErrorCodes.INVALID_IDENTIFICATION_NUMBER)) {
                    identificationInvalid = true;
                }
            }
        }
        return identificationInvalid;
    }

    private boolean noUserInteractionReached() {
        return mSelectedPaymentMethod == null;
    }

    public void onPaymentMethodSelectionResponse(final PaymentMethod paymentMethod,
                                                 final Issuer issuer,
                                                 final PayerCost payerCost,
                                                 final Token token,
                                                 final Discount discount,
                                                 final Card card,
                                                 final Payer payer) {
        mSelectedPaymentMethod = paymentMethod;
        mSelectedIssuer = issuer;
        mSelectedPayerCost = payerCost;
        mCreatedToken = token;
        mDiscount = discount;
        mSelectedCard = card;
        mCollectedPayer = payer;
        onPaymentMethodSelected();
    }

    private void onPaymentMethodSelected() {
        if (!showHook2(createPaymentData())) {
            hook2Continue();
        }
    }

    public void resolvePaymentDataResponse() {
        if (MercadoPagoCheckout.PAYMENT_DATA_RESULT_CODE.equals(mRequestedResult)) {
            PaymentData paymentData = createPaymentData();
            getView().finishWithPaymentDataResult(paymentData, mPaymentMethodEdited);
        } else {
            createPayment();
        }
    }

    private void createPayment() {

        final PaymentData paymentData = createPaymentData();

        if (hasPaymentPlugin()) {
            CheckoutStore.getInstance().setPaymentData(paymentData);
            getView().showPaymentPlugin();
        } else {
            final String transactionId = getTransactionID();
            getResourcesProvider().createPayment(transactionId, mCheckoutPreference,
                    paymentData, mBinaryMode, mCustomerId, new OnResourcesRetrievedCallback<Payment>() {
                @Override
                public void onSuccess(final Payment payment) {
                    mCreatedPayment = payment;
                    PaymentResult paymentResult = createPaymentResult(payment, paymentData);
                    checkStartPaymentResultActivity(paymentResult);
                    cleanTransactionId();
                }
                @Override
                public void onFailure(final MercadoPagoError error) {
                    if (error.isApiException() && error.getApiException().getStatus().equals(ApiUtil.StatusCodes.BAD_REQUEST)
                            && error.getApiException().getCause() != null && !error.getApiException().getCause().isEmpty()) {

                        final List<Cause> causes = error.getApiException().getCause();
                        final Cause cause = causes.get(0);

                        if (ApiException.ErrorCodes.INVALID_PAYMENT_WITH_ESC.equals(cause.getCode()) &&
                                paymentData.getToken().getCardId() != null) {
                            deleteESC(paymentData);
                            continuePaymentWithoutESC();
                        } else {
                            recoverCreatePayment(error);
                        }

                    } else {
                        recoverCreatePayment(error);
                    }
                }
            });
        }
    }

    private boolean hasPaymentPlugin() {
        final PaymentMethodInfo paymentMethodInfo = CheckoutStore.getInstance()
                    .getSelectedPaymentMethod();
        return paymentMethodInfo != null
                && CheckoutStore.getInstance()
                    .getPaymentPluginByMethod(paymentMethodInfo.id) != null;
    }

    private void continuePaymentWithoutESC() {
        mPaymentRecovery = new PaymentRecovery(mCreatedToken, mSelectedPaymentMethod,
                mSelectedPayerCost, mSelectedIssuer, Payment.StatusCodes.STATUS_REJECTED,
                Payment.StatusCodes.STATUS_DETAIL_INVALID_ESC);

        getView().startPaymentRecoveryFlow(mPaymentRecovery);
    }

    private void deleteESC(final PaymentData paymentData) {
        getResourcesProvider().deleteESC(paymentData.getToken().getCardId());
    }

    private void recoverCreatePayment(final MercadoPagoError error) {
        setFailureRecovery(new FailureRecovery() {
            @Override
            public void recover() {
                createPayment();
            }
        });
        resolvePaymentFailure(error);
    }

    private void finishCheckout() {
        if (mCreatedPayment == null) {
            getView().finishWithPaymentResult();
        } else {
            getView().finishWithPaymentResult(mCreatedPayment);
        }
    }

    private void resolvePaymentFailure(final MercadoPagoError mercadoPagoError) {

        if (isPaymentProcessing(mercadoPagoError)) {
            resolveProcessingPaymentStatus();
        } else if (isInternalServerError(mercadoPagoError)) {
            resolveInternalServerError(mercadoPagoError);
        } else if (isBadRequestError(mercadoPagoError)) {
            resolveBadRequestError(mercadoPagoError);
        } else {
            getView().showError(mercadoPagoError);
        }
    }

    private boolean isBadRequestError(final MercadoPagoError mercadoPagoError) {
        return mercadoPagoError != null && mercadoPagoError.getApiException() != null
                && mercadoPagoError.getApiException().getStatus() != null
                && mercadoPagoError.getApiException().getStatus().equals(ApiUtil.StatusCodes.BAD_REQUEST);
    }

    private boolean isInternalServerError(final MercadoPagoError mercadoPagoError) {
        return mercadoPagoError != null && mercadoPagoError.getApiException() != null
                && mercadoPagoError.getApiException().getStatus() != null
                && String.valueOf(mercadoPagoError.getApiException().getStatus()).startsWith(INTERNAL_SERVER_ERROR_FIRST_DIGIT);
    }

    private boolean isPaymentProcessing(final MercadoPagoError mercadoPagoError) {
        return mercadoPagoError != null && mercadoPagoError.getApiException() != null
                && mercadoPagoError.getApiException().getStatus() != null
                && mercadoPagoError.getApiException().getStatus() == ApiUtil.StatusCodes.PROCESSING;
    }

    private void resolveInternalServerError(final MercadoPagoError mercadoPagoError) {
        getView().showError(mercadoPagoError);
        setFailureRecovery(new FailureRecovery() {
            @Override
            public void recover() {
                createPayment();
            }
        });
    }

    private void resolveProcessingPaymentStatus() {
        mCreatedPayment = new Payment();
        mCreatedPayment.setStatus(Payment.StatusCodes.STATUS_IN_PROCESS);
        mCreatedPayment.setStatusDetail(Payment.StatusCodes.STATUS_DETAIL_PENDING_CONTINGENCY);
        PaymentResult paymentResult = createPaymentResult(mCreatedPayment, createPaymentData());
        getView().showPaymentResult(paymentResult);
        cleanTransactionId();
    }

    private void resolveBadRequestError(final MercadoPagoError mercadoPagoError) {
        getView().showError(mercadoPagoError);
    }

    public void onPaymentMethodSelectionError(final MercadoPagoError mercadoPagoError) {
        if (!mPaymentMethodEditionRequested) {
            getView().cancelCheckout(mercadoPagoError);
        } else {
            mPaymentMethodEditionRequested = false;
            getView().backToReviewAndConfirm();
        }
    }

    public void onPaymentMethodSelectionCancel() {
        if (!mPaymentMethodEditionRequested) {
            getView().cancelCheckout();
        } else {
            mPaymentMethodEditionRequested = false;
            getView().backToReviewAndConfirm();
        }
    }

    public void onPaymentConfirmation() {
        if (!showHook3(createPaymentData())) {
            resolvePaymentDataResponse();
        }
    }

    public void changePaymentMethod() {
        if (!isUniquePaymentMethod()) {
            if (mFlowPreference.shouldExitOnPaymentMethodChange()) {
                getView().finishFromReviewAndConfirm();
            } else {
                mPaymentMethodEdited = true;
                mPaymentMethodEditionRequested = true;
                getView().startPaymentMethodEdition();
            }
        }
    }

    public void onReviewAndConfirmCancel() {
        if (mFlowPreference.shouldExitOnPaymentMethodChange() && !isUniquePaymentMethod()) {
            getView().finishFromReviewAndConfirm();
        } else if (isUniquePaymentMethod()) {
            getView().cancelCheckout();
        } else {
            mPaymentMethodEdited = true;
            getView().backToPaymentMethodSelection();
        }
    }

    public void onReviewAndConfirmCancelPayment() {
        getView().cancelCheckout();
    }

    public void onReviewAndConfirmError(final MercadoPagoError mercadoPagoError) {
        getView().cancelCheckout(mercadoPagoError);
    }

    public void onPaymentResultCancel(final String nextAction) {
        if (!TextUtils.isEmpty(nextAction)) {
            if (nextAction.equals(PaymentResult.SELECT_OTHER_PAYMENT_METHOD)) {
                mPaymentMethodEdited = true;
                getView().backToPaymentMethodSelection();
            } else if (nextAction.equals(PaymentResult.RECOVER_PAYMENT)) {
                recoverPayment();
            }
        }
    }

    public void onPaymentResultResponse() {
        finishCheckout();
    }

    public void onCardFlowResponse(final PaymentMethod paymentMethod,
                                   final Issuer issuer,
                                   final PayerCost payerCost,
                                   final Token token,
                                   final Discount discount) {

        mSelectedPaymentMethod = paymentMethod;
        mSelectedIssuer = issuer;
        mSelectedPayerCost = payerCost;
        mCreatedToken = token;
        mDiscount = discount;

        if (isRecoverableTokenProcess()) {
            resolvePaymentDataResponse();
        } else {
            onPaymentMethodSelected();
        }
    }

    public void onCardFlowError(final MercadoPagoError mercadoPagoError) {
        getView().cancelCheckout(mercadoPagoError);
    }

    public void onCardFlowCancel() {
        mPaymentMethodEdited = true;
        getView().backToPaymentMethodSelection();
    }

    public void onCustomReviewAndConfirmResponse(final Integer customResultCode, final PaymentData paymentData) {
        getView().cancelCheckout(customResultCode, paymentData, mPaymentMethodEdited);
    }

    public void onCustomPaymentResultResponse(final Integer customResultCode) {
        if (mCreatedPayment == null) {
            getView().finishWithPaymentResult(customResultCode);
        } else {
            getView().finishWithPaymentResult(customResultCode, mCreatedPayment);
        }
    }

    public boolean isUniquePaymentMethod() {
        return isOnlyUniquePaymentMethodAvailable() || isOnlyAccountMoneyEnabled();
    }

    private boolean isOnlyUniquePaymentMethodAvailable() {
        return mPaymentMethodSearch != null && mPaymentMethodSearch.hasSearchItems() && mPaymentMethodSearch.getGroups().size() == 1 && mPaymentMethodSearch.getGroups().get(0).isPaymentMethod() && !mPaymentMethodSearch.hasCustomSearchItems();
    }

    private boolean isOnlyAccountMoneyEnabled() {
        return mPaymentMethodSearch.hasCustomSearchItems()
                && mPaymentMethodSearch.getCustomSearchItems().size() == 1
                && mPaymentMethodSearch.getCustomSearchItems().get(0).getId().equals(PaymentMethods.ACCOUNT_MONEY)
                && (mPaymentMethodSearch.getGroups() == null || mPaymentMethodSearch.getGroups().isEmpty());
    }

    private PaymentResult createPaymentResult(final Payment payment, final PaymentData paymentData) {
        return new PaymentResult.Builder()
                .setPaymentData(paymentData)
                .setPaymentId(payment.getId())
                .setPaymentStatus(payment.getStatus())
                .setPaymentStatusDetail(payment.getStatusDetail())
                .setPayerEmail(mCheckoutPreference.getPayer().getEmail())
                .setStatementDescription(payment.getStatementDescriptor())
                .build();
    }

    private PaymentData createPaymentData() {
        PaymentData paymentData = new PaymentData();
        paymentData.setPaymentMethod(mSelectedPaymentMethod);
        paymentData.setPayerCost(mSelectedPayerCost);
        paymentData.setIssuer(mSelectedIssuer);
        paymentData.setDiscount(mDiscount);
        paymentData.setToken(mCreatedToken);
        paymentData.setTransactionAmount(mCheckoutPreference.getAmount());

        Payer payer = createPayerFrom(mCheckoutPreference.getPayer(), mCollectedPayer);
        paymentData.setPayer(payer);

        return paymentData;
    }

    private Payer createPayerFrom(final Payer checkoutPreferencePayer,
                                  final Payer collectedPayer) {
        Payer payerForPayment;
        if (checkoutPreferencePayer != null && collectedPayer != null) {
            payerForPayment = copy(checkoutPreferencePayer);
            payerForPayment.setFirstName(collectedPayer.getFirstName());
            payerForPayment.setLastName(collectedPayer.getLastName());
            payerForPayment.setIdentification(collectedPayer.getIdentification());
        } else {
            payerForPayment = checkoutPreferencePayer;
        }
        return payerForPayment;
    }

    private Payer copy(final Payer original) {
        return JsonUtil.getInstance().fromJson(JsonUtil.getInstance().toJson(original), Payer.class);
    }

    private void recoverPayment() {
        try {
            String paymentStatus = mCreatedPayment == null ? mPaymentResultInput.getPaymentStatus() : mCreatedPayment.getStatus();
            String paymentStatusDetail = mCreatedPayment == null ? mPaymentResultInput.getPaymentStatusDetail() : mCreatedPayment.getStatusDetail();
            mPaymentRecovery = new PaymentRecovery(mCreatedToken, mSelectedPaymentMethod, mSelectedPayerCost, mSelectedIssuer, paymentStatus, paymentStatusDetail);
            getView().startPaymentRecoveryFlow(mPaymentRecovery);
        } catch (IllegalStateException e) {
            String message = getResourcesProvider().getCheckoutExceptionMessage(e);
            getView().showError(new MercadoPagoError(message, e.getMessage(), false));
        }
    }

    public void recoverFromFailure() {
        if (failureRecovery != null) {
            failureRecovery.recover();
        } else {
            IllegalStateException e = new IllegalStateException("Failure recovery not defined");
            getView().showError(new MercadoPagoError(getResourcesProvider().getCheckoutExceptionMessage(e), false));
        }
    }

    public void setFailureRecovery(final FailureRecovery failureRecovery) {
        this.failureRecovery = failureRecovery;
    }

    private boolean isRecoverableTokenProcess() {
        return mPaymentRecovery != null && mPaymentRecovery.isTokenRecoverable();
    }

    private String getTransactionID() {
        if (!existsTransactionId() || mPaymentMethodEdited) {
            mCurrentPaymentIdempotencyKey = createNewTransactionId();
        }
        return mCurrentPaymentIdempotencyKey;
    }

    private String createNewTransactionId() {
        return String.valueOf(mIdempotencyKeySeed + Calendar.getInstance().getTimeInMillis());
    }

    private boolean existsTransactionId() {
        return mCurrentPaymentIdempotencyKey != null;
    }

    private void cleanTransactionId() {
        mCurrentPaymentIdempotencyKey = null;
    }

    public void setIdempotencyKeySeed(final String idempotencyKeySeed) {
        mIdempotencyKeySeed = idempotencyKeySeed;
    }

    public ReviewScreenPreference getReviewScreenPreference() {
        return mReviewScreenPreference;
    }

    public PaymentMethod getSelectedPaymentMethod() {
        return mSelectedPaymentMethod;
    }

    public Issuer getIssuer() {
        return mSelectedIssuer;
    }

    public PayerCost getSelectedPayerCost() {
        return mSelectedPayerCost;
    }

    public Token getCreatedToken() {
        return mCreatedToken;
    }

    public Payment getCreatedPayment() {
        return mCreatedPayment;
    }

    public PaymentResultScreenPreference getPaymentResultScreenPreference() {
        return mPaymentResultScreenPreference;
    }

    public Integer getCongratsDisplay() {
        return mFlowPreference.getCongratsDisplayTime();
    }

    public void setCheckoutPreference(final CheckoutPreference checkoutPreference) {
        this.mCheckoutPreference = checkoutPreference;
    }

    public void setPaymentResultScreenPreference(final PaymentResultScreenPreference paymentResultScreenPreference) {
        this.mPaymentResultScreenPreference = paymentResultScreenPreference;
    }

    public void setServicePreference(final ServicePreference servicePreference) {
        if (servicePreference != null) {
            this.mServicePreference = servicePreference;
        }
    }

    public void setReviewScreenPreference(final ReviewScreenPreference reviewScreenPreference) {
        this.mReviewScreenPreference = reviewScreenPreference;
    }

    public void setFlowPreference(final FlowPreference flowPreference) {
        if (flowPreference != null) {
            this.mFlowPreference = flowPreference;
        }
    }

    public void setBinaryMode(Boolean binaryMode) {
        this.mBinaryMode = binaryMode;
    }

    public void setDiscount(Discount discount) {
        this.mDiscount = discount;
    }

    public void setDirectDiscountEnabled(final Boolean directDiscountEnabled) {
        this.mDirectDiscountEnabled = directDiscountEnabled;
    }

    public void setPaymentDataInput(final PaymentData paymentDataInput) {
        this.mPaymentDataInput = paymentDataInput;
    }

    public void setPaymentResultInput(final PaymentResult paymentResultInput) {
        this.mPaymentResultInput = paymentResultInput;
    }

    public void setRequestedResult(final Integer requestedResult) {
        this.mRequestedResult = requestedResult;
    }

    public void setTimer(Timer timer) {
        this.mCheckoutTimer = timer;
    }

    public CheckoutPreference getCheckoutPreference() {
        return mCheckoutPreference;
    }

    public PaymentMethodSearch getPaymentMethodSearch() {
        return mPaymentMethodSearch;
    }

    public void setPaymentMethodSearch(final PaymentMethodSearch paymentMethodSearch) {
        this.mPaymentMethodSearch = paymentMethodSearch;
    }

    public Discount getDiscount() {
        return mDiscount;
    }

    public boolean isDirectDiscountEnabled() {
        return mDirectDiscountEnabled;
    }

    public Boolean getShowBankDeals() {
        return mFlowPreference.isBankDealsEnabled() && mServicePreference.shouldShowBankDeals();
    }

    public Boolean shouldShowAllSavedCards() {
        return mFlowPreference.isShowAllSavedCardsEnabled();
    }

    public Boolean isDiscountValid() {
        return mDiscount != null && mDiscount.isValid();
    }

    public Integer getMaxSavedCardsToShow() {
        return mFlowPreference.getMaxSavedCardsToShow();
    }

    //### Hooks #####################

    public boolean showHook2(final PaymentData paymentData) {
        return showHook2(paymentData, MercadoPagoComponents.Activities.HOOK_2);
    }

    public boolean showHook2(final PaymentData paymentData, final int requestCode) {
        final Map<String, Object> data = CheckoutStore.getInstance().getData();
        final Hook hook = HookHelper.activateAfterPaymentMethodConfig(
                CheckoutStore.getInstance().getCheckoutHooks(), paymentData, data);
        if (hook != null && getView() != null) {
            getView().showHook(hook, requestCode);
            return true;
        }
        return false;
    }

    public boolean showHook3(final PaymentData paymentData) {
        return showHook3(paymentData, MercadoPagoComponents.Activities.HOOK_3);
    }

    public boolean showHook3(final PaymentData paymentData, final int requestCode) {
        final Map<String, Object> data = CheckoutStore.getInstance().getData();
        final Hook hook = HookHelper.activateBeforePayment(
                CheckoutStore.getInstance().getCheckoutHooks(), paymentData, data);
        if (hook != null && getView() != null) {
            getView().showHook(hook, requestCode);
            return true;
        }
        return false;
    }

    public void hook2Continue() {
        mPaymentMethodEditionRequested = false;
        if (isReviewAndConfirmEnabled()) {
            showReviewAndConfirm();
        } else {
            resolvePaymentDataResponse();
        }
    }
}