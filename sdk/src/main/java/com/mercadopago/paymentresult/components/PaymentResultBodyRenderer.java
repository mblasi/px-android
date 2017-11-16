package com.mercadopago.paymentresult.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.paymentresult.components.Body;

/**
 * Created by vaserber on 10/23/17.
 */

public class PaymentResultBodyRenderer extends Renderer<Body> {
    @Override
    public View render() {
        final View bodyView = LayoutInflater.from(context).inflate(R.layout.mpsdk_payment_result_body, null, false);
        final ViewGroup bodyViewGroup = (FrameLayout) bodyView.findViewById(R.id.mpsdkPaymentResultContainerBody);

        if (component.hasInstructions()) {
            final Renderer instructionsRenderer = RendererFactory.create(context, component.getInstructionsComponent());
            final View instructions = instructionsRenderer.render();
            bodyViewGroup.addView(instructions);
        }

        stretchHeight(bodyViewGroup);
        return bodyView;
    }
}
