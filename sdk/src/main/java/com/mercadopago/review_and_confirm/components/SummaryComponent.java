package com.mercadopago.review_and_confirm.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.Component;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.core.CheckoutStore;
import com.mercadopago.model.SummaryDetail;
import com.mercadopago.preferences.ReviewScreenPreference;
import com.mercadopago.review_and_confirm.FullSummaryProvider;
import com.mercadopago.review_and_confirm.models.SummaryModel;
import com.mercadopago.review_and_confirm.props.AmountDescriptionProps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mercadopago.util.TextUtils.isEmpty;

/**
 * Created by mromar on 2/28/18.
 */

public class SummaryComponent extends Component<SummaryModel, Void> {

    private FullSummaryProvider provider;

    public static final String CFT = "CFT ";

    static {
        RendererFactory.register(SummaryComponent.class, SummaryRenderer.class);
    }

    public SummaryComponent(@NonNull final SummaryModel props,
                            @NonNull final FullSummaryProvider provider) {
        super(props);
        this.provider = provider;
    }

    public FullSummary getFullSummary() {
        return new FullSummary(props, provider);
    }

    public CompactSummary getCompactSummary() {
        return new CompactSummary(props, provider);
    }
}
