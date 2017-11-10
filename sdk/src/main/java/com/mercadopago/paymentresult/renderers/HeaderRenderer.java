package com.mercadopago.paymentresult.renderers;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.paymentresult.components.HeaderComponent;
import com.mercadopago.util.CurrenciesUtil;
import com.mercadopago.util.SuperscriptSpanAdjuster;

import java.math.BigDecimal;

/**
 * Created by vaserber on 10/20/17.
 */

public class HeaderRenderer extends Renderer<HeaderComponent> {

    @Override
    public View render() {

        final View headerView = LayoutInflater.from(context).inflate(R.layout.mpsdk_payment_result_header, null, false);
        final ViewGroup headerContainer = headerView.findViewById(R.id.mpsdkPaymentResultContainerHeader);
        final TextView titleTextView = headerView.findViewById(R.id.mpsdkHeaderTitle);
        final ViewGroup iconParentViewGroup = headerView.findViewById(R.id.iconContainer);
        final TextView labelTextView = headerView.findViewById(R.id.mpsdkHeaderLabel);
        final int background = ContextCompat.getColor(context, component.props.background);
        final int statusBarColor = ContextCompat.getColor(context, component.props.statusBarColor);

        headerContainer.setBackgroundColor(background);
        setStatusBarColor(statusBarColor);
        setText(labelTextView, component.props.label);
        renderIcon(iconParentViewGroup);
        renderTitle(titleTextView);

        //TODO: Reimplementar el body height en un componente.

        return headerView;
    }

    private void setStatusBarColor(final int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) context).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        }
    }

    private void renderIcon(@NonNull final ViewGroup parent) {
        final Renderer iconRenderer = RendererFactory.create(context, component.getIconComponent());
        View icon = iconRenderer.render();
        parent.addView(icon);
    }

    private void renderTitle(@NonNull final TextView titleTextView) {
        if (component.props.amountFormat == null) {
            setText(titleTextView, component.props.title);
        } else {
            titleTextView.setText(component.props.amountFormat.formatTextWithAmount(component.props.title));
        }
    }
}
