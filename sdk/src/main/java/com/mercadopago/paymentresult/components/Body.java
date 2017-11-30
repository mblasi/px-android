package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;
import com.mercadopago.model.Payment;
import com.mercadopago.paymentresult.PaymentResultProvider;
import com.mercadopago.paymentresult.props.BodyErrorProps;
import com.mercadopago.paymentresult.props.InstructionsProps;
import com.mercadopago.paymentresult.props.PaymentResultBodyProps;

/**
 * Created by vaserber on 10/23/17.
 */

public class Body extends Component<PaymentResultBodyProps> {

    public PaymentResultProvider resourcesProvider;

    public Body(@NonNull final PaymentResultBodyProps props,
                @NonNull final ActionDispatcher dispatcher,
                @NonNull final PaymentResultProvider provider) {
        super(props, dispatcher);
        this.resourcesProvider = provider;
    }

    public boolean hasInstructions() {
        return props.instruction != null;
    }

    public Instructions getInstructionsComponent() {
        final InstructionsProps instructionsProps = new InstructionsProps.Builder()
                .setInstruction(props.instruction)
                .setProcessingMode(props.processingMode)
                .build();
        return new Instructions(instructionsProps, getDispatcher());
    }

    public boolean hasBodyError() {
        return props.status != null && props.statusDetail != null &&
                (isPendingWithBody() || isRejectedWithBody());
    }

    private boolean isPendingWithBody() {
        return (props.status.equals(Payment.StatusCodes.STATUS_PENDING) || props.status.equals(Payment.StatusCodes.STATUS_IN_PROCESS)) &&
                (props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_PENDING_CONTINGENCY) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_PENDING_REVIEW_MANUAL));
    }

    private boolean isRejectedWithBody() {
        return (props.status.equals(Payment.StatusCodes.STATUS_REJECTED) &&
                (props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_OTHER_REASON) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_REJECTED_REJECTED_BY_BANK) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_REJECTED_REJECTED_INSUFFICIENT_DATA) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_DUPLICATED_PAYMENT) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_MAX_ATTEMPTS) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_REJECTED_HIGH_RISK) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED) ||
                        props.statusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_INSUFFICIENT_AMOUNT)));
    }

    public BodyError getBodyErrorComponent() {
        final BodyErrorProps bodyErrorProps = new BodyErrorProps.Builder()
                .setStatus(props.status)
                .setStatusDetail(props.statusDetail)
                .setPaymentMethodName(props.paymentData.getPaymentMethod().getName())
                .build();
        return new BodyError(bodyErrorProps, getDispatcher(), resourcesProvider);
    }
}
