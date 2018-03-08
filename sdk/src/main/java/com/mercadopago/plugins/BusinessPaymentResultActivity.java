package com.mercadopago.plugins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mercadopago.components.Action;
import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.ComponentManager;
import com.mercadopago.paymentresult.components.Header;
import com.mercadopago.paymentresult.props.HeaderProps;
import com.mercadopago.plugins.model.BusinessPayment;
import com.mercadopago.plugins.model.ButtonAction;

public class BusinessPaymentResultActivity extends AppCompatActivity implements ActionDispatcher {

    private static final String EXTRA_BUSINESS_PAYMENT = "extra_business_payment";
    public static final String EXTRA_CLIENT_RES_CODE = "extra_res_code";

    public static void start(final AppCompatActivity activity,
                             final BusinessPayment businessPayment,
                             int requestCode) {
        Intent intent = new Intent(activity, BusinessPaymentResultActivity.class);
        intent.putExtra(EXTRA_BUSINESS_PAYMENT, businessPayment);
        activity.startActivityForResult(intent, requestCode);
    }

    private BusinessPayment parseIntent() {
        return (BusinessPayment) getIntent().getExtras().getParcelable(EXTRA_BUSINESS_PAYMENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bussines_payment_result);

        BusinessPayment businessPayment = parseIntent();

        BusinessPayment.Status status = businessPayment.getStatus();
        final HeaderProps headerProps = new HeaderProps.Builder()
                .setHeight(HeaderProps.HEADER_MODE_WRAP)
                .setBackground(status.resColor)
                .setStatusBarColor(status.resColor)
                .setIconImage(businessPayment.getIcon())
                .setBadgeImage(status.badge)
                .setTitle(businessPayment.getTitle())
                .setLabel(status.message == 0 ? null : getString(status.message))
                .build();

        Header header = new Header(headerProps, this);
        ComponentManager componentManager = new ComponentManager(this);
        componentManager.render(header);
    }

    @Override
    public void dispatch(final Action action) {
        if (action instanceof ButtonAction) {
            int resCode = ((ButtonAction) action).getResCode();
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CLIENT_RES_CODE, resCode);
            setResult(RESULT_OK, intent);
        }
    }
}
