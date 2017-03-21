package com.mercadopago.presenters;

import com.mercadopago.callbacks.OnSelectedCallback;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.model.CustomSearchItem;
import com.mercadopago.model.Discount;
import com.mercadopago.model.DiscountSearch;
import com.mercadopago.model.DiscountSearchItem;
import com.mercadopago.model.PaymentMethodSearchItem;
import com.mercadopago.mvp.MvpPresenter;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;
import com.mercadopago.providers.DiscountsProvider;
import com.mercadopago.views.DiscountsView;

import java.math.BigDecimal;
import com.mercadopago.views.DiscountsActivityView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by mromar on 11/29/16.
 */

public class DiscountsPresenter extends MvpPresenter<DiscountsActivityView, DiscountsProvider> {

    private DiscountsActivityView mDiscountsView;

    //Activity parameters
    private String mPublicKey;
    private String mPayerEmail;
    private BigDecimal mTransactionAmount;
    private Discount mDiscount;
    private Boolean mDirectDiscountEnabled;

    private DiscountSearch mDiscountSearch;

    @Override
    public void attachView(DiscountsActivityView discountsView) {
        this.mDiscountsView = discountsView;
    }

    public void initialize() {
        //TODO ordenar
        getDiscountSearch();

        if (mDiscount == null) {
            initDiscountFlow();
        } else {
            mDiscountsView.drawSummary();
        }
    }

    private void initDiscountFlow() {
        if (mDirectDiscountEnabled && isTransactionAmountValid()) {
            mDiscountsView.hideDiscountSummary();
            getDirectDiscount();
        } else {
            mDiscountsView.requestDiscountCode();
        }
    }

    private Boolean isTransactionAmountValid() {
        return mTransactionAmount != null && mTransactionAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    private void getDiscountSearch() {
        getResourcesProvider().getDiscountSearch(mTransactionAmount.toString(), mPayerEmail,new OnResourcesRetrievedCallback<DiscountSearch>() {
            @Override
            public void onSuccess(DiscountSearch discountSearch) {
                mDiscountSearch = discountSearch;
                resolveDiscountSearch();
            }

            @Override
            public void onFailure(MercadoPagoError error) {
                mDiscountsView.requestDiscountCode();
            }
        });
    }

    private void resolveDiscountSearch() {
        if (viewAttached()) {

            if (noDiscountsAvailable()) {
                //TODO mostrar solo burbuja de agregar tarjeta
                //TODO se puede hacer una selección automática
//                showEmptyPaymentMethodsError();
            } else {
                showAvailableOptions();
//                getView().hideProgress();
            }
        }
    }

    private void showAvailableOptions() {

//        if (mPaymentMethodSearch.hasCustomSearchItems()) {
//            List<CustomSearchItem> shownCustomItems;
//            if (mMaxSavedCards != null && mMaxSavedCards > 0) {
//                shownCustomItems = getLimitedCustomOptions(mPaymentMethodSearch.getCustomSearchItems(), mMaxSavedCards);
//            } else {
//                shownCustomItems = mPaymentMethodSearch.getCustomSearchItems();
//            }
//            getView().showCustomOptions(shownCustomItems, getCustomOptionCallback());
//        }

        if (searchItemsAvailable()) {
            getView().showSearchItems(mDiscountSearch.getGroups(), getDiscountSearchItemSelectionCallback());
        }
    }

    private OnSelectedCallback<DiscountSearchItem> getDiscountSearchItemSelectionCallback() {
        return new OnSelectedCallback<DiscountSearchItem>() {
            @Override
            public void onSelected(DiscountSearchItem item) {
                selectItem(item);
            }
        };
    }

    private void selectItem(DiscountSearchItem item) {
//        if (item.hasChildren()) {
//            getView().restartWithSelectedItem(item);
//        } else if (item.isPaymentType()) {
//            startNextStepForPaymentType(item);
//        } else if (item.isPaymentMethod()) {
//            resolvePaymentMethodSelection(item);
//        }
    }

    private boolean searchItemsAvailable() {
        return mDiscountSearch != null && mDiscountSearch.getGroups() != null && !mDiscountSearch.getGroups().isEmpty();
    }

    private boolean noDiscountsAvailable() {
        return mDiscountSearch.getGroups() == null || mDiscountSearch.getGroups().isEmpty();
    }

    private boolean viewAttached() {
        return getView() != null;
    }

    private void getDirectDiscount() {
        getResourcesProvider().getDirectDiscount(mTransactionAmount.toString(), mPayerEmail, new OnResourcesRetrievedCallback<Discount>() {
            @Override
            public void onSuccess(Discount discount) {
                mDiscount = discount;
                mDiscountsView.drawSummary();
            }

            @Override
            public void onFailure(MercadoPagoError error) {
                mDiscountsView.requestDiscountCode();
            }
        });
    }

    private void getCodeDiscount(final String discountCode) {
        mDiscountsView.showProgressBar();

        getResourcesProvider().getCodeDiscount(mTransactionAmount.toString(), mPayerEmail, discountCode, new OnResourcesRetrievedCallback<Discount>() {
            @Override
            public void onSuccess(Discount discount) {
                mDiscountsView.setSoftInputModeSummary();
                mDiscountsView.hideKeyboard();
                mDiscountsView.hideProgressBar();

                mDiscount = discount;
                mDiscount.setCouponCode(discountCode);
                mDiscountsView.drawSummary();
            }

            @Override
            public void onFailure(MercadoPagoError error) {
                mDiscountsView.hideProgressBar();
                if(error.isApiException()) {
                    String errorMessage = getResourcesProvider().getApiErrorMessage(error.getApiException().getError());
                    mDiscountsView.showCodeInputError(errorMessage);
                } else {
                    mDiscountsView.showCodeInputError(getResourcesProvider().getStandardErrorMessage());
                }
            }
        });
    }

    public void validateDiscountCodeInput(String discountCode) {
        if (isTransactionAmountValid()) {
            if (isEmpty(discountCode)) {
                mDiscountsView.showEmptyDiscountCodeError();
            } else {
                getCodeDiscount(discountCode);
            }
        } else {
            mDiscountsView.finishWithCancelResult();
        }
    }

    public Discount getDiscount() {
        return this.mDiscount;
    }

    public void setMerchantPublicKey(String publicKey) {
        this.mPublicKey = publicKey;
    }

    public void setPayerEmail(String payerEmail) {
        this.mPayerEmail = payerEmail;
    }

    public void setDiscount(Discount discount) {
        this.mDiscount = discount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.mTransactionAmount = transactionAmount;
    }

    public void setDirectDiscountEnabled(Boolean directDiscountEnabled) {
        this.mDirectDiscountEnabled = directDiscountEnabled;
    }

    public Boolean getDirectDiscountEnabled() {
        return this.mDirectDiscountEnabled;
    }

    public String getCurrencyId() {
        return mDiscount.getCurrencyId();
    }

    public BigDecimal getTransactionAmount() {
        return mTransactionAmount;
    }

    public BigDecimal getCouponAmount() {
        return mDiscount.getCouponAmount();
    }

    public String getPublicKey() {
        return this.mPublicKey;
    }

    private boolean isEmpty(String discountCode) {
        return discountCode == null || discountCode.isEmpty();
    }
}
