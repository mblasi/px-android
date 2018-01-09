package com.mercadopago.reviewconfirm.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.Component;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.Item;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Site;
import com.mercadopago.model.Token;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by nfortuna on 1/3/18.
 */

public class Summary extends Component<Summary.Props> {

    static {
        RendererFactory.register(Summary.class, SummaryRenderer.class);
    }

    public Summary(@NonNull Props props) {
        super(props);
    }

    public static class Props {
    }
}