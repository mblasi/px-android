package com.mercadopago.review_and_confirm.components;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.customviews.MPTextView;
import com.mercadopago.uicontrollers.payercosts.PayerCostColumn;
import com.mercadopago.util.CurrenciesUtil;

import java.math.BigDecimal;

/**
 * Created by mromar on 2/28/18.
 */

public class SummaryRenderer extends Renderer<Summary> {

    @Override
    public View render(final Summary component, final Context context, ViewGroup viewGroup) {
        final View summaryView = LayoutInflater.from(context).inflate(R.layout.mpsdk_review_summary_component, null, false);
        final MPTextView subtotalAmountTextView = summaryView.findViewById(R.id.mpsdkReviewSummarySubtotalText);
        final MPTextView totalAmountTextView = summaryView.findViewById(R.id.mpsdkReviewSummaryTotalText);
        final FrameLayout payerCostContainer = summaryView.findViewById(R.id.mpsdkReviewSummaryPayerCostContainer);
        final MPTextView cftTextView = summaryView.findViewById(R.id.mpsdkCFT);
        final MPTextView disclaimerTextView = summaryView.findViewById(R.id.mpsdkDisclaimer);
        final LinearLayout summaryDetailsContainer = summaryView.findViewById(R.id.mpsdkSummaryDetails);

        //summaryDetails list
        for (AmountDescription amountDescription : component.getAmountDescriptionComponents()) {
            final Renderer amountDescriptionRenderer = RendererFactory.create(context, amountDescription);
            final View amountView = amountDescriptionRenderer.render();
            summaryDetailsContainer.addView(amountView);
        }

        if (component.hasToRenderPayerCost()) {
            //payer cost
            PayerCostColumn payerCostColumn = new PayerCostColumn(context, component.props.site);
            payerCostColumn.inflateInParent(payerCostContainer, true);
            payerCostColumn.initializeControls();
            payerCostColumn.drawPayerCostWithoutTotal(component.props.payerCost);

            //finance
            setText(cftTextView, component.getFinance());
        }

        //subtotal
        setText(subtotalAmountTextView, getFormattedAmount(component.getSubtotalAmount(), component.props.currencyId));

        //total
        setText(totalAmountTextView, getFormattedAmount(component.getTotalAmount(), component.props.currencyId));

        //disclaimer
        setText(disclaimerTextView, component.props.summary.getDisclaimerText());

        return summaryView;
    }

    private Spanned getFormattedAmount(BigDecimal amount, String currencyId) {
        return CurrenciesUtil.getFormattedAmount(amount, currencyId);
    }
}
