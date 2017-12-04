package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;
import com.mercadopago.model.Currency;
import com.mercadopago.paymentresult.props.TotalAmountProps;
import com.mercadopago.util.CurrenciesUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by mromar on 11/28/17.
 */

public class TotalAmountComponent extends Component<TotalAmountProps> {

    public TotalAmountComponent(@NonNull final TotalAmountProps props,
                                @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }

    public String getAmountTitle() {
        String amountTitle;

        if (hasPayerCost() && props.payerCost.getInstallments() > 1) {
            amountTitle = props.payerCost.getInstallments() + "x " + props.amountFormatter.formatNumber(props.payerCost.getInstallmentAmount(), props.amountFormatter.getCurrencyId());
        } else {
            amountTitle = props.amountFormatter.formatNumber(props.amountFormatter.getAmount(), props.amountFormatter.getCurrencyId());
        }

        return amountTitle;
    }

    public String getAmountDetail() {
        String amountDetail = "";

        if (hasPayerCost() && props.payerCost.getInstallments() > 1) {
            amountDetail = "(" + props.amountFormatter.formatNumber(props.payerCost.getTotalAmount(), props.amountFormatter.getCurrencyId()) + ")";
        }

        return amountDetail;
    }

    private boolean hasPayerCost() {
        return props.payerCost != null;
    }

}
