package com.mercadopago.review_and_confirm.components.payment_method;

import com.mercadopago.constants.PaymentTypes;
import com.mercadopago.model.Discount;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Site;
import com.mercadopago.review_and_confirm.SummaryProviderImpl;
import com.mercadopago.review_and_confirm.components.FullSummary;
import com.mercadopago.review_and_confirm.models.SummaryModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

/**
 * Created by mromar on 3/12/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class FullSummaryTest {

    private final static String CURRENCY_ID = "ARS";
    private final static Long DISCOUNT_ID = 77L;
    private final static BigDecimal DISCOUNT_COUPON_AMOUNT = new BigDecimal(20);
    private final static BigDecimal DISCOUNT_PERCENT_OFF = new BigDecimal(20);

    private final static BigDecimal TOTAL_AMOUNT = new BigDecimal(1000);
    private final static Site SITE = new Site("MLA", CURRENCY_ID);

    private final static String PAYMENT_TYPE_ID_CARD = PaymentTypes.CREDIT_CARD;

    @Mock
    SummaryProviderImpl provider;

    @Test
    public void whenHasDiscountAndIsCardPaymentMethodThenGetTotalAmountWithDiscount() throws Exception {
        SummaryModel model = new SummaryModel(TOTAL_AMOUNT, getCardPaymentMethod(), SITE, getPayerCostWithDiscount(), getDiscount(), null);

        FullSummary component = new FullSummary(model, provider);

        Assert.assertEquals(component.getTotalAmount(), getTotalAmountWithDiscount());
    }

    @Test
    public void whenHasInstallmentsThenGetPayerCostTotalAmount() throws Exception {
        SummaryModel model = new SummaryModel(TOTAL_AMOUNT, getCardPaymentMethod(), SITE, getPayerCostWithInstallments(), getDiscount(), null);

        FullSummary component = new FullSummary(model, provider);

        Assert.assertEquals(component.getTotalAmount(), TOTAL_AMOUNT);
    }

    private PaymentMethod getCardPaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentTypeId(PAYMENT_TYPE_ID_CARD);

        return paymentMethod;
    }

    private PayerCost getPayerCostWithDiscount() {
        PayerCost payerCost = new PayerCost();

        payerCost.setTotalAmount(getTotalAmountWithDiscount());
        payerCost.setInstallments(1);
        payerCost.setInstallmentRate(new BigDecimal(0));

        return payerCost;
    }

    private PayerCost getPayerCostWithInstallments() {
        PayerCost payerCost = new PayerCost();

        payerCost.setTotalAmount(TOTAL_AMOUNT);
        payerCost.setInstallments(3);
        payerCost.setInstallmentRate(new BigDecimal(0));

        return payerCost;
    }

    private Discount getDiscount() {
        Discount discount = new Discount();

        discount.setId(DISCOUNT_ID);
        discount.setCouponAmount(DISCOUNT_COUPON_AMOUNT);
        discount.setCurrencyId(CURRENCY_ID);
        discount.setPercentOff(DISCOUNT_PERCENT_OFF);

        return discount;
    }

    private BigDecimal getTotalAmountWithDiscount() {
        return TOTAL_AMOUNT.subtract(getDiscount().getCouponAmount());
    }
}
