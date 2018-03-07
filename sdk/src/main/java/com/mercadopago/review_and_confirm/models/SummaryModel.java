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

    private final String amount;
    public final String currencyId;
    public final String siteId;
    public final String paymentTypeId;
    private final String payerCostTotalAmount;
    private final String installments;
    public final String cftPercent;
    private final String couponAmount;
    public final boolean hasPercentOff;
    private final String installmentsRate;
    private final String installmentAmount;
    public final String itemTitle;

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
        //TODO
        this.itemTitle = "Lala";
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
        itemTitle = in.readString();
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
        dest.writeString(itemTitle);
    }

    public BigDecimal getTotalAmount() {
        return new BigDecimal(this.amount);
    }

    public BigDecimal getPayerCostTotalAmount() {
        return this.payerCostTotalAmount != null ? new BigDecimal(this.payerCostTotalAmount) : null;
    }

    public BigDecimal getCouponAmount() {
        return this.couponAmount != null ? new BigDecimal(this.couponAmount) : null;
    }

    public BigDecimal getInstallmentsRate() {
        return this.installmentsRate != null ? new BigDecimal(this.installmentsRate) : null;
    }

    public BigDecimal getInstallmentAmount() {
        return this.installmentAmount != null ? new BigDecimal(this.installmentAmount) : null;
    }

    public Integer getInstallments() {
        return this.installments != null ? Integer.valueOf(this.installments) : null;
    }
}

