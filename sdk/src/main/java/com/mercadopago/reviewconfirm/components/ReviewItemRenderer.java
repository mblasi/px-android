package com.mercadopago.reviewconfirm.components;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.core.CheckoutSessionStore;
import com.mercadopago.customviews.MPTextView;
import com.mercadopago.preferences.ReviewScreenPreference;
import com.mercadopago.util.CircleTransform;
import com.mercadopago.util.ScaleUtil;
import com.mercadopago.util.TextUtil;
import com.squareup.picasso.Picasso;

import static com.mercadopago.util.TextUtil.isEmpty;

/**
 * Created by vaserber on 09/01/2018.
 */

public class ReviewItemRenderer extends Renderer<ReviewItem> {

    @Override
    public View render() {
        final View itemView = LayoutInflater.from(context).inflate(R.layout.mpsdk_review_item, null, false);
        final ImageView itemImage = itemView.findViewById(R.id.itemImage);
        final MPTextView itemTitle = itemView.findViewById(R.id.itemTitle);
        final MPTextView itemDescription = itemView.findViewById(R.id.itemDescription);
        final MPTextView itemQuantity = itemView.findViewById(R.id.itemQuantity);
        final MPTextView itemAmount = itemView.findViewById(R.id.itemAmount);

        drawItemImage(itemImage);
        setText(itemTitle, component.getTitle());
        setText(itemDescription, component.getDescription());
        drawProductQuantity(itemQuantity);
        drawProductAmount(itemAmount);

        return itemView;
    }

    private void drawItemImage(ImageView itemImage) {
        final String imageUrl = component.getImageUrl();
        final int resId = R.drawable.mpsdk_review_item;

        if (imageUrl == null || imageUrl.isEmpty()) {
            itemImage.setImageResource(resId);
        } else {
            int dimen = ScaleUtil.getPxFromDp(48, context);
            Picasso.with(context)
                    .load(imageUrl)
                    .transform(new CircleTransform())
                    .resize(dimen, dimen)
                    .centerInside()
                    .placeholder(resId)
                    .into(itemImage);
        }
    }

    private void drawProductQuantity(MPTextView itemQuantity) {
        ReviewScreenPreference preference = CheckoutSessionStore.getInstance().getReviewScreenPreference();
        if (preference != null) {
            if (!preference.showQuantityRow()) {
                itemQuantity.setVisibility(View.GONE);

            } else if (preference.showQuantityRow() && !isEmpty(preference.getQuantityTitle())) {
                String productQuantityText = preference.getQuantityTitle() + component.getQuantity();
                itemQuantity.setText(productQuantityText);
            } else {
                itemQuantity.setText(context.getResources().getString(R.string.mpsdk_review_product_quantity, String.valueOf(component.getQuantity())));
            }
        }
    }

    private void drawProductAmount(MPTextView itemAmount) {
        ReviewScreenPreference preference = CheckoutSessionStore.getInstance().getReviewScreenPreference();
        String originalPrice = component.getAmount();

        if (TextUtil.isEmpty(originalPrice)) {
            itemAmount.setVisibility(View.GONE);
        } else {
            String priceText = "";
            if (preference != null) {
                if (!preference.showAmountTitle()) {
                    priceText = originalPrice;
                } else if (preference.showAmountTitle() && !isEmpty(preference.getAmountTitle())) {
                    priceText = preference.getAmountTitle() + originalPrice;
                } else {
                    priceText = context.getString(R.string.mpsdk_review_product_price, originalPrice);
                }
            }
            itemAmount.setText(priceText);
        }
    }

}