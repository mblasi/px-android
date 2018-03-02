package com.mercadopago.plugins.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentData;
import com.mercadopago.paymentresult.components.Footer;

/**
 * Created by nfortuna on 12/29/17.
 */

public class ProcessorPaymentResult {

    public final Long paymentId;
    public final String status;
    public final String statusDetail;
    public final String headerTitle;
    public final @DrawableRes int headerIcon;
    public final Footer.FooterAction footerButtonAction;
    public final Footer.FooterAction footerBlinkAction;

    public ProcessorPaymentResult(final Long paymentId, final String status, final String statusDetail,
                                  final PaymentData paymentData) {
        this.paymentId = paymentId;
        this.status = status;
        this.statusDetail = processStatusDetail(status, statusDetail);
        this.headerTitle = null;
        this.headerIcon = 0;
        this.footerButtonAction = null;
        this.footerBlinkAction = null;
    }

    public ProcessorPaymentResult(final Builder builder) {
        this.paymentId = builder.paymentId;
        this.status = builder.status;
        this.statusDetail = processStatusDetail(builder.status, builder.statusDetail);
        this.headerTitle = builder.headerTitle;
        this.headerIcon = builder.headerIcon;
        this.footerButtonAction = builder.footerButtonAction;
        this.footerBlinkAction = builder.footerBlinkAction;
    }

    private String processStatusDetail(@NonNull final String status, @NonNull final String statusDetail) {
        if (Payment.StatusCodes.STATUS_APPROVED.equals(status)) {
            return Payment.StatusCodes.STATUS_DETAIL_APPROVED_PLUGIN_PM;
        } if (Payment.StatusCodes.STATUS_REJECTED.equals(status)) {
            if (Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_OTHER.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_SECURITY_CODE.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_DATE.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_CARD_NUMBER.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_DUPLICATED_PAYMENT.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_INSUFFICIENT_AMOUNT.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_MAX_ATTEMPTS.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_INVALID_ESC.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_REJECTED_HIGH_RISK.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_REJECTED_REJECTED_BY_BANK.equals(statusDetail)
                    || Payment.StatusCodes.STATUS_DETAIL_REJECTED_REJECTED_INSUFFICIENT_DATA.equals(statusDetail)) {
                return statusDetail;
            } else {
                return Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_PLUGIN_PM;
            }
        }
        return statusDetail;
    }

    public static class Builder {
        protected Long paymentId;
        protected String status;
        protected String statusDetail;
        protected PaymentData paymentData;
        protected String headerTitle;
        protected @DrawableRes int headerIcon;
        private Footer.FooterAction footerButtonAction;
        private Footer.FooterAction footerBlinkAction;

        public Builder setPaymentId(final Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setStatus(final String status) {
            this.status = status;
            return this;
        }

        public Builder setStatusDetail(final String statusDetail) {
            this.statusDetail = statusDetail;
            return this;
        }

        public Builder setHeaderTitle(final String headerTitle) {
            this.headerTitle = headerTitle;
            return this;
        }

        public Builder setHeaderIcon(final int headerIcon) {
            this.headerIcon = headerIcon;
            return this;
        }

        public Builder setFooterButtonAction(final Footer.FooterAction footerButtonAction) {
            this.footerButtonAction = footerButtonAction;
            return this;
        }

        public Builder setFooterBlinkAction(final Footer.FooterAction footerBlinkAction) {
            this.footerBlinkAction = footerBlinkAction;
            return this;
        }

        public ProcessorPaymentResult build() {
            return new ProcessorPaymentResult(this);
        }
    }
}