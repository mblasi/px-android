package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;

/**
 * Created by vaserber on 10/20/17.
 */

public class PaymentResultFooterComponent extends Component<String> {

    public PaymentResultFooterComponent(String props, @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }

    @Override
    public void applyProps(@NonNull String props) {

    }
}
