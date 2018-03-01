package com.mercadopago.review_and_confirm.props;

import android.support.annotation.NonNull;

import com.mercadopago.model.Discount;
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
}
