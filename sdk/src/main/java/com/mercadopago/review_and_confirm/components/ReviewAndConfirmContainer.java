package com.mercadopago.review_and_confirm.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.model.Summary;
import com.mercadopago.review_and_confirm.FullSummaryProvider;
import com.mercadopago.review_and_confirm.models.PaymentModel;
import com.mercadopago.review_and_confirm.models.ReviewAndConfirmPreferences;
import com.mercadopago.review_and_confirm.models.SummaryModel;
import com.mercadopago.review_and_confirm.models.TermsAndConditionsModel;

public class ReviewAndConfirmContainer extends Component<ReviewAndConfirmContainer.Props, Void> {

    private FullSummaryProvider fullSummaryProvider;

    static {
        RendererFactory.register(ReviewAndConfirmContainer.class, ReviewAndConfirmRenderer.class);
    }

    public ReviewAndConfirmContainer(@NonNull final Props props,
                                     @NonNull final FullSummaryProvider fullSummaryProvider) {
        super(props);
        this.fullSummaryProvider = fullSummaryProvider;
    }

    public ReviewAndConfirmContainer(@NonNull Props props, @NonNull ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }

    public Component createTermsAndConditions() {
        return new TermsAndCondition(props.termsAndConditionsModel, getDispatcher());
    }

    public Summary getSummaryComponent() {

        SummaryProps summaryProps = new SummaryProps(props.summaryModel);

        return new Summary(summaryProps, summaryProvider);
    }

    public static class Props {
        final TermsAndConditionsModel termsAndConditionsModel;
        final PaymentModel paymentModel;
        final SummaryModel summaryModel;
        final ReviewAndConfirmPreferences preferences;

        public Props(final TermsAndConditionsModel termsAndConditionsModel,
                     final PaymentModel paymentModel,
                     final SummaryModel summaryModel,
                     final ReviewAndConfirmPreferences preferences) {

            this.termsAndConditionsModel = termsAndConditionsModel;
            this.paymentModel = paymentModel;
            this.summaryModel = summaryModel;
            this.preferences = preferences;
        }
    }
}
