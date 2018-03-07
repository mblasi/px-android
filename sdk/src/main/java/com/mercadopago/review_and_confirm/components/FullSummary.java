package com.mercadopago.review_and_confirm.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.Component;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.core.CheckoutStore;
import com.mercadopago.model.SummaryDetail;
import com.mercadopago.preferences.ReviewScreenPreference;
import com.mercadopago.review_and_confirm.FullSummaryProvider;
import com.mercadopago.review_and_confirm.models.SummaryModel;
import com.mercadopago.review_and_confirm.props.AmountDescriptionProps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mercadopago.util.TextUtils.isEmpty;

/**
 * Created by mromar on 2/28/18.
 */

public class FullSummary extends Component<SummaryModel, Void> {

    private FullSummaryProvider provider;

    public static final String CFT = "CFT ";

    static {
        RendererFactory.register(FullSummary.class, FullSummaryRenderer.class);
    }

    public FullSummary(@NonNull final SummaryModel props,
                       @NonNull final FullSummaryProvider provider) {
        super(props);
        this.provider = provider;
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
            if (props.getInstallments() == 1) {
                if (props.getCouponAmount() != null && !isEmptySummaryDetails()) {
                    totalAmount = props.getPayerCostTotalAmount();
                }
            } else {
                totalAmount = props.getPayerCostTotalAmount();
            }
        } else if (hasDiscount() && !isEmptySummaryDetails()) {
            totalAmount = getSubtotal();
        }

        return totalAmount;
    }

    public boolean hasToRenderPayerCost() {
        return isCardPaymentMethod() && props.getInstallments() > 1;
    }

    public String getFinance() {
        StringBuilder stringBuilder = new StringBuilder();

        if (!isEmpty(props.cftPercent)) {
            stringBuilder.append(CFT);
            stringBuilder.append(props.cftPercent);
        }

        return stringBuilder.toString();
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

    public com.mercadopago.model.Summary getSummary() {
        ReviewScreenPreference reviewScreenPreference = CheckoutStore.getInstance().getReviewScreenPreference();
        com.mercadopago.model.Summary.Builder summaryBuilder = new com.mercadopago.model.Summary.Builder();

        if (reviewScreenPreference != null && isValidTotalAmount() && hasProductAmount()) {
            summaryBuilder.addSummaryProductDetail(reviewScreenPreference.getProductAmount(), provider.getSummaryProductsTitle(), provider.getDefaultTextColor())
                    .addSummaryShippingDetail(reviewScreenPreference.getShippingAmount(), provider.getSummaryShippingTitle(), provider.getDefaultTextColor())
                    .addSummaryArrearsDetail(reviewScreenPreference.getArrearsAmount(), provider.getSummaryArrearTitle(), provider.getDefaultTextColor())
                    .addSummaryTaxesDetail(reviewScreenPreference.getTaxesAmount(), provider.getSummaryTaxesTitle(), provider.getDefaultTextColor())
                    .addSummaryDiscountDetail(getDiscountAmount(), provider.getSummaryDiscountsTitle(), provider.getDiscountTextColor())
                    .setDisclaimerText(reviewScreenPreference.getDisclaimerText())
                    .setDisclaimerColor(provider.getDisclaimerTextColor());

            if (getChargesAmount().compareTo(BigDecimal.ZERO) > 0) {
                summaryBuilder.addSummaryChargeDetail(getChargesAmount(), provider.getSummaryChargesTitle(), provider.getDefaultTextColor());
            }

        } else {
            summaryBuilder.addSummaryProductDetail(props.getAmount(), provider.getSummaryProductsTitle(), provider.getDefaultTextColor());

            if (isValidAmount(props.getPayerCostTotalAmount()) && getPayerCostChargesAmount().compareTo(BigDecimal.ZERO) > 0) {
                summaryBuilder.addSummaryChargeDetail(getPayerCostChargesAmount(), provider.getSummaryChargesTitle(), provider.getDefaultTextColor());
            }

            if (reviewScreenPreference != null && !isEmpty(reviewScreenPreference.getDisclaimerText())) {
                summaryBuilder.setDisclaimerText(reviewScreenPreference.getDisclaimerText())
                        .setDisclaimerColor(provider.getDisclaimerTextColor());
            }

            if (isValidAmount(props.getCouponAmount())) {
                summaryBuilder.addSummaryDiscountDetail(props.getCouponAmount(), provider.getSummaryDiscountsTitle(), provider.getDiscountTextColor());
            }
        }

        return summaryBuilder.build();
    }

    private BigDecimal getChargesAmount() {
        ReviewScreenPreference reviewScreenPreference = CheckoutStore.getInstance().getReviewScreenPreference();
        BigDecimal interestAmount = new BigDecimal(0);

        if (reviewScreenPreference.getChargeAmount() != null) {
            interestAmount = reviewScreenPreference.getChargeAmount();
        }

        if (props.getInstallments() != null && props.getInstallments() > 1 && isValidAmount(props.getPayerCostTotalAmount())) {
            BigDecimal totalInterestsAmount = getPayerCostChargesAmount();
            interestAmount = interestAmount.add(totalInterestsAmount);
        }

        return interestAmount;
    }

    private BigDecimal getPayerCostChargesAmount() {
        BigDecimal totalInterestsAmount;

        if (isValidAmount(props.getCouponAmount())) {
            BigDecimal totalAmount = props.getAmount().subtract(props.getCouponAmount());
            totalInterestsAmount = props.getPayerCostTotalAmount().subtract(totalAmount);
        } else {
            totalInterestsAmount = props.getPayerCostTotalAmount().subtract(props.getAmount());
        }

        return totalInterestsAmount;
    }

    private BigDecimal getDiscountAmount() {
        BigDecimal discountAmount = CheckoutStore.getInstance().getReviewScreenPreference().getDiscountAmount();

        if (isValidAmount(props.getCouponAmount())) {
            discountAmount = discountAmount.add(props.getCouponAmount());
        }

        return discountAmount;
    }

    private boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean isValidTotalAmount() {
        BigDecimal totalAmountPreference = CheckoutStore.getInstance().getReviewScreenPreference().getTotalAmount();
        return totalAmountPreference.compareTo(props.getAmount()) == 0;
    }

    private boolean hasProductAmount() {
        return CheckoutStore.getInstance().getReviewScreenPreference().hasProductAmount();
    }

    private boolean isEmptySummaryDetails() {
        return getSummary() != null && getSummary().getSummaryDetails() != null && getSummary().getSummaryDetails().size() < 2;
    }

    private BigDecimal getSubtotal() {
        BigDecimal ans = props.getAmount();
        if (hasDiscount()) {
            ans = props.getAmount().subtract(props.getCouponAmount());
        }
        return ans;
    }

    private boolean hasDiscount() {
        return props.currencyId != null && props.getCouponAmount() != null;
    }

    private boolean isCardPaymentMethod() {
        return props.paymentTypeId != null && isCard(props.paymentTypeId);
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
