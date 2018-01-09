package com.mercadopago.reviewconfirm.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.Component;
import com.mercadopago.reviewconfirm.props.ItemProps;

import java.math.BigDecimal;

/**
 * Created by vaserber on 09/01/2018.
 */

public class ReviewItem extends Component<ItemProps> {

    public ReviewItem(@NonNull ItemProps props) {
        super(props);
    }

    public String getImageUrl() {
        String url = "";
        if (props.item != null) {
            url = props.item.getPictureUrl();
        }
        return url;
    }

    public String getTitle() {
        String title = "";
        if (props.item != null) {
            title = props.item.getTitle();
        }
        return title;
    }

    public String getDescription() {
        String description = "";
        if (props.item != null) {
            description = props.item.getDescription();
        }
        return description;
    }

    public int getQuantity() {
        int quantity = 1;
        if (props.item != null && props.item.getQuantity() != null) {
            quantity = props.item.getQuantity();
        }
        return quantity;
    }

    public String getAmount() {
        BigDecimal unitPrice;
        String priceText = "";
        if (props.item != null && props.item.getUnitPrice() != null) {
            unitPrice = props.item.getUnitPrice();
            priceText = props.amountFormatter.formatNumber(unitPrice);
        }
        return priceText;
    }
}