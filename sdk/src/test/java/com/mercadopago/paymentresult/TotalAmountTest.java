package com.mercadopago.paymentresult;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.mocks.Issuers;
import com.mercadopago.mocks.PayerCosts;
import com.mercadopago.mocks.PaymentMethods;
import com.mercadopago.mocks.Tokens;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mercadopago.paymentresult.formatter.BodyAmountFormatter;
import com.mercadopago.paymentresult.props.TotalAmountProps;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;

/**
 * Created by mromar on 11/30/2017.
 */

public class TotalAmountTest {

    private ActionDispatcher dispatcher;

    @Before
    public void setup() {
        dispatcher = mock(ActionDispatcher.class);
    }

    @Test
    public void getAmountTitleWhenComponentHasPayerCostWithInstallments() {
        String amountTitle;
        final PayerCost payerCost = PayerCosts.getPayerCost();
        final BodyAmountFormatter amountFormatter = new BodyAmountFormatter("ARS", new BigDecimal(1000));
        final com.mercadopago.paymentresult.components.TotalAmountComponent component = getTotalAmountComponent(payerCost, amountFormatter);

        amountTitle = component.props.payerCost.getInstallments() + "x " + component.props.amountFormatter.formatNumber(component.props.payerCost.getInstallmentAmount(), component.props.amountFormatter.getCurrencyId());
        Assert.assertTrue(component.getAmountTitle().equals(amountTitle));
    }

    @Test
    public void getAmountTitleWhenComponentHasPayerCostWithoutInstallments() {
        String amountTitle;
        final PayerCost payerCost = PayerCosts.getPayerCostWithoutInstallments();
        final BodyAmountFormatter amountFormatter = new BodyAmountFormatter("ARS", new BigDecimal(1000));
        final com.mercadopago.paymentresult.components.TotalAmountComponent component = getTotalAmountComponent(payerCost, amountFormatter);

        amountTitle = component.props.amountFormatter.formatNumber(component.props.amountFormatter.getAmount(), component.props.amountFormatter.getCurrencyId());
        Assert.assertTrue(component.getAmountTitle().equals(amountTitle));
    }

    @Test
    public void getEmptyAmountTitleWhenComponentHasNotPayerCost() {
        final PayerCost payerCost = null;
        final BodyAmountFormatter amountFormatter = new BodyAmountFormatter("ARS", new BigDecimal(1000));
        final com.mercadopago.paymentresult.components.TotalAmountComponent component = getTotalAmountComponent(payerCost, amountFormatter);

        Assert.assertTrue(component.getAmountDetail().equals(""));
    }

    @Test
    public void getAmountDetailWhenComponentHasPayerCost() {
        String amountDetail;
        final PayerCost payerCost = PayerCosts.getPayerCost();
        final BodyAmountFormatter amountFormatter = new BodyAmountFormatter("ARS", new BigDecimal(1000));

        final com.mercadopago.paymentresult.components.TotalAmountComponent component =
                getTotalAmountComponent(payerCost, amountFormatter);

        amountDetail = "(" + component.props.amountFormatter.formatNumber(component.props.payerCost.getTotalAmount(), component.props.amountFormatter.getCurrencyId()) + ")";

        Assert.assertTrue(component.getAmountDetail().equals(amountDetail));
    }

    @Test
    public void getEmptyAmountDetailWhenComponentHasNotPayerCost() {
        final PayerCost payerCost = null;
        final BodyAmountFormatter amountFormatter = new BodyAmountFormatter("ARS", new BigDecimal(1000));

        final com.mercadopago.paymentresult.components.TotalAmountComponent component =
                getTotalAmountComponent(payerCost, amountFormatter);

        Assert.assertTrue(component.getAmountDetail().equals(""));
    }

    private com.mercadopago.paymentresult.components.TotalAmountComponent getTotalAmountComponent(PayerCost payerCost, BodyAmountFormatter amountFormatter) {
        final TotalAmountProps props = new TotalAmountProps.Builder()
                .setPayerCost(payerCost)
                .setAmountFormatter(amountFormatter)
                .build();

        return new com.mercadopago.paymentresult.components.TotalAmountComponent(props, dispatcher);
    }
}
