package com.mercadopago.plugins.components;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.mercadopago.components.RendererFactory;
import com.mercadopago.model.Payment;
import com.mercadopago.plugins.PluginComponent;
import com.mercadopago.plugins.PaymentPluginProcessorResultAction;
import com.mercadopago.plugins.model.GenericPayment;


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

                final GenericPayment result = new GenericPayment(
                        98723496234l,
                        Payment.StatusCodes.STATUS_APPROVED,
                        Payment.StatusDetail.STATUS_DETAIL_APPROVED_PLUGIN_PM,
                        props.paymentData);

                getDispatcher().dispatch(new PaymentPluginProcessorResultAction(result));
            }
        }, 2000);
    }
}