package com.mercadopago.paymentresult.renderers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.paymentresult.components.PaymentResultContainer;

/**
 * Created by vaserber on 10/20/17.
 */

public class PaymentResultRenderer extends Renderer<PaymentResultContainer> {

    @Override
    public View render() {
        View view;

        if (component.isLoading()) {

            view = RendererFactory.create(context, component.getLoadingComponent()).render();

        } else {

            view = LayoutInflater.from(context).inflate(R.layout.mpsdk_payment_result_container, null, false);
            final ViewGroup parentViewGroup = (ViewGroup) view.findViewById(R.id.mpsdkPaymentResultContainer);

            final Renderer headerRenderer = RendererFactory.create(context, component.getHeaderComponent());
            final View header = headerRenderer.render();
            parentViewGroup.addView(header);

            final Renderer bodyRenderer = RendererFactory.create(context, component.getBodyComponent());
            final View body = bodyRenderer.render();
            parentViewGroup.addView(body);

            final Renderer footerRenderer = RendererFactory.create(context, component.getFooterComponent());
            final View footer = footerRenderer.render();
            parentViewGroup.addView(footer);
        }
        return view;
    }
}
