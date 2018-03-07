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

    public final String amount;
    public final String currencyId;
    public final String siteId;
    public final String paymentTypeId;
    public final String payerCostTotalAmount;
    public final String installments;
    public final String cftPercent;
    public final String couponAmount;
    public final boolean hasPercentOff;
    public final String installmentsRate;
    public final String installmentAmount;

    public SummaryModel(BigDecimal amount,
                        PaymentMethod paymentMethod,
                        Site site,
                        PayerCost payerCost,
                        Discount discount) {

        this.amount = amount.toString();
        this.currencyId = site.getCurrencyId();
        this.siteId = site.getId();
        this.paymentTypeId = paymentMethod.getPaymentTypeId();
        this.payerCostTotalAmount = payerCost.getTotalAmount().toString();
        this.installments = payerCost.getInstallments().toString();
        this.cftPercent = payerCost.getCFTPercent();
        this.couponAmount = discount != null ? discount.getCouponAmount().toString() : null;
        this.hasPercentOff = discount != null ? discount.hasPercentOff() : false;
        this.installmentsRate = payerCost.getInstallmentRate().toString();
        this.installmentAmount = payerCost.getInstallmentAmount().toString();
    }

    protected SummaryModel(Parcel in) {
        amount = in.readString();
        currencyId = in.readString();
        siteId = in.readString();
        paymentTypeId = in.readString();
        payerCostTotalAmount = in.readString();
        installments = in.readString();
        cftPercent = in.readString();
        couponAmount = in.readString();
        hasPercentOff = in.readByte() != 0;
        installmentsRate = in.readString();
        installmentAmount = in.readString();
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
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(amount);
        dest.writeString(currencyId);
        dest.writeString(siteId);
        dest.writeString(paymentTypeId);
        dest.writeString(payerCostTotalAmount);
        dest.writeString(installments);
        dest.writeString(cftPercent);
        dest.writeString(couponAmount);
        dest.writeByte((byte) (hasPercentOff ? 1 : 0));
        dest.writeString(installmentsRate);
        dest.writeString(installmentAmount);
    }
}
