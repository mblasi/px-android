package com.mercadopago.review_and_confirm.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.customviews.MPTextView;

import static com.mercadopago.util.TextUtils.isEmpty;

/**
 * Created by mromar on 2/28/18.
 */

public class CompactSummaryRenderer extends Renderer<CompactSummary> {

    @Override
    public View render(@NonNull final CompactSummary component, @NonNull final Context context, final ViewGroup parent) {
        final View summaryView = inflate(R.layout.mpsdk_compact_summary_component, parent);
        final MPTextView totalAmountTextView = summaryView.findViewById(R.id.mpsdkTotalAmount);
        final MPTextView itemTitleTextView = summaryView.findViewById(R.id.mpsdkItemTitle);

        setText(totalAmountTextView, component.props.getTotalAmount().toString());
        setText(itemTitleTextView, getItemTitle(component.props.itemTitle));

        return summaryView;
    }

    private String getItemTitle(String itemTitle) {
        return isEmpty(itemTitle) ? getDefaultTitle() : itemTitle;
    }

    private String getDefaultTitle() {
        //TODO
        return "";
    }
}
