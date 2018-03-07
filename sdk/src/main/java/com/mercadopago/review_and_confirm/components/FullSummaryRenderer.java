package com.mercadopago.review_and_confirm.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;
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

import static com.mercadopago.util.TextUtils.isEmpty;

/**
 * Created by mromar on 2/28/18.
 */

public class FullSummaryRenderer extends Renderer<FullSummary> {

    @Override
    public View render(@NonNull final FullSummary component, @NonNull final Context context, final ViewGroup parent) {
        final View summaryView = inflate(R.layout.mpsdk_full_summary_component, parent);
        final MPTextView subtotalAmountTextView = summaryView.findViewById(R.id.mpsdkReviewSummarySubtotalText);
        final MPTextView subtotalLabel = summaryView.findViewById(R.id.subtotalLabel);
        final MPTextView totalAmountTextView = summaryView.findViewById(R.id.mpsdkReviewSummaryTotalText);
        final FrameLayout payerCostContainer = summaryView.findViewById(R.id.mpsdkReviewSummaryPayerCostContainer);
        final MPTextView cftTextView = summaryView.findViewById(R.id.mpsdkCFT);
        final MPTextView disclaimerTextView = summaryView.findViewById(R.id.mpsdkDisclaimer);
        final LinearLayout summaryDetailsContainer = summaryView.findViewById(R.id.mpsdkSummaryDetails);
        final LinearLayout reviewSummaryPayContainer = summaryView.findViewById(R.id.mpsdkReviewSummaryPay);
        final View firstSeparetor = summaryView.findViewById(R.id.mpsdkFirstSeparator);
        final LinearLayout totalAmountContainer = summaryView.findViewById(R.id.mpsdkReviewSummaryTotal);
        final View secondSeparator = summaryView.findViewById(R.id.mpsdkSecondSeparator);

        //summaryDetails list
        for (AmountDescription amountDescription : component.getAmountDescriptionComponents()) {
            final Renderer amountDescriptionRenderer = RendererFactory.create(context, amountDescription);
            final View amountView = amountDescriptionRenderer.render();
            summaryDetailsContainer.addView(amountView);
        }

        if (component.hasToRenderPayerCost()) {
            //payer cost
            PayerCostColumn payerCostColumn = new PayerCostColumn(context, component.props.currencyId, component.props.siteId, component.props.getInstallmentsRate(),
                    component.props.getInstallments(), component.props.getPayerCostTotalAmount(), component.props.getInstallmentAmount());
            payerCostColumn.inflateInParent(payerCostContainer, true);
            payerCostColumn.initializeControls();
            payerCostColumn.drawPayerCostWithoutTotal();

            //finance
            setText(cftTextView, component.getFinance());
            cftTextView.setVisibility(isEmpty(component.getFinance()) ? View.GONE : View.VISIBLE);
        } else {
            reviewSummaryPayContainer.setVisibility(View.GONE);
            firstSeparetor.setVisibility(View.GONE);
        }

        //subtotal
        Spanned subTotal = getFormattedAmount(component.getSubtotalAmount(), component.props.currencyId);
        setText(subtotalAmountTextView, subTotal);
        subtotalLabel.setVisibility(subTotal == null ? View.GONE : View.VISIBLE);

        //total
        setText(totalAmountTextView, getFormattedAmount(component.getTotalAmount(), component.props.currencyId));
        totalAmountContainer.setVisibility(component.getTotalAmount() == null ? View.GONE : View.VISIBLE);
        secondSeparator.setVisibility(component.getTotalAmount() == null ? View.GONE : View.VISIBLE);

        //disclaimer
        setText(disclaimerTextView, component.getSummary().getDisclaimerText());

        return summaryView;
    }

    private Spanned getFormattedAmount(BigDecimal amount, String currencyId) {
        return amount != null && !isEmpty(currencyId) ? CurrenciesUtil.getFormattedAmount(amount, currencyId) : null;
    }
}
