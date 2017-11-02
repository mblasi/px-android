package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;
import com.mercadopago.paymentresult.props.HeaderProps;
import com.mercadopago.paymentresult.props.IconProps;

/**
 * Created by vaserber on 10/20/17.
 */

public class HeaderComponent extends Component<HeaderProps> {

    public HeaderComponent(@NonNull final HeaderProps props,
                           @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }

    public IconComponent getIconComponent() {

        final IconProps iconProps = new IconProps.Builder()
                .setIconImage(props.iconImage)
                .setBadgeImage(props.badgeImage)
                .build();

        return new IconComponent(iconProps, getDispatcher());
    }
}