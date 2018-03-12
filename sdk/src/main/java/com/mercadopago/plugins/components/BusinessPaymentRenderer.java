package com.mercadopago.plugins.components;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mercadopago.R;
import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.paymentresult.components.Header;
import com.mercadopago.paymentresult.props.HeaderProps;
import com.mercadopago.plugins.model.ButtonAction;

public class BusinessPaymentRenderer extends Renderer<BusinessPaymentContainer> {
    @Override
    protected View render(@NonNull final BusinessPaymentContainer component,
                          @NonNull final Context context,
                          @Nullable final ViewGroup parent) {

        LinearLayout linearLayout = createMainContainer(context);
        ScrollView scrollView = createScrollContainer(context, linearLayout);
        addHeader(component, context, linearLayout);

        if (component.props.hasHelp()) {
            addHelp(component.props.getHelp(), linearLayout);
        }

        if (component.props.hasPrimaryButton()) {
            addPrimaryButton(component.getDispatcher(),
                    component.props.getPrimaryAction(),
                    linearLayout);
        }

        if (component.props.hasSecondaryButton()) {
            addSecondaryButton(component.getDispatcher(),
                    component.props.getSecondaryAction(), linearLayout);
        }

        return scrollView;
    }

    private void addPrimaryButton(final ActionDispatcher dispatcher,
                                  final ButtonAction primaryAction,
                                  final ViewGroup parent) {
        View parentView = inflate(R.layout.mpsdk_view_text_button_blue, parent);
        TextView blueButton = parentView.findViewById(R.id.text_button_blue);
        blueButton.setText(primaryAction.getName());
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dispatcher.dispatch(primaryAction);
            }
        });


    }

    private void addSecondaryButton(final ActionDispatcher dispatcher,
                                    final ButtonAction secondaryAction,
                                    final ViewGroup parent) {

        View parentView = inflate(R.layout.mpsdk_view_text_button, parent);
        TextView whiteButton = parentView.findViewById(R.id.text_button_white);
        whiteButton.setText(secondaryAction.getName());
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dispatcher.dispatch(secondaryAction);
            }
        });
    }

    private void addHelp(final String help, final ViewGroup parent) {
        final View bodyErrorView = inflate(R.layout.mpsdk_payment_result_body_error, parent);
        TextView errorTitle = bodyErrorView.findViewById(R.id.paymentResultBodyErrorTitle);
        TextView errorDescription = bodyErrorView.findViewById(R.id.paymentResultBodyErrorDescription);
        bodyErrorView.findViewById(R.id.paymentResultBodyErrorSecondDescription).setVisibility(View.GONE);
        errorTitle.setText(parent.getContext().getString(R.string.mpsdk_what_can_do));
        errorDescription.setText(help);
    }

    private void addHeader(final @NonNull BusinessPaymentContainer component, final @NonNull Context context, final LinearLayout linearLayout) {
        Header header = new Header(HeaderProps.from(component.props, context), component.getDispatcher());
        RendererFactory.create(context, header).render(linearLayout);
    }

    @NonNull
    private ScrollView createScrollContainer(final @NonNull Context context, final LinearLayout linearLayout) {
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.addView(linearLayout);
        return scrollView;
    }

    @NonNull
    private LinearLayout createMainContainer(final @NonNull Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return linearLayout;
    }
}
