package com.mercadopago.reviewconfirm.props;

import android.support.annotation.NonNull;

import com.mercadopago.model.Item;
import com.mercadopago.paymentresult.formatter.BodyAmountFormatter;

/**
 * Created by vaserber on 09/01/2018.
 */

public class ItemProps {

    public final Item item;
    public final BodyAmountFormatter amountFormatter;

    public ItemProps(@NonNull final Item item,
                     @NonNull final BodyAmountFormatter amountFormatter) {
        this.item = item;
        this.amountFormatter = amountFormatter;
    }

    public ItemProps(@NonNull final Builder builder) {
        this.item = builder.item;
        this.amountFormatter = builder.amountFormatter;
    }

    public Builder toBuilder() {
        return new Builder()
                .setItem(this.item)
                .setAmountFormatter(this.amountFormatter);
    }

    public static class Builder {

        public Item item;
        public BodyAmountFormatter amountFormatter;

        public Builder setItem(Item item) {
            this.item = item;
            return this;
        }

        public Builder setAmountFormatter(BodyAmountFormatter amountFormatter) {
            this.amountFormatter = amountFormatter;
            return this;
        }

        public ItemProps build() {
            return new ItemProps(this);
        }
    }
}
