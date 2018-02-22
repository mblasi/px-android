package com.mercadopago;
import com.mercadopago.examples.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mercadopago.model.Reviewable;

/**
 * Created by vaserber on 22/02/2018.
 */

public class CellphoneReview extends Reviewable {
    protected View mView;
    protected TextView mNumberTextView;

    private Context mContext;
    private String mNumber;

    public CellphoneReview(Context context, String cellphoneNumber) {
        this.mContext = context;
        this.mNumber = cellphoneNumber;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public View inflateInParent(ViewGroup parent, boolean attachToRoot) {
        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.example_item, parent, attachToRoot);
        return mView;
    }

    @Override
    public void initializeControls() {
        mNumberTextView = (TextView) mView.findViewById(R.id.phoneNumber);
    }

    @Override
    public void draw() {
        mNumberTextView.setText(mNumber);
    }

}
