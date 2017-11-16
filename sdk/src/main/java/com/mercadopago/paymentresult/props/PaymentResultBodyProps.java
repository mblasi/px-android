package com.mercadopago.paymentresult.props;

import android.support.annotation.NonNull;

import com.mercadopago.model.Instruction;

/**
 * Created by vaserber on 10/23/17.
 */

public class PaymentResultBodyProps {

    public final String status;
    public final String statusDetail;
    public final Instruction instruction;
    public final String processingMode;

    public PaymentResultBodyProps(String status, String statusDetail, Instruction instruction, String processingMode) {
        this.status = status;
        this.statusDetail = statusDetail;
        this.instruction = instruction;
        this.processingMode = processingMode;
    }

    public PaymentResultBodyProps(@NonNull final Builder builder) {
        this.status = builder.status;
        this.statusDetail = builder.statusDetail;
        this.instruction = builder.instruction;
        this.processingMode = builder.processingMode;
    }

    public Builder toBuilder() {
        return new Builder()
                .setStatus(this.status)
                .setStatusDetail(this.statusDetail)
                .setInstruction(this.instruction)
                .setProcessingMode(this.processingMode);
    }

    public class Builder {

        public String status;
        public String statusDetail;
        public Instruction instruction;
        public String processingMode;

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setStatusDetail(String statusDetail) {
            this.statusDetail = statusDetail;
            return this;
        }

        public Builder setInstruction(Instruction instruction) {
            this.instruction = instruction;
            return this;
        }

        public Builder setProcessingMode(String processingMode) {
            this.processingMode = processingMode;
            return this;
        }

        public PaymentResultBodyProps build() {
            return new PaymentResultBodyProps(this);
        }
    }
}
