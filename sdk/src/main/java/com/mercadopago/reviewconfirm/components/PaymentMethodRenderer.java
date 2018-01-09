package com.mercadopago.reviewconfirm.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;
import com.mercadopago.customviews.MPTextView;
import com.mercadopago.paymentresult.components.PaymentMethod;

/**
 * Created by mromar on 11/22/17.
 */

public class PaymentMethodRenderer extends Renderer<PaymentMethod> {
    @Override
    public View render() {
        final View paymentMethodView = LayoutInflater.from(context).inflate(R.layout.mpsdk_component_reviewconfirm_payment_method, null, false);
        final ViewGroup paymentMethodViewGroup = paymentMethodView.findViewById(R.id.mpsdkPaymentMethodContainer);
        final ImageView imageView = paymentMethodView.findViewById(R.id.mpsdkPaymentMethodIcon);
        final MPTextView descriptionTextView = paymentMethodView.findViewById(R.id.mpsdkPaymentMethodDescription);
        final MPTextView detailTextView = paymentMethodView.findViewById(R.id.mpsdkPaymentMethodDetail);
        final MPTextView statementDescriptionTextView = paymentMethodView.findViewById(R.id.mpsdkStatementDescription);
        final MPTextView accreditationTimeTextView = paymentMethodView.findViewById(R.id.mpsdkAccreditationTime);

        imageView.setImageDrawable(component.getImage());

        setText(descriptionTextView, component.getDescription());
        setText(detailTextView, component.getDetail());
        setText(statementDescriptionTextView, component.getDisclaimer());
        setText(accreditationTimeTextView, component.getAccreditationTime());

        stretchHeight(paymentMethodViewGroup);
        return paymentMethodView;
    }
}
