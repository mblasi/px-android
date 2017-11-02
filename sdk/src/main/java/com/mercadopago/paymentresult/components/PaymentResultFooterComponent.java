package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;

/**
 * Created by vaserber on 10/20/17.
 */
public class PaymentResultFooterComponent extends Component<String> {
    public PaymentResultFooterComponent(@NonNull final String props,
                                        @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }
}