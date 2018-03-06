package com.mercadopago.review_and_confirm.props;

import android.support.annotation.NonNull;

import com.mercadopago.review_and_confirm.models.SummaryModel;

import java.math.BigDecimal;

/**
 * Created by mromar on 3/1/18.
 */

public class SummaryProps {

    public final BigDecimal amount;
    public final String currencyId;
    public final String siteId;
    public final String paymentTypeId;
    public final BigDecimal payerCostTotalAmount;
    public final Integer installments;
    public final String cftPercent;
    public final BigDecimal couponAmount;
    public final boolean hasPercentOff;
    public final BigDecimal installmentsRate;
    public final BigDecimal installmentAmount;

    public SummaryProps(@NonNull final SummaryModel summaryModel) {
        this.amount = summaryModel.getAmount();
        this.currencyId = summaryModel.currencyId;
        this.siteId = summaryModel.siteId;
        this.paymentTypeId = summaryModel.paymentTypeId;
        this.payerCostTotalAmount = summaryModel.getPayerCostTotalAmount();
        this.installments = summaryModel.getInstallments();
        this.cftPercent = summaryModel.cftPercent;
        this.couponAmount = summaryModel.getCouponAmount();
        this.hasPercentOff = summaryModel.hasPercentOff;
        this.installmentsRate = summaryModel.getInstallmentsRate();
        this.installmentAmount = summaryModel.getInstallmentAmount();
    }
}
