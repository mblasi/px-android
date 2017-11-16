package com.mercadopago.paymentresult.components;

import android.support.annotation.NonNull;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.components.Component;
import com.mercadopago.paymentresult.props.InstructionsProps;
import com.mercadopago.paymentresult.props.PaymentResultBodyProps;

/**
 * Created by vaserber on 10/23/17.
 */

public class Body extends Component<PaymentResultBodyProps> {

    public Body(@NonNull final PaymentResultBodyProps props,
                @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
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
}
