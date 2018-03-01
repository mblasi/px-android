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

public class MainPayment extends PluginComponent<Void> {

    private final Handler handler = new Handler();

    static {
        RendererFactory.register(MainPayment.class, MainPaymentRenderer.class);
    }

    public MainPayment(@NonNull final Props props) {
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

                final ProcessorPaymentResult result = new ProcessorPaymentResult(
                        8406656l,
                        Payment.StatusCodes.STATUS_PENDING,
                        Payment.StatusCodes.STATUS_DETAIL_PENDING_WAITING_PAYMENT,
                        props.paymentData);

                getDispatcher().dispatch(new PluginPaymentResultAction(result));
            }
        }, 2000);
    }
}