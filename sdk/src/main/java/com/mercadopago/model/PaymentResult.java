package com.mercadopago.model;

import android.support.annotation.DrawableRes;

import com.mercadopago.paymentresult.components.Footer;

/**
 * Created by vaserber on 2/13/17.
 */

public class PaymentResult {

    public static final String SELECT_OTHER_PAYMENT_METHOD = "select_other_payment_method";
    public static final String RECOVER_PAYMENT = "recover_payment";

    private PaymentData paymentData;
    private Long paymentId;
    private String paymentStatus;
    private String paymentStatusDetail;
    private String payerEmail;
    private String statementDescription;
    public final String headerTitle;
    public final @DrawableRes int headerIcon;
    public final Footer.FooterAction footerButtonAction;
    public final Footer.FooterAction footerlinkAction;

    private PaymentResult(Builder builder) {
        this.paymentData = builder.paymentData;
        this.paymentId = builder.paymentId;
        this.paymentStatus = builder.paymentStatus;
        this.paymentStatusDetail = builder.paymentStatusDetail;
        this.payerEmail = builder.payerEmail;
        this.statementDescription = builder.statementDescription;
        this.headerTitle = builder.headerTitle;
        this.headerIcon = builder.headerIcon;
        this.footerButtonAction = builder.footerButtonAction;
        this.footerlinkAction = builder.footerBlinkAction;
    }

    public PaymentData getPaymentData() {
        return paymentData;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public boolean isStatusApproved() {
        return Payment.StatusCodes.STATUS_APPROVED.equals(paymentStatus);
    }

    public boolean isStatusRejected() {
        return Payment.StatusCodes.STATUS_REJECTED.equals(paymentStatus);
    }

    public boolean isStatusPending() {
        return Payment.StatusCodes.STATUS_PENDING.equals(paymentStatus);
    }

    public boolean isStatusInProcess() {
        return Payment.StatusCodes.STATUS_IN_PROCESS.equals(paymentStatus);
    }

    public String getPaymentStatusDetail() {
        return paymentStatusDetail;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public String getStatementDescription() {
        return statementDescription;
    }

    public boolean hasDiscount() {
        return paymentData != null && paymentData.getDiscount() != null;
    }

    public static class Builder{

        private PaymentData paymentData;
        private Long paymentId;
        private String paymentStatus;
        private String paymentStatusDetail;
        private String payerEmail;
        private String statementDescription;
        private  String headerTitle;
        private @DrawableRes int headerIcon = 0;
        private Footer.FooterAction footerButtonAction;
        private Footer.FooterAction footerBlinkAction;

        public Builder setPaymentData(PaymentData paymentData) {
            this.paymentData = paymentData;
            return this;
        }

        public Builder setPaymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder setPaymentStatusDetail(String statusDetail) {
            this.paymentStatusDetail = statusDetail;
            return this;
        }

        public Builder setPayerEmail(String payerEmail) {
            this.payerEmail = payerEmail;
            return this;
        }

        public Builder setStatementDescription(String statementDescription) {
            this.statementDescription = statementDescription;
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

        public Builder setFooterlinkAction(final Footer.FooterAction footerBlinkAction) {
            this.footerBlinkAction = footerBlinkAction;
            return this;
        }

        public PaymentResult build() {
            return new PaymentResult(this);
        }
    }
}
