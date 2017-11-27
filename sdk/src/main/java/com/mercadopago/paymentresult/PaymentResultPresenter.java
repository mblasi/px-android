package com.mercadopago.paymentresult;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.mercadopago.callbacks.FailureRecovery;
import com.mercadopago.components.Action;
import com.mercadopago.components.ActionsListener;
import com.mercadopago.components.LinkAction;
import com.mercadopago.components.ChangePaymentMethodAction;
import com.mercadopago.components.RecoverPaymentAction;
import com.mercadopago.components.ResultCodeAction;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.model.Instruction;
import com.mercadopago.model.Instructions;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentResult;
import com.mercadopago.model.Site;
import com.mercadopago.mvp.MvpPresenter;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;
import com.mercadopago.paymentresult.formatter.HeaderTitleFormatter;
import com.mercadopago.preferences.PaymentResultScreenPreference;
import com.mercadopago.preferences.ServicePreference;
import com.mercadopago.util.ApiUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentResultPresenter extends MvpPresenter<PaymentResultPropsView, PaymentResultProvider> implements ActionsListener {
    //FIXME: No se usa ?
    private Boolean discountEnabled;
    private PaymentResult paymentResult;
    private Site site;
    private BigDecimal amount;
    private ServicePreference servicePreference;
    private PaymentResultScreenPreference paymentResultScreenPreference;
    private PaymentResultNavigator navigator;
    private FailureRecovery failureRecovery;

    public PaymentResultPresenter(@NonNull final PaymentResultNavigator navigator) {
        this.navigator = navigator;
        this.paymentResultScreenPreference = new PaymentResultScreenPreference.Builder().build();
    }

    public void initialize() {
        try {
            validateParameters();
            onValidStart();
        } catch (final IllegalStateException exception) {
            navigator.showError(new MercadoPagoError(exception.getMessage(), false), "");
        }
    }

    private void validateParameters() {
        if (!isPaymentResultValid()) {
            throw new IllegalStateException("payment result is invalid");
        } else if (!isPaymentMethodValid()) {
            throw new IllegalStateException("payment data is invalid");
        } else if (isPaymentMethodOff()) {
            if (!isPaymentIdValid()) {
                throw new IllegalStateException("payment id is invalid");
            } else if (!isSiteValid()) {
                throw new IllegalStateException("site is invalid");
            }
        }
    }

    protected void onValidStart() {
        HeaderTitleFormatter amountFormat = null;
        if (isCallForAuthorize()) {
            amountFormat = new HeaderTitleFormatter(site.getCurrencyId(), amount, paymentResult.getPaymentData().getPaymentMethod().getName());
        }
        boolean showLoading = false;
        if (hasToAskForInstructions()) {
            showLoading = true;
        }
        getView().setPropPaymentResult(paymentResult, amountFormat, showLoading);
        checkGetInstructions();
    }

    private boolean isCallForAuthorize() {
        return paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_REJECTED) &&
                paymentResult.getPaymentStatusDetail().equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE);
    }

    private boolean isPaymentResultValid() {
        return paymentResult != null && paymentResult.getPaymentStatus() != null && paymentResult.getPaymentStatusDetail() != null;
    }

    private boolean isPaymentMethodValid() {
        return paymentResult != null && paymentResult.getPaymentData() != null && paymentResult.getPaymentData().getPaymentMethod() != null &&
                paymentResult.getPaymentData().getPaymentMethod().getId() != null && !paymentResult.getPaymentData().getPaymentMethod().getId().isEmpty() &&
                paymentResult.getPaymentData().getPaymentMethod().getPaymentTypeId() != null && !paymentResult.getPaymentData().getPaymentMethod().getPaymentTypeId().isEmpty() &&
                paymentResult.getPaymentData().getPaymentMethod().getName() != null && !paymentResult.getPaymentData().getPaymentMethod().getName().isEmpty();
    }

    private boolean isPaymentIdValid() {
        return paymentResult.getPaymentId() != null;
    }

    private boolean isSiteValid() {
        return site != null && site.getCurrencyId() != null && !site.getCurrencyId().isEmpty();
    }

    private boolean isPaymentMethodOff() {
        final String paymentStatus = paymentResult.getPaymentStatus();
        final String paymentStatusDetail = paymentResult.getPaymentStatusDetail();
        return paymentStatus.equals(Payment.StatusCodes.STATUS_PENDING)
                && paymentStatusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_PENDING_WAITING_PAYMENT);
    }

    public void setDiscountEnabled(final Boolean discountEnabled) {
        this.discountEnabled = discountEnabled;
    }

    public void setPaymentResult(final PaymentResult paymentResult) {
        this.paymentResult = paymentResult;
    }

    public void setSite(final Site site) {
        this.site = site;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentResultScreenPreference(final PaymentResultScreenPreference paymentResultScreenPreference) {
        if (paymentResultScreenPreference != null) {
            this.paymentResultScreenPreference = paymentResultScreenPreference;
            getView().setPaymentResultScreenPreference(this.paymentResultScreenPreference);
            getView().notifyPropsChanged();
        }
    }

    public void setServicePreference(final ServicePreference servicePreference) {
        if (servicePreference != null) {
            this.servicePreference = servicePreference;
        }
    }

    private void checkGetInstructions() {
        if (hasToAskForInstructions()) {
            getInstructionsAsync(paymentResult.getPaymentId(), paymentResult.getPaymentData()
                    .getPaymentMethod().getPaymentTypeId());
        } else {
            getView().notifyPropsChanged();
        }
    }

    private boolean hasToAskForInstructions() {
        return isPaymentMethodOff();
    }

    private void getInstructionsAsync(final Long paymentId, final String paymentTypeId) {
        getResourcesProvider().getInstructionsAsync(paymentId, paymentTypeId, new OnResourcesRetrievedCallback<Instructions>() {
            @Override
            public void onSuccess(Instructions instructions) {
                final List<Instruction> instructionsList
                        = instructions.getInstructions() == null ? new ArrayList<Instruction>() : instructions.getInstructions();
                if (instructionsList.isEmpty()) {
                    navigator.showError(new MercadoPagoError(getResourcesProvider().getStandardErrorMessage(), false), ApiUtil.RequestOrigin.GET_INSTRUCTIONS);
                } else {
                    resolveInstructions(instructionsList);
                }
            }

            @Override
            public void onFailure(final MercadoPagoError error) {
                //TODO revisar
                if (navigator != null) {
                    navigator.showError(error, ApiUtil.RequestOrigin.GET_INSTRUCTIONS);
                    setFailureRecovery(new FailureRecovery() {
                        @Override
                        public void recover() {
                            getInstructionsAsync(paymentId, paymentTypeId);
                        }
                    });
                }
            }
        });
    }

    public void recoverFromFailure() {
        if (failureRecovery != null) {
            failureRecovery.recover();
        }
    }

    private void setFailureRecovery(final FailureRecovery failureRecovery) {
        this.failureRecovery = failureRecovery;
    }

    private void resolveInstructions(final List<Instruction> instructionsList) {
        final Instruction instruction = getInstruction(instructionsList);
        if (instruction == null) {
            navigator.showError(new MercadoPagoError(getResourcesProvider().getStandardErrorMessage(), false), ApiUtil.RequestOrigin.GET_INSTRUCTIONS);
        } else {
            getView().setPropInstruction(instruction, new HeaderTitleFormatter(site.getCurrencyId(), amount), false, servicePreference.getProcessingModeString());
            getView().notifyPropsChanged();
        }
    }

    private Instruction getInstruction(final List<Instruction> instructions) {
        final Instruction instruction;
        if (instructions.size() == 1) {
            instruction = instructions.get(0);
        } else {
            instruction = getInstructionForType(instructions, paymentResult.getPaymentData().getPaymentMethod().getPaymentTypeId());
        }
        return instruction;
    }

    private Instruction getInstructionForType(final List<Instruction> instructions, final String paymentTypeId) {
        Instruction instructionForType = null;
        for (Instruction instruction : instructions) {
            if (instruction.getType().equals(paymentTypeId)) {
                instructionForType = instruction;
                break;
            }
        }
        return instructionForType;
    }

    @Override
    public void onAction(@NonNull final Action action) {
        if (Action.TYPE_CONTINUE == action.type) {
            navigator.finishWithResult(Activity.RESULT_OK);
        } else if (action instanceof ResultCodeAction) {
            navigator.finishWithResult(((ResultCodeAction) action).resultCode);
        } else if (action instanceof ChangePaymentMethodAction) {
            navigator.changePaymentMethod();
        } else if (action instanceof RecoverPaymentAction) {
            navigator.recoverPayment();
        } else if (action instanceof LinkAction) {
            navigator.openLink(((LinkAction) action).url);
        }

    }

    public Boolean getDiscountEnabled() {
        return discountEnabled;
    }

    public PaymentResult getPaymentResult() {
        return paymentResult;
    }

    public Site getSite() {
        return site;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ServicePreference getServicePreference() {
        return servicePreference;
    }

}
