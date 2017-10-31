package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;
import com.mercadopago.paymentresult.props.IconProps;

/**
 * Created by vaserber on 10/23/17.
 */

public class IconComponent extends Component<IconProps> {

    public IconComponent(IconProps props, @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }

    @Override
    public void applyProps(@NonNull IconProps props) {

    }
}
