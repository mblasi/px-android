package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.ChangePaymentMethodAction;
import com.mercadopago.components.Component;
import com.mercadopago.components.RecoverPaymentAction;
import com.mercadopago.components.ResultCodeAction;
import com.mercadopago.core.CheckoutStore;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentResult;
import com.mercadopago.paymentresult.PaymentResultProvider;
import com.mercadopago.preferences.PaymentResultScreenPreference;
import com.mercadopago.util.TextUtils;

public class FooterContainer extends Component<FooterContainer.Props, Void> {

    public PaymentResultProvider resourcesProvider;

    public FooterContainer(@NonNull final Props props,
                           @NonNull final ActionDispatcher dispatcher,
                           @NonNull final PaymentResultProvider provider) {
        super(props, dispatcher);
        this.resourcesProvider = provider;
    }

    public Footer getFooter() {
        return new Footer(getFooterProps(), getDispatcher());
    }

    private Footer.Props getFooterProps() {

        final PaymentResultScreenPreference preferences = CheckoutStore.getInstance().getPaymentResultScreenPreference();

        Footer.FooterAction buttonAction = props.paymentResult.footerButtonAction;
        Footer.FooterAction linkAction = props.paymentResult.footerlinkAction;

        if (buttonAction == null) {
            buttonAction = getButtonAction();
        }

        if (linkAction == null) {
            linkAction = getLinkAction();
        }

        return new Footer.Props(
            buttonAction, linkAction
        );
    }

    private Footer.FooterAction getButtonAction() {

        Footer.FooterAction buttonAction = null;
        final PaymentResultScreenPreference preferences = CheckoutStore.getInstance().getPaymentResultScreenPreference();

        if (props.paymentResult.isStatusApproved()) {

            if (!preferences.isCongratsSecondaryExitButtonEnabled() ||
                    preferences.getSecondaryCongratsExitButtonTitle() == null
                    || preferences.getSecondaryCongratsExitResultCode() == null) {
                buttonAction = null;
            } else {
                buttonAction = new Footer.FooterAction(
                        preferences.getSecondaryCongratsExitButtonTitle(),
                        new ResultCodeAction(preferences.getSecondaryCongratsExitResultCode())
                );
            }

        } else if (props.paymentResult.isStatusPending() || props.paymentResult.isStatusInProcess()) {

            if (!preferences.isPendingSecondaryExitButtonEnabled() ||
                    preferences.getSecondaryPendingExitButtonTitle() == null
                    || preferences.getSecondaryPendingExitResultCode() == null) {
                buttonAction = null;
            } else {
                buttonAction = new Footer.FooterAction(
                        preferences.getSecondaryPendingExitButtonTitle(),
                        new ResultCodeAction(preferences.getSecondaryPendingExitResultCode())
                );
            }

        } else if (props.paymentResult.isStatusRejected()) {

            if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                buttonAction = new Footer.FooterAction(
                        resourcesProvider.getChangePaymentMethodLabel(),
                        new ChangePaymentMethodAction()
                );

            } else if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                buttonAction = new Footer.FooterAction(
                        resourcesProvider.getCardEnabled(),
                        new RecoverPaymentAction()
                );

            } else if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_INSUFFICIENT_AMOUNT
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                buttonAction = new Footer.FooterAction(
                        resourcesProvider.getChangePaymentMethodLabel(),
                        new ChangePaymentMethodAction()
                );

            } else if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_DATE
                    .equals(props.paymentResult.getPaymentStatusDetail())
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_SECURITY_CODE
                    .equals(props.paymentResult.getPaymentStatusDetail())
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_OTHER
                    .equals(props.paymentResult.getPaymentStatusDetail())
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_CARD_NUMBER
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                buttonAction = new Footer.FooterAction(
                        resourcesProvider.getRejectedBadFilledCardTitle(),
                        new RecoverPaymentAction()
                );

            } else if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_DUPLICATED_PAYMENT
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                buttonAction = null;

            } else {

                buttonAction = new Footer.FooterAction(
                        resourcesProvider.getChangePaymentMethodLabel(),
                        new ChangePaymentMethodAction()
                );
            }

            // Remove the button by user preference
            if (!preferences.isRejectedSecondaryExitButtonEnabled()) {
                buttonAction = null;
            }
        }

        return buttonAction;
    }

    private Footer.FooterAction getLinkAction() {

        Footer.FooterAction linkAction = null;
        final PaymentResultScreenPreference preferences = CheckoutStore.getInstance().getPaymentResultScreenPreference();

        if (props.paymentResult.isStatusApproved()) {

            if (TextUtils.isEmpty(preferences.getExitButtonTitle())) {
                linkAction = new Footer.FooterAction(resourcesProvider.getContinueShopping());
            } else {
                linkAction = new Footer.FooterAction(preferences.getExitButtonTitle());
            }

        } else if (props.paymentResult.isStatusPending() || props.paymentResult.isStatusInProcess()) {

            if (TextUtils.isEmpty(preferences.getExitButtonTitle())) {
                linkAction = new Footer.FooterAction(resourcesProvider.getContinueShopping());
            } else {
                linkAction = new Footer.FooterAction(preferences.getExitButtonTitle());
            }

        } else if (props.paymentResult.isStatusRejected()) {

            if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                linkAction = new Footer.FooterAction(resourcesProvider.getCancelPayment());

            } else if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                linkAction = new Footer.FooterAction(resourcesProvider.getChangePaymentMethodLabel(),
                        new ChangePaymentMethodAction());

            } else if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_INSUFFICIENT_AMOUNT
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                linkAction = new Footer.FooterAction(resourcesProvider.getCancelPayment());

            } else if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_DATE
                    .equals(props.paymentResult.getPaymentStatusDetail())
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_SECURITY_CODE
                    .equals(props.paymentResult.getPaymentStatusDetail())
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_OTHER
                    .equals(props.paymentResult.getPaymentStatusDetail())
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_CARD_NUMBER
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                linkAction = new Footer.FooterAction(resourcesProvider.getChangePaymentMethodLabel(),
                        new ChangePaymentMethodAction());

            } else if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_DUPLICATED_PAYMENT
                    .equals(props.paymentResult.getPaymentStatusDetail())) {

                linkAction = new Footer.FooterAction(resourcesProvider.getContinueShopping());

            } else {

                linkAction = new Footer.FooterAction(resourcesProvider.getCancelPayment());
            }
        }

        return linkAction;
    }

    public static class Props {

        public final PaymentResult paymentResult;

        public Props(final PaymentResult paymentResult) {
            this.paymentResult = paymentResult;
        }
    }
}