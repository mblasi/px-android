package com.mercadopago.review_and_confirm.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.Component;
import com.mercadopago.model.SummaryDetail;
import com.mercadopago.review_and_confirm.props.AmountDescriptionProps;
import com.mercadopago.review_and_confirm.props.SummaryProps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mromar on 2/28/18.
 */

public class Summary extends Component<SummaryProps, Void> {

    public static final String CFT = "CFT ";

    public Summary(@NonNull SummaryProps props) {
        super(props);
    }

    public BigDecimal getSubtotalAmount() {
        BigDecimal subtotal = null;

        if (hasDiscount()) {
            subtotal = getSubtotal();
        }

        return subtotal;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal totalAmount = null;

        if (isCardPaymentMethod()) {
            if (props.payerCost.getInstallments() == 1) {
                if (props.discount != null && !isEmptySummaryDetails()) {
                    totalAmount = props.payerCost.getTotalAmount();
                }
            } else {
                totalAmount = props.payerCost.getTotalAmount();
            }
        } else if (hasDiscount() && !isEmptySummaryDetails()) {
            totalAmount = getSubtotal();
        }

        return totalAmount;
    }

    public boolean hasToRenderPayerCost() {
        return isCardPaymentMethod() && props.payerCost.getInstallments() > 1;
    }

    public StringBuilder getFinance() {
        StringBuilder stringBuilder = new StringBuilder();

        if (props.payerCost.hasCFT()) {
            stringBuilder.append(CFT);
            stringBuilder.append(props.payerCost.getCFTPercent());
        }

        return stringBuilder;
    }

    public List<AmountDescription> getAmountDescriptionComponents() {
        List<AmountDescription> amountDescriptionList = new ArrayList<>();

        for (SummaryDetail summaryDetail : getSummary().getSummaryDetails()) {
            final AmountDescriptionProps amountDescriptionProps = new AmountDescriptionProps(
                    summaryDetail.getTotalAmount(),
                    summaryDetail.getTitle(),
                    props.currencyId,
                    summaryDetail.getTextColor());

            amountDescriptionList.add(new AmountDescription(amountDescriptionProps));
        }

        return amountDescriptionList;
    }

    private com.mercadopago.model.Summary getSummary() {
        com.mercadopago.model.Summary.Builder summaryBuilder = new com.mercadopago.model.Summary.Builder();

        if (reviewScreenPreference != null && isValidTotalAmount() && hasProductAmount()) {
            summaryBuilder.addSummaryProductDetail(reviewScreenPreference.getProductAmount(), getSummaryProductsTitle(), getDefaultTextColor())
                    .addSummaryShippingDetail(reviewScreenPreference.getShippingAmount(), getSummaryShippingTitle(), getDefaultTextColor())
                    .addSummaryArrearsDetail(reviewScreenPreference.getArrearsAmount(), getSummaryArrearTitle(), getDefaultTextColor())
                    .addSummaryTaxesDetail(reviewScreenPreference.getTaxesAmount(), getSummaryTaxesTitle(), getDefaultTextColor())
                    .addSummaryDiscountDetail(getDiscountAmount(), getSummaryDiscountsTitle(), getDiscountTextColor())
                    .setDisclaimerText(reviewScreenPreference.getDisclaimerText())
                    .setDisclaimerColor(getDisclaimerTextColor());

            if (getChargesAmount().compareTo(BigDecimal.ZERO) > 0) {
                summaryBuilder.addSummaryChargeDetail(getChargesAmount(), getSummaryChargesTitle(), getDefaultTextColor());
            }

        } else {
            summaryBuilder.addSummaryProductDetail(amount, getSummaryProductsTitle(), getDefaultTextColor());

            if (payerCost != null && getPayerCostChargesAmount().compareTo(BigDecimal.ZERO) > 0) {
                summaryBuilder.addSummaryChargeDetail(getPayerCostChargesAmount(), getSummaryChargesTitle(), getDefaultTextColor());
            }

            if (reviewScreenPreference != null && !isEmpty(reviewScreenPreference.getDisclaimerText())) {
                summaryBuilder.setDisclaimerText(reviewScreenPreference.getDisclaimerText())
                        .setDisclaimerColor(getDisclaimerTextColor());
            }

            if (discount != null) {
                summaryBuilder.addSummaryDiscountDetail(discount.getCouponAmount(), getSummaryDiscountsTitle(), getDiscountTextColor());
            }
        }

        return summaryBuilder.build();
    }

    private boolean isValidTotalAmount() {
        BigDecimal totalAmountPreference = reviewScreenPreference.getTotalAmount();
        return totalAmountPreference.compareTo(amount) == 0;
    }

    private boolean hasProductAmount() {
        return reviewScreenPreference.hasProductAmount();
    }

    private boolean isEmptySummaryDetails() {
        return props.summary != null && props.summary.getSummaryDetails() != null && props.summary.getSummaryDetails().size() < 2;
    }

    private BigDecimal getSubtotal() {
        BigDecimal ans = props.amount;
        if (hasDiscount()) {
            ans = props.amount.subtract(props.discount.getCouponAmount());
        }
        return ans;
    }

    private boolean hasDiscount() {
        return (props.discount != null && props.currencyId != null && (props.discount.hasPercentOff() != null || props.discount.getCouponAmount() != null));
    }

    private boolean isCardPaymentMethod() {
        return props.paymentMethod != null && isCard(props.paymentMethod.getPaymentTypeId());
    }

    private boolean isCard(String paymentTypeId) {
        boolean isCard = false;

        if ((paymentTypeId != null) && (paymentTypeId.equals("credit_card") ||
                paymentTypeId.equals("debit_card") || paymentTypeId.equals("prepaid_card"))) {
            isCard = true;
        }

        return isCard;
    }
}
