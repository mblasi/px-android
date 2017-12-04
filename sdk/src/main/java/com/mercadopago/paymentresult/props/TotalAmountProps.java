package com.mercadopago.paymentresult.props;

import android.support.annotation.NonNull;

import com.mercadopago.model.PayerCost;
import com.mercadopago.paymentresult.components.Body;
import com.mercadopago.paymentresult.formatter.BodyAmountFormatter;
import com.mercadopago.paymentresult.formatter.HeaderTitleFormatter;

/**
 * Created by mromar on 11/28/17.
 */

public class TotalAmountProps {

    public final BodyAmountFormatter amountFormatter;
    public final PayerCost payerCost;

    public TotalAmountProps(BodyAmountFormatter amountFormatter, PayerCost payerCost) {
        this.amountFormatter = amountFormatter;
        this.payerCost = payerCost;
    }

    public TotalAmountProps(@NonNull final Builder builder) {
        this.amountFormatter = builder.amountFormatter;
        this.payerCost = builder.payerCost;
    }

    public Builder toBuilder() {
        return new Builder()
                .setAmountFormatter(this.amountFormatter)
                .setPayerCost(this.payerCost);
    }

    public static class Builder {

        public BodyAmountFormatter amountFormatter;
        public PayerCost payerCost;

        public Builder setAmountFormatter(BodyAmountFormatter amountFormatter) {
            this.amountFormatter = amountFormatter;
            return this;
        }

        public Builder setPayerCost(PayerCost payerCost) {
            this.payerCost = payerCost;
            return this;
        }

        public TotalAmountProps build() {
            return new TotalAmountProps(this);
        }
    }
}
