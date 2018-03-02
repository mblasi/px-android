package com.mercadopago.plugins.components;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.mercadopago.components.RendererFactory;
import com.mercadopago.model.Payment;
import com.mercadopago.plugins.PluginComponent;
import com.mercadopago.plugins.PluginPaymentResultAction;
import com.mercadopago.plugins.model.ProcessorPaymentResult;

/**
 * Created by nfortuna on 12/13/17.
 */

public class SamplePayment extends PluginComponent<Void> {

    private final Handler handler = new Handler();

    static {
        RendererFactory.register(SamplePayment.class, SamplePaymentRenderer.class);
    }

    public SamplePayment(@NonNull final Props props) {
        super(props);
    }

    @Override
    public void onViewAttachedToWindow() {
        executePayment();
    }

    public void executePayment() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                final ProcessorPaymentResult result = new ProcessorPaymentResult.Builder()
                        .setPaymentId(8406656l)
                        .setStatus(Payment.StatusCodes.STATUS_REJECTED)
                        .setStatusDetail(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_PLUGIN_PM)
                        .setHeaderTitle("Sample title")
                        .build();

                getDispatcher().dispatch(new PluginPaymentResultAction(result));
            }
        }, 2000);
    }
}