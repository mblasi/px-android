package com.mercadopago.review_and_confirm.props;

import android.support.annotation.NonNull;

import com.mercadopago.model.Discount;
import com.mercadopago.model.Payer;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Site;
import com.mercadopago.model.Summary;

import java.math.BigDecimal;

/**
 * Created by mromar on 3/1/18.
 */

public class SummaryProps {

    public final BigDecimal amount;
    public final String currencyId;
    public final PaymentMethod paymentMethod;
    public final PayerCost payerCost;
    public final Discount discount;
    public final Summary summary;
    public final Site site;

    public SummaryProps(@NonNull final BigDecimal amount,
                        @NonNull final String currencyId,
                        @NonNull final PaymentMethod paymentMethod,
                        @NonNull final Site site,
                        final PayerCost payerCost,
                        final Summary summary,
                        @NonNull final Discount discount) {

        this.amount = amount;
        this.currencyId = currencyId;
        this.paymentMethod = paymentMethod;
        this.payerCost = payerCost;
        this.site = site;
        this.summary = summary;
        this.discount = discount;
    }

    public SummaryProps(@NonNull final Builder builder) {
        this.amount = builder.amount;
        this.currencyId = builder.currencyId;
        this.paymentMethod = builder.paymentMethod;
        this.payerCost = builder.payerCost;
        this.site = builder.site;
        this.summary = builder.summary;
        this.discount = builder.discount;
    }

    public static class Builder {

        public BigDecimal amount;
        public String currencyId;
        public PaymentMethod paymentMethod;
        public PayerCost payerCost;
        public Site site;
        public Summary summary;
        public Discount discount;

        public Builder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder setCurrencyId(String currencyId) {
            this.currencyId = currencyId;
            return this;
        }

        public Builder setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder setPayerCost(PayerCost payerCost) {
            this.payerCost = payerCost;
            return this;
        }

        public Builder setSite(Site site) {
            this.site = site;
            return this;
        }

        public Builder setSummary(Summary summary) {
            this.summary = summary;
            return this;
        }

        public Builder setDiscount(Discount discount) {
            this.discount = discount;
            return this;
        }

        public SummaryProps build() {
            return new SummaryProps(this);
        }
    }
}
