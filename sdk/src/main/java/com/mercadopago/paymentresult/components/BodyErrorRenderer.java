package com.mercadopago.paymentresult.components;

import android.view.LayoutInflater;
import android.view.View;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.customviews.MPTextView;

/**
 * Created by vaserber on 27/11/2017.
 */

public class BodyErrorRenderer extends Renderer<BodyError> {

    @Override
    public View render() {
        final View bodyErrorView = LayoutInflater.from(context).inflate(R.layout.mpsdk_payment_result_body_error, null, false);
        final MPTextView titleTextView = bodyErrorView.findViewById(R.id.paymentResultBodyErrorTitle);
        final MPTextView descriptionTextView = bodyErrorView.findViewById(R.id.paymentResultBodyErrorDescription);
        final MPTextView actionTextView = bodyErrorView.findViewById(R.id.paymentResultBodyErrorAction);
        final View middleDivider = bodyErrorView.findViewById(R.id.bodyErrorMiddleDivider);
        final MPTextView secondaryTitleTextView = bodyErrorView.findViewById(R.id.bodyErrorSecondaryTitle);
        final View bottomDivider = bodyErrorView.findViewById(R.id.bodyErrorBottomDivider);

        setText(titleTextView, component.getTitle());
        setText(descriptionTextView, component.getDescription());

        if (component.hasActionForCallForAuth()) {
            actionTextView.setText(component.getActionText());
            actionTextView.setVisibility(View.VISIBLE);
            middleDivider.setVisibility(View.VISIBLE);
            secondaryTitleTextView.setText(component.getSecondaryTitleForCallForAuth());
            secondaryTitleTextView.setVisibility(View.VISIBLE);
            bottomDivider.setVisibility(View.VISIBLE);
            actionTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    component.recoverPayment();
                }
            });
        } else {
            actionTextView.setVisibility(View.GONE);
            middleDivider.setVisibility(View.GONE);
            secondaryTitleTextView.setVisibility(View.GONE);
            bottomDivider.setVisibility(View.GONE);
        }

        return bodyErrorView;
    }
}
