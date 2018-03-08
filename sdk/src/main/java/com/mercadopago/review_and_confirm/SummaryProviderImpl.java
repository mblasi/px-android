package com.mercadopago.review_and_confirm;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.mercadopago.R;
import com.mercadopago.core.CheckoutStore;
import com.mercadopago.preferences.ReviewScreenPreference;

import static com.mercadopago.util.TextUtils.isEmpty;

/**
 * Created by mromar on 3/5/18.
 */

public class SummaryProviderImpl implements SummaryProvider {

    private final Context context;

    public SummaryProviderImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getSummaryProductsTitle() {
        ReviewScreenPreference reviewScreenPreference = CheckoutStore.getInstance().getReviewScreenPreference();
        String summaryProductTitle;

        if (reviewScreenPreference != null && !isEmpty(reviewScreenPreference.getProductTitle())) {
            summaryProductTitle = reviewScreenPreference.getProductTitle();
        } else {
            summaryProductTitle = context.getString(R.string.mpsdk_review_summary_product);
        }

        return summaryProductTitle;
    }

    @Override
    public int getDefaultTextColor() {
        return ContextCompat.getColor(context, R.color.mpsdk_summary_text_color);
    }

    @Override
    public String getSummaryShippingTitle() {
        return context.getString(R.string.mpsdk_review_summary_shipping);
    }

    @Override
    public int getDiscountTextColor(){
        return ContextCompat.getColor(context, R.color.mpsdk_summary_discount_color);
    }

    @Override
    public String getSummaryArrearTitle() {
        return context.getString(R.string.mpsdk_review_summary_arrear);
    }

    @Override
    public String getSummaryTaxesTitle(){
        return context.getString(R.string.mpsdk_review_summary_taxes);
    }

    @Override
    public String getSummaryDiscountsTitle() {
        return context.getString(R.string.mpsdk_review_summary_discounts);
    }

    @Override
    public int getDisclaimerTextColor() {
        ReviewScreenPreference reviewScreenPreference = CheckoutStore.getInstance().getReviewScreenPreference();
        int disclaimerTextColor;

        if (isEmpty(reviewScreenPreference.getDisclaimerTextColor())) {
            disclaimerTextColor = ContextCompat.getColor(context, R.color.mpsdk_default_disclaimer);
        } else {
            disclaimerTextColor = Color.parseColor(reviewScreenPreference.getDisclaimerTextColor());
        }

        return disclaimerTextColor;
    }

    @Override
    public String getSummaryChargesTitle() {
        return context.getString(R.string.mpsdk_review_summary_charges);
    }
}
