package com.mercadopago.paymentresult.props;

import android.support.annotation.NonNull;

import com.mercadopago.model.Instruction;
import com.mercadopago.model.PaymentData;
import com.mercadopago.paymentresult.formatter.BodyAmountFormatter;
import com.mercadopago.paymentresult.formatter.HeaderTitleFormatter;

/**
 * Created by vaserber on 10/23/17.
 */

public class PaymentResultBodyProps {

    public final String status;
    public final String statusDetail;
    public final Instruction instruction;
    public final PaymentData paymentData;
    public final String processingMode;
    public final String disclaimer;
    public final BodyAmountFormatter bodyAmountFormatter;

    public PaymentResultBodyProps(String status, String statusDetail,
                                  Instruction instruction, PaymentData paymentData, String disclaimer,
                                  String processingMode, BodyAmountFormatter bodyAmountFormatter) {
        this.status = status;
        this.statusDetail = statusDetail;
        this.instruction = instruction;
        this.paymentData = paymentData;
        this.disclaimer = disclaimer;
        this.processingMode = processingMode;
        this.bodyAmountFormatter = bodyAmountFormatter;
    }

    public PaymentResultBodyProps(@NonNull final Builder builder) {
        this.status = builder.status;
        this.statusDetail = builder.statusDetail;
        this.instruction = builder.instruction;
        this.paymentData = builder.paymentData;
        this.disclaimer = builder.disclaimer;
        this.processingMode = builder.processingMode;
        this.bodyAmountFormatter = builder.bodyAmountFormatter;
    }

    public Builder toBuilder() {
        return new Builder()
                .setStatus(this.status)
                .setStatusDetail(this.statusDetail)
                .setInstruction(this.instruction)
                .setPaymentData(this.paymentData)
                .setDisclaimer(this.disclaimer)
                .setProcessingMode(this.processingMode)
                .setBodyAmountFormatter(this.bodyAmountFormatter);
    }

    public static class Builder {

        public String status;
        public String statusDetail;
        public Instruction instruction;
        public PaymentData paymentData;
        public String disclaimer;
        public String processingMode;
        public BodyAmountFormatter bodyAmountFormatter;

        public Builder setStatus(@NonNull final String status) {
            this.status = status;
            return this;
        }

        public Builder setStatusDetail(@NonNull final String statusDetail) {
            this.statusDetail = statusDetail;
            return this;
        }

        public Builder setInstruction(final Instruction instruction) {
            this.instruction = instruction;
            return this;
        }

        public Builder setPaymentData(final PaymentData paymentData) {
            this.paymentData = paymentData;
            return this;
        }

        public Builder setDisclaimer(String disclaimer) {
            this.disclaimer = disclaimer;
            return this;
        }

        public Builder setProcessingMode(final String processingMode) {
            this.processingMode = processingMode;
            return this;
        }

        public Builder setBodyAmountFormatter(BodyAmountFormatter bodyAmountFormatter) {
            this.bodyAmountFormatter = bodyAmountFormatter;
            return this;
        }

        public PaymentResultBodyProps build() {
            return new PaymentResultBodyProps(this);
        }
    }
}
