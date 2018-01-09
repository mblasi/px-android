package com.mercadopago.reviewconfirm.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;

/**
 * Created by nfortuna on 1/3/18.
 */

public class SummaryRenderer extends Renderer<ReviewContainer> {

    @Override
    public View render() {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.mpsdk_component_summary, null);

        return view;
    }
}