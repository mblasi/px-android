package com.mercadopago.reviewconfirm.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mercadopago.R;
import com.mercadopago.components.Renderer;
import com.mercadopago.components.RendererFactory;

/**
 * Created by nfortuna on 1/3/18.
 */

public class ReviewRenderer extends Renderer<ReviewContainer> {

    @Override
    public View render() {

        final ViewGroup view = (ViewGroup) LayoutInflater.from(context)
                .inflate(R.layout.mpsdk_component_review_confirm_container, null);

        //Render summary componente, view es un LinearLayout vertical,
        // agregar todos las vistas que sean necesarias.
        view.addView(RendererFactory.create(context, component.getSummary()).render());

        //Render item componente
        // ...


        //Render payment method componente
        // ...


        // Otros....

        return view;
    }
}