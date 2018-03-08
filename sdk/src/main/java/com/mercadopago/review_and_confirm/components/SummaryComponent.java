package com.mercadopago.review_and_confirm.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.Component;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.review_and_confirm.SummaryProvider;
import com.mercadopago.review_and_confirm.models.SummaryModel;

/**
 * Created by mromar on 2/28/18.
 */

public class SummaryComponent extends Component<SummaryModel, Void> {

    private SummaryProvider provider;

    public static final String CFT = "CFT ";

    static {
        RendererFactory.register(SummaryComponent.class, SummaryRenderer.class);
    }

    public SummaryComponent(@NonNull final SummaryModel props,
                            @NonNull final SummaryProvider provider) {
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
