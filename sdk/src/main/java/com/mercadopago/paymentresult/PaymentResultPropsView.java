package com.mercadopago.paymentresult;

import com.mercadopago.model.Instruction;
import com.mercadopago.model.PaymentResult;
import com.mercadopago.mvp.MvpView;
import com.mercadopago.paymentresult.formatter.AmountFormat;
import com.mercadopago.paymentresult.formatter.HeaderTitleFormatter;
import com.mercadopago.preferences.PaymentResultScreenPreference;

public interface PaymentResultPropsView extends MvpView {

    void setPropPaymentResult(final PaymentResult paymentResult, final PaymentResultScreenPreference paymentResultScreenPreference, final HeaderTitleFormatter amountFormat, final boolean showLoading);

    void setPropInstruction(final Instruction instruction, final HeaderTitleFormatter amountFormat, final boolean showLoading);

    void notifyPropsChanged();
}
