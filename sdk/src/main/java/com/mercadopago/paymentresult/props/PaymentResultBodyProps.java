package com.mercadopago.paymentresult.props;

import android.support.annotation.NonNull;

/**
 * Created by vaserber on 10/23/17.
 */

public class PaymentResultBodyProps {

    public final String status;

    public PaymentResultBodyProps(String status) {
        this.status = status;
    }

    public PaymentResultBodyProps(@NonNull final Builder builder) {
        this.status = builder.status;
    }

    public Builder toBuilder() {
        return new Builder()
                .setStatus(this.status);
    }

    public class Builder {

        public String status;

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public PaymentResultBodyProps build() {
            return new PaymentResultBodyProps(this);
        }
    }
}
