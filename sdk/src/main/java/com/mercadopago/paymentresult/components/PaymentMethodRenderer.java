package com.mercadopago.paymentresult.components;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.customviews.MPTextView;

/**
 * Created by mromar on 11/22/17.
 */

public class PaymentMethodRenderer extends Renderer<PaymentMethodComponent> {
    @Override
    public View render() {
        final View bodyView = LayoutInflater.from(context).inflate(R.layout.mpsdk_payment_method_component, null, false);
        final ImageView imageView = bodyView.findViewById(R.id.mpsdkPaymentMethodIcon);
        final MPTextView descriptionTextView = bodyView.findViewById(R.id.mpsdkPaymentMethodDescription);
        final MPTextView detailTextView = bodyView.findViewById(R.id.mpsdkPaymentMethodDetail);
        final MPTextView statementDescriptionTextView = bodyView.findViewById(R.id.mpsdkStatementDescription);
        final FrameLayout totalAmountContainer = bodyView.findViewById(R.id.mpsdkTotalAmountContainer);

        imageView.setImageDrawable(component.getImage());

        final Renderer totalAmountRenderer = RendererFactory.create(context, component.getTotalAmountComponent());
        final View amountView = totalAmountRenderer.render();
        totalAmountContainer.addView(amountView);

        setText(descriptionTextView, component.getDescription());
        setText(detailTextView, component.getDetail());
        setText(statementDescriptionTextView, component.getDisclaimer());

        return bodyView;
    }
}
