package com.mercadopago.paymentresult;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.constants.ProcessingModes;
import com.mercadopago.mocks.Instructions;
import com.mercadopago.model.Instruction;
import com.mercadopago.model.Payment;
import com.mercadopago.paymentresult.components.Body;
import com.mercadopago.paymentresult.props.PaymentResultBodyProps;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by vaserber on 22/11/2017.
 */

public class BodyTest {


    private ActionDispatcher dispatcher;

    @Before
    public void setup() {
        dispatcher = mock(ActionDispatcher.class);
    }

    @Test
    public void testBodyHasInstructions() {
        final Body body = new Body(getBodyPropsForInstructions(Instructions.getRapipagoInstruction()), dispatcher);

        Assert.assertTrue(body.hasInstructions());
        Assert.assertNotNull(body.getInstructionsComponent());
    }

    @Test
    public void testInstructionsHasValidProps() {
        final Instruction instruction = Instructions.getRapipagoInstruction();
        final Body body = new Body(getBodyPropsForInstructions(instruction), dispatcher);

        com.mercadopago.paymentresult.components.Instructions instructionsComponent = body.getInstructionsComponent();
        Assert.assertEquals(instructionsComponent.props.instruction, instruction);
    }

    private PaymentResultBodyProps getBodyPropsForInstructions(Instruction instruction) {
        return new PaymentResultBodyProps.Builder()
                .setStatus(Payment.StatusCodes.STATUS_PENDING)
                .setStatusDetail(Payment.StatusCodes.STATUS_DETAIL_PENDING_WAITING_PAYMENT)
                .setProcessingMode(ProcessingModes.AGGREGATOR)
                .setInstruction(instruction)
                .build();

    }
}
