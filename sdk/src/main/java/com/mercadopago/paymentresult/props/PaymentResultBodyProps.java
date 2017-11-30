package com.mercadopago.paymentresult.props;

import android.support.annotation.NonNull;

import com.mercadopago.model.Instruction;
import com.mercadopago.model.PaymentData;

/**
 * Created by vaserber on 10/23/17.
 */

public class PaymentResultBodyProps {

    public final String status;
    public final String statusDetail;
    public final Instruction instruction;
    public final PaymentData paymentData;
    public final String processingMode;

    public PaymentResultBodyProps(final String status, final String statusDetail, final Instruction instruction,
                                  final PaymentData paymentData, final String processingMode) {
        this.status = status;
        this.statusDetail = statusDetail;
        this.instruction = instruction;
        this.paymentData = paymentData;
        this.processingMode = processingMode;
    }

    public PaymentResultBodyProps(@NonNull final Builder builder) {
        this.status = builder.status;
        this.statusDetail = builder.statusDetail;
        this.instruction = builder.instruction;
        this.paymentData = builder.paymentData;
        this.processingMode = builder.processingMode;
    }

    public Builder toBuilder() {
        return new Builder()
                .setStatus(this.status)
                .setStatusDetail(this.statusDetail)
                .setInstruction(this.instruction)
                .setPaymentData(this.paymentData)
                .setProcessingMode(this.processingMode);
    }

    public static class Builder {

        public String status;
        public String statusDetail;
        public Instruction instruction;
        public PaymentData paymentData;
        public String processingMode;

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

        public Builder setProcessingMode(final String processingMode) {
            this.processingMode = processingMode;
            return this;
        }

        public Builder setPaymentData(final PaymentData paymentData) {
            this.paymentData = paymentData;
            return this;
        }

        public PaymentResultBodyProps build() {
            return new PaymentResultBodyProps(this);
        }
    }
}
