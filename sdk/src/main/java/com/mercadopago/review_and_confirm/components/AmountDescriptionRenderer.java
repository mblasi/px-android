package com.mercadopago.review_and_confirm.components;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.customviews.MPTextView;
import com.mercadopago.util.CurrenciesUtil;

import java.math.BigDecimal;

/**
 * Created by mromar on 2/28/18.
 */

public class AmountDescriptionRenderer extends Renderer<AmountDescription> {

    @Override
    public View render(AmountDescription component, Context context) {
        final View bodyView = LayoutInflater.from(context).inflate(R.layout.mpsdk_amount_description_component, null, false);
        final MPTextView descriptionTextView = bodyView.findViewById(R.id.mpsdkDescription);
        final MPTextView amountTextView = bodyView.findViewById(R.id.mpsdkAmount);

        setText(descriptionTextView, component.props.description);
        setText(amountTextView, getFormattedAmount(component.props.amount, component.props.currencyId));

        descriptionTextView.setTextColor(component.props.textColor);
        amountTextView.setTextColor(component.props.textColor);

        return bodyView;
    }

    private Spanned getFormattedAmount(BigDecimal amount, String currencyId) {
        return CurrenciesUtil.getFormattedAmount(amount, currencyId);
    }
}
