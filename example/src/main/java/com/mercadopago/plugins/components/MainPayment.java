package com.mercadopago.plugins.components;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.mercadopago.components.RendererFactory;
import com.mercadopago.examples.R;
import com.mercadopago.model.Payment;
import com.mercadopago.paymentresult.components.Footer;
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

                final ProcessorPaymentResult result = new ProcessorPaymentResult.Builder()
                        .setPaymentId(8406656l)
                        .setStatus(Payment.StatusCodes.STATUS_REJECTED)
                        .setStatusDetail(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_PLUGIN_PM)
                        .setHeaderTitle("Custom title")
                        .setHeaderIcon(R.drawable.mpsdk_badge_check)
                        .setFooterButtonAction(new Footer.FooterAction("Ir al inicio"))
                        .setFooterBlinkAction(new Footer.FooterAction("Custom link"))
                        .build();

                getDispatcher().dispatch(new PluginPaymentResultAction(result));
            }
        }, 2000);
    }
}