package com.mercadopago.review_and_confirm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mercadopago.model.Discount;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Site;
import com.mercadopago.model.Summary;

import java.math.BigDecimal;

/**
 * Created by mromar on 3/2/18.
 */

public class SummaryModel implements Parcelable {

    public final BigDecimal amount;
    public final String currencyId;
    public final PaymentMethod paymentMethod;
    public final Site site;
    public final PayerCost payerCost;
    public final Summary summary;
    public final Discount discount;

    public SummaryModel(BigDecimal amount,
                        String currencyId,
                        PaymentMethod paymentMethod,
                        Site site,
                        PayerCost payerCost,
                        Summary summary,
                        Discount discount) {

        this.amount = amount;
        this.currencyId = currencyId;
        this.paymentMethod = paymentMethod;
        this.site = site;
        this.payerCost = payerCost;
        this.summary = summary;
        this.discount = discount;
    }

    @Override
    public int describeContents() {
        //TODO
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //TODO
    }
}
