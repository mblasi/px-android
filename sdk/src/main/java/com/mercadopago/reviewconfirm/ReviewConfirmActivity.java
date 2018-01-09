package com.mercadopago.reviewconfirm;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mercadopago.R;
import com.mercadopago.components.Action;
import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.ComponentManager;
import com.mercadopago.core.CheckoutStore;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.Item;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Site;
import com.mercadopago.model.Token;
import com.mercadopago.preferences.DecorationPreference;
import com.mercadopago.providers.ReviewAndConfirmProvider;
import com.mercadopago.providers.ReviewAndConfirmProviderImpl;
import com.mercadopago.reviewconfirm.components.ReviewContainer;
import com.mercadopago.uicontrollers.FontCache;
import com.mercadopago.util.JsonUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by nfortuna on 1/8/18.
 */

public class ReviewConfirmActivity extends AppCompatActivity implements ActionDispatcher {

    private ReviewAndConfirmProvider provider;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        provider = new ReviewAndConfirmProviderImpl(this,
                CheckoutStore.getInstance().getReviewScreenPreference());

        final View layout = LayoutInflater.from(this).inflate(R.layout.mpsdk_activity_review_confirm, null);
        setContentView(layout);
        initializeToolbar(CheckoutStore.getInstance().getDecorationPreference());

        List<Item> items;

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Item>>() {
            }.getType();
            items = gson.fromJson(this.getIntent().getStringExtra("items"), listType);
        } catch (Exception ex) {
            items = null;
        }

        final ReviewContainer.Props props = new ReviewContainer.Props.Builder()
                .setPublicKey(getIntent().getStringExtra("merchantPublicKey"))
                .setSite(JsonUtil.getInstance().fromJson(getIntent().getStringExtra("site"), Site.class))
                .setIssuer(JsonUtil.getInstance().fromJson(getIntent().getStringExtra("issuer"), Issuer.class))
                .setTermsAndConditionsEnabled(getIntent().getBooleanExtra("termsAndConditionsEnabled", true))
                .setEditionEnabled(getIntent().getBooleanExtra("editionEnabled", true))
                .setDiscountEnabled(getIntent().getBooleanExtra("discountEnabled", true))
                .setAmount(new BigDecimal(getIntent().getStringExtra("amount")))
                .setDiscount(JsonUtil.getInstance().fromJson(getIntent().getStringExtra("discount"), Discount.class))
                .setPayerCost(JsonUtil.getInstance().fromJson(getIntent().getStringExtra("payerCost"), PayerCost.class))
                .setToken(JsonUtil.getInstance().fromJson(getIntent().getStringExtra("token"), Token.class))
                .setPaymentMethod(JsonUtil.getInstance().fromJson(getIntent().getStringExtra("paymentMethod"), PaymentMethod.class))
                .setPaymentMethodCommentInfo(getIntent().getStringExtra("paymentMethodCommentInfo"))
                .setPaymentMethodDescriptionInfo(getIntent().getStringExtra("paymentMethodDescriptionInfo"))
                .setItems(items)
                .build();

        final ReviewContainer component = new ReviewContainer(props);
        final ComponentManager componentManager = new ComponentManager(this);

        if (component == null) {
            finish();
            return;
        }

        component.setDispatcher(this);
        componentManager.render(component, (ViewGroup) layout.findViewById(R.id.contents));
    }

    private void initializeToolbar(final DecorationPreference decorationPreference) {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.mpsdkRegularToolbar);
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.mpsdkCollapsingToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable upArrow = toolbar.getNavigationIcon();
        if (upArrow != null && getSupportActionBar() != null) {
            if (decorationPreference != null && decorationPreference.hasColors()) {
                upArrow.setColorFilter(decorationPreference.getBaseColor(), PorterDuff.Mode.SRC_ATOP);
            } else {
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.mpsdk_background_blue), PorterDuff.Mode.SRC_ATOP);
            }
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        if (FontCache.hasTypeface(FontCache.CUSTOM_REGULAR_FONT)) {
            collapsingToolbar.setCollapsedTitleTypeface(FontCache.getTypeface(FontCache.CUSTOM_REGULAR_FONT));
            collapsingToolbar.setExpandedTitleTypeface(FontCache.getTypeface(FontCache.CUSTOM_REGULAR_FONT));
        }

        collapsingToolbar.setTitle(provider.getReviewTitle());
        if (decorationPreference != null && decorationPreference.hasColors()) {
            collapsingToolbar.setExpandedTitleColor(decorationPreference.getBaseColor());
            collapsingToolbar.setCollapsedTitleTextColor(decorationPreference.getBaseColor());
        } else {
            collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.mpsdk_background_blue));
            collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.mpsdk_background_blue));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void dispatch(Action action) {

    }
}
