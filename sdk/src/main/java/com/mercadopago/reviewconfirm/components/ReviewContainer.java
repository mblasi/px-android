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
import com.mercadopago.paymentresult.formatter.BodyAmountFormatter;
import com.mercadopago.reviewconfirm.props.ItemProps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nfortuna on 1/3/18.
 */

public class ReviewContainer extends Component<ReviewContainer.Props> {

    static {
        RendererFactory.register(ReviewContainer.class, ReviewRenderer.class);
        RendererFactory.register(ReviewItem.class, ReviewItemRenderer.class);
    }

    public ReviewContainer(@NonNull Props props) {
        super(props);
    }

    public Summary getSummary() {
        final Summary.Props props = new Summary.Props();
        return new Summary(props);
    }

    public List<ReviewItem> getItemComponents() {
        List<ReviewItem> componentList = new ArrayList<>();

        for (Item item: props.items) {
            final ItemProps itemProps = new ItemProps.Builder()
                    .setItem(item)
                    .setAmountFormatter(new BodyAmountFormatter(props.site.getCurrencyId(), item.getUnitPrice()))
                    .build();

            final ReviewItem component = new ReviewItem(itemProps);

            componentList.add(component);
        }

        return componentList;
    }

    public static class Props {

        public final String publicKey;
        public final Site site;
        public final Issuer issuer;
        public final boolean termsAndConditionsEnabled;
        public final boolean editionEnabled;
        public final boolean discountEnabled;
        public final BigDecimal amount;
        public final Discount discount;
        public final PayerCost payerCost;
        public final Token token;
        public final PaymentMethod paymentMethod;
        public final String paymentMethodCommentInfo;
        public final String paymentMethodDescriptionInfo;
        public final List<Item> items;

        private Props(final Builder builder) {
            this.publicKey = builder.publicKey;
            this.site = builder.site;
            this.issuer = builder.issuer;
            this.termsAndConditionsEnabled = builder.termsAndConditionsEnabled;
            this.editionEnabled = builder.editionEnabled;
            this.discountEnabled = builder.discountEnabled;
            this.amount = builder.amount;
            this.discount = builder.discount;
            this.payerCost = builder.payerCost;
            this.token = builder.token;
            this.paymentMethod = builder.paymentMethod;
            this.paymentMethodCommentInfo = builder.paymentMethodCommentInfo;
            this.paymentMethodDescriptionInfo = builder.paymentMethodDescriptionInfo;
            this.items = builder.items;
        }

        public static class Builder {

            public String publicKey;
            public Site site;
            public Issuer issuer;
            public boolean termsAndConditionsEnabled;
            public boolean editionEnabled;
            public boolean discountEnabled;
            public BigDecimal amount;
            public Discount discount;
            public PayerCost payerCost;
            public Token token;
            public PaymentMethod paymentMethod;
            public String paymentMethodCommentInfo;
            public String paymentMethodDescriptionInfo;
            public List<Item> items;

            public Builder setPublicKey(String publicKey) {
                this.publicKey = publicKey;
                return this;
            }

            public Builder setSite(Site site) {
                this.site = site;
                return this;
            }

            public Builder setIssuer(Issuer issuer) {
                this.issuer = issuer;
                return this;
            }

            public Builder setTermsAndConditionsEnabled(boolean termsAndConditionsEnabled) {
                this.termsAndConditionsEnabled = termsAndConditionsEnabled;
                return this;
            }

            public Builder setEditionEnabled(boolean editionEnabled) {
                this.editionEnabled = editionEnabled;
                return this;
            }

            public Builder setDiscountEnabled(boolean discountEnabled) {
                this.discountEnabled = discountEnabled;
                return this;
            }

            public Builder setAmount(BigDecimal amount) {
                this.amount = amount;
                return this;
            }

            public Builder setDiscount(Discount discount) {
                this.discount = discount;
                return this;
            }

            public Builder setPayerCost(PayerCost payerCost) {
                this.payerCost = payerCost;
                return this;
            }

            public Builder setToken(Token token) {
                this.token = token;
                return this;
            }

            public Builder setPaymentMethod(PaymentMethod paymentMethod) {
                this.paymentMethod = paymentMethod;
                return this;
            }

            public Builder setPaymentMethodCommentInfo(String paymentMethodCommentInfo) {
                this.paymentMethodCommentInfo = paymentMethodCommentInfo;
                return this;
            }

            public Builder setPaymentMethodDescriptionInfo(String paymentMethodDescriptionInfo) {
                this.paymentMethodDescriptionInfo = paymentMethodDescriptionInfo;
                return this;
            }

            public Builder setItems(List<Item> items) {
                this.items = items;
                return this;
            }

            public Props build() {
                return new Props(this);
            }
        }
    }
}