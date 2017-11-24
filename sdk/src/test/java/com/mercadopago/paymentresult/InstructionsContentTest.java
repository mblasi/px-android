package com.mercadopago.paymentresult;

import android.util.Log;

import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.mocks.Instructions;
import com.mercadopago.model.Instruction;
import com.mercadopago.paymentresult.components.InstructionsContent;
import com.mercadopago.paymentresult.components.InstructionsInfo;
import com.mercadopago.paymentresult.components.InstructionsReferences;
import com.mercadopago.paymentresult.props.InstructionsContentProps;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by vaserber on 22/11/2017.
 */

public class InstructionsContentTest {

    private ActionDispatcher dispatcher;

    @Before
    public void setup() {
        dispatcher = mock(ActionDispatcher.class);
    }

    @Test
    public void testInstructionHasInfo() {
        final Instruction instruction = Instructions.getRapipagoInstruction();
        final InstructionsContentProps props = new InstructionsContentProps.Builder()
                .setInstruction(instruction)
                .build();
        final InstructionsContent component = new InstructionsContent(props, dispatcher);

        Assert.assertNotNull(instruction.getInfo());
        Assert.assertFalse(instruction.getInfo().isEmpty());
        Assert.assertTrue(component.hasInfo());
        Assert.assertNotNull(component.getInfoComponent());
    }

    @Test
    public void testInstructionsInfoHasValidPropsForRapipago() {
        final Instruction instruction = Instructions.getRapipagoInstruction();
        final InstructionsContentProps props = new InstructionsContentProps.Builder()
                .setInstruction(instruction)
                .build();
        final InstructionsContent component = new InstructionsContent(props, dispatcher);

        final InstructionsInfo instructionsInfo = component.getInfoComponent();

        Assert.assertNotNull(instructionsInfo.props.infoTitle);
        Assert.assertEquals(instructionsInfo.props.infoTitle, instruction.getInfo().get(0));
        Assert.assertTrue(instructionsInfo.props.infoContent.isEmpty());
        Assert.assertFalse(instructionsInfo.props.bottomDivider);
    }

    @Test
    public void testInstructionsInfoHasValidPropsForRedLinkAtm() {
        final Instruction instruction = Instructions.getRedLinkAtmInstruction();
        final InstructionsContentProps props = new InstructionsContentProps.Builder()
                .setInstruction(instruction)
                .build();
        final InstructionsContent component = new InstructionsContent(props, dispatcher);

        final InstructionsInfo instructionsInfo = component.getInfoComponent();

        Assert.assertNotNull(instructionsInfo.props.infoTitle);
        Assert.assertEquals(instructionsInfo.props.infoTitle, instruction.getInfo().get(0));

        List<String> infoContentList = new ArrayList<>();
        infoContentList.add(instruction.getInfo().get(2));
        infoContentList.add(instruction.getInfo().get(3));
        infoContentList.add(instruction.getInfo().get(4));

        Assert.assertFalse(instructionsInfo.props.infoContent.isEmpty());
        Assert.assertEquals(instructionsInfo.props.infoContent, infoContentList);

        Assert.assertTrue(instruction.getInfo().get(1).isEmpty());
        Assert.assertTrue(instruction.getInfo().get(5).isEmpty());
        Assert.assertTrue(instructionsInfo.props.bottomDivider);
    }

    @Test
    public void testInstructionsInfoHasValidPropsForBoletoBankTransfer() {
        final Instruction instruction = Instructions.getBoletoInstructionBankTransfer();
        final InstructionsContentProps props = new InstructionsContentProps.Builder()
                .setInstruction(instruction)
                .build();
        final InstructionsContent component = new InstructionsContent(props, dispatcher);

        final InstructionsInfo instructionsInfo = component.getInfoComponent();

        Assert.assertTrue(instructionsInfo.props.infoTitle.isEmpty());

        List<String> infoContentList = new ArrayList<>();
        infoContentList.add(instruction.getInfo().get(0));
        infoContentList.add(instruction.getInfo().get(1));

        Assert.assertFalse(instructionsInfo.props.infoContent.isEmpty());
        Assert.assertEquals(instructionsInfo.props.infoContent, infoContentList);

        Assert.assertFalse(instructionsInfo.props.bottomDivider);
    }

    @Test
    public void testInstructionHasReferences() {
        final Instruction instruction = Instructions.getRapipagoInstruction();
        final InstructionsContentProps props = new InstructionsContentProps.Builder()
                .setInstruction(instruction)
                .build();
        final InstructionsContent component = new InstructionsContent(props, dispatcher);

        Assert.assertNotNull(instruction.getReferences());
        Assert.assertFalse(instruction.getReferences().isEmpty());
        Assert.assertTrue(component.hasReferences());
        Assert.assertNotNull(component.getReferencesComponent());
    }

    @Test
    public void testInstructionsReferencesHasValidPropsForRapipago() {
        final Instruction instruction = Instructions.getRapipagoInstruction();
        final InstructionsContentProps props = new InstructionsContentProps.Builder()
                .setInstruction(instruction)
                .build();
        final InstructionsContent component = new InstructionsContent(props, dispatcher);

        final InstructionsReferences references = component.getReferencesComponent();

        Assert.assertTrue(references.props.title.isEmpty());

        Assert.assertNotNull(instruction.getReferences());
        Assert.assertFalse(instruction.getReferences().isEmpty());
        Assert.assertNotNull(references.props.references);
        Assert.assertFalse(references.props.references.isEmpty());
        Assert.assertEquals(references.props.references, instruction.getReferences());
    }

    @Test
    public void testInstructionsReferencesHasValidPropsForRedLinkAtm() {
        final Instruction instruction = Instructions.getRedLinkAtmInstruction();
        final InstructionsContentProps props = new InstructionsContentProps.Builder()
                .setInstruction(instruction)
                .build();
        final InstructionsContent component = new InstructionsContent(props, dispatcher);

        final InstructionsReferences references = component.getReferencesComponent();

        Assert.assertFalse(references.props.title.isEmpty());
        Assert.assertEquals(references.props.title, instruction.getInfo().get(6));

        Assert.assertNotNull(instruction.getReferences());
        Assert.assertFalse(instruction.getReferences().isEmpty());
        Assert.assertNotNull(references.props.references);
        Assert.assertFalse(references.props.references.isEmpty());
        Assert.assertEquals(references.props.references, instruction.getReferences());
    }
}
