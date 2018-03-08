package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.paymentresult.props.HeaderProps;
import com.mercadopago.paymentresult.props.IconProps;

/**
 * Created by vaserber on 10/20/17.
 */

public class Header extends Component<HeaderProps, Void> {

    static {
        RendererFactory.register(Header.class, HeaderRenderer.class);
    }

    public Header(@NonNull final HeaderProps props,
                  @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }

    public Icon getIconComponent() {

        final IconProps iconProps = new IconProps.Builder()
                .setIconImage(props.iconImage)
                .setIconUrl(props.iconUrl)
                .setBadgeImage(props.badgeImage)
                .build();

        return new Icon(iconProps, getDispatcher());
    }
}