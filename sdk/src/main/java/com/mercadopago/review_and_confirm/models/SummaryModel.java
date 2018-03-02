package com.mercadopago.review_and_confirm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mercadopago.model.Discount;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Site;

import java.math.BigDecimal;

/**
 * Created by mromar on 3/2/18.
 */

public class SummaryModel implements Parcelable {

    public final BigDecimal amount;
    public final PaymentMethod paymentMethod;
    public final Site site;
    public final PayerCost payerCost;
    public final Discount discount;

    public SummaryModel(BigDecimal amount,
                        PaymentMethod paymentMethod,
                        Site site,
                        PayerCost payerCost,
                        Discount discount) {

        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.site = site;
        this.payerCost = payerCost;
        this.discount = discount;
    }

    protected SummaryModel(Parcel in) {
        currencyId = in.readString();
    }

    public static final Creator<SummaryModel> CREATOR = new Creator<SummaryModel>() {
        @Override
        public SummaryModel createFromParcel(Parcel in) {
            return new SummaryModel(in);
        }

        @Override
        public SummaryModel[] newArray(int size) {
            return new SummaryModel[size];
        }
    };

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
