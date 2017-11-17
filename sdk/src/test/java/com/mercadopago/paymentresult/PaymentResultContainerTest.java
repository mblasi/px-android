package com.mercadopago.paymentresult;

import com.mercadopago.components.Action;
import com.mercadopago.components.ActionDispatcher;
import com.mercadopago.mocks.PaymentMethods;
import com.mercadopago.mocks.PaymentResults;
import com.mercadopago.model.Instruction;
import com.mercadopago.model.Instructions;
import com.mercadopago.model.PaymentData;
import com.mercadopago.model.PaymentResult;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;
import com.mercadopago.paymentresult.components.PaymentResultContainer;
import com.mercadopago.paymentresult.model.Badge;
import com.mercadopago.paymentresult.props.HeaderProps;
import com.mercadopago.paymentresult.props.PaymentResultProps;
import com.mercadopago.preferences.PaymentResultScreenPreference;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by vaserber on 11/2/17.
 */

public class PaymentResultContainerTest {

    @Test
    public void onApprovedPaymentThenShowGreenBackground() {

        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.GREEN_BACKGROUND_COLOR);
    }

    @Test
    public void onInProcessPaymentThenShowGreenBackground() {

        PaymentResult paymentResult = PaymentResults.getStatusInProcessPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.GREEN_BACKGROUND_COLOR);
    }

    @Test
    public void onPaymentMethodOffPaymentThenShowGreenBackground() {

        PaymentResult paymentResult = PaymentResults.getPaymentMethodOffPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.GREEN_BACKGROUND_COLOR);
    }

    @Test
    public void onRejectedOtherReasonPaymentThenShowRedBackground() {

        PaymentResult paymentResult = PaymentResults.getStatusRejectedOtherPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.RED_BACKGROUND_COLOR);
    }

    @Test
    public void onRejectedCallForAuthPaymentThenShowOrangeBackground() {

        PaymentResult paymentResult = PaymentResults.getStatusCallForAuthPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.ORANGE_BACKGROUND_COLOR);
    }

    @Test
    public void onRejectedInsufficientAmountPaymentThenShowOrangeBackground() {

        PaymentResult paymentResult = PaymentResults.getStatusRejectedInsufficientAmountPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.ORANGE_BACKGROUND_COLOR);
    }

    @Test
    public void onRejectedBadFilledSecuPaymentThenShowOrangeBackground() {

        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledSecuPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.ORANGE_BACKGROUND_COLOR);
    }

    @Test
    public void onRejectedBadFilledDatePaymentThenShowOrangeBackground() {

        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledDatePaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.ORANGE_BACKGROUND_COLOR);
    }

    @Test
    public void onRejectedBadFilledFormPaymentThenShowOrangeBackground() {

        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledFormPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.ORANGE_BACKGROUND_COLOR);
    }

    @Test
    public void onEmptyPaymentResultGetDefaultBackground() {
        HeaderProps headerProps = getHeaderPropsFromContainerWith(null);

        Assert.assertEquals(headerProps.background, PaymentResultContainer.DEFAULT_BACKGROUND_COLOR);
    }

    @Test
    public void onInvalidPaymentResultStatusGetDefaultBackground() {
        PaymentData paymentData = new PaymentData();
        paymentData.setPaymentMethod(PaymentMethods.getPaymentMethodOff());

        PaymentResult paymentResult = new PaymentResult.Builder()
                .setPaymentStatus("")
                .setPaymentStatusDetail("")
                .setPaymentData(paymentData)
                .build();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        Assert.assertEquals(headerProps.background, PaymentResultContainer.DEFAULT_BACKGROUND_COLOR);
    }

    @Test
    public void onAccreditedPaymentThenShowItemIcon() {
        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.ITEM_ICON_IMAGE);
    }

    @Test
    public void onPaymentMethodOffPaymentThenShowItemIcon() {
        PaymentResult paymentResult = PaymentResults.getPaymentMethodOffPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.ITEM_ICON_IMAGE);
    }

    @Test
    public void onPaymentMethodOnInProcessThenShowCardIcon() {
        PaymentResult paymentResult = PaymentResults.getStatusInProcessPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.CARD_ICON_IMAGE);
    }

    @Test
    public void onPaymentMethodOnRejectedBadFilledThenShowCardIcon() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledFormPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.CARD_ICON_IMAGE);
    }

    @Test
    public void onPaymentMethodOnRejectedInsufficientAmountThenShowCardIcon() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedInsufficientAmountPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.CARD_ICON_IMAGE);
    }

    @Test
    public void onBoletoRejectedPaymentThenShowBoletoIcon() {
        PaymentResult paymentResult = PaymentResults.getBoletoRejectedPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.BOLETO_ICON_IMAGE);
    }

    @Test
    public void onBoletoApprovedPaymentThenShowItemIcon() {
        PaymentResult paymentResult = PaymentResults.getBoletoApprovedPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.ITEM_ICON_IMAGE);
    }

    @Test
    public void onPaymentMethodOnRejectedOtherThenShowCardIcon() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedOtherPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.CARD_ICON_IMAGE);
    }

    @Test
    public void onEmptyPaymentResultGetDefaultIcon() {
        HeaderProps headerProps = getHeaderPropsFromContainerWith(null);

        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.DEFAULT_ICON_IMAGE);
    }

    @Test
    public void onInvalidPaymentResultStatusGetDefaultIcon() {
        PaymentData paymentData = new PaymentData();
        paymentData.setPaymentMethod(PaymentMethods.getPaymentMethodOff());

        PaymentResult paymentResult = new PaymentResult.Builder()
                .setPaymentStatus("")
                .setPaymentStatusDetail("")
                .setPaymentData(paymentData)
                .build();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.DEFAULT_ICON_IMAGE);
    }

    @Test
    public void onCustomizedIconOnApprovedStatusThenShowIt() {
        int customizedIcon = 1;

        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setApprovedHeaderIcon(customizedIcon).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.iconImage, customizedIcon);
    }

    @Test
    public void onCustomizedIconOnPaymentMethodOffThenShowIt() {
        int customizedIcon = 2;

        PaymentResult paymentResult = PaymentResults.getPaymentMethodOffPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setPendingHeaderIcon(customizedIcon).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.iconImage, customizedIcon);
    }

    @Test
    public void onCustomizedIconOnRejectedStatusThenShowIt() {
        int customizedIcon = 3;

        PaymentResult paymentResult = PaymentResults.getStatusRejectedOtherPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setRejectedHeaderIcon(customizedIcon).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.iconImage, customizedIcon);
    }

    @Test
    public void onCustomizedIconWithOtherStatusThenDontShowIt() {
        int customizedIcon = 4;

        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setRejectedHeaderIcon(customizedIcon).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);

        Assert.assertNotSame(headerProps.iconImage, customizedIcon);
        Assert.assertEquals(headerProps.iconImage, PaymentResultContainer.ITEM_ICON_IMAGE);
    }

    @Test
    public void onApprovedPaymentThenShowCheckBadge() {
        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.CHECK_BADGE_IMAGE);
    }

    @Test
    public void onInProcessPaymentThenShowPendingBadge() {
        PaymentResult paymentResult = PaymentResults.getStatusInProcessPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.PENDING_BADGE_IMAGE);
    }

    @Test
    public void onPaymentMethodOffThenShowPendingBadge() {
        PaymentResult paymentResult = PaymentResults.getPaymentMethodOffPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.PENDING_BADGE_IMAGE);
    }

    @Test
    public void onStatusCallForAuthPaymentThenShowWarningBadge() {
        PaymentResult paymentResult = PaymentResults.getStatusCallForAuthPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.WARNING_BADGE_IMAGE);
    }

    @Test
    public void onStatusBadFilledSecuPaymentThenShowWarningBadge() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledSecuPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.WARNING_BADGE_IMAGE);
    }

    @Test
    public void onStatusBadFilledDatePaymentThenShowWarningBadge() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledDatePaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.WARNING_BADGE_IMAGE);
    }

    @Test
    public void onStatusBadFilledFormPaymentThenShowWarningBadge() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledFormPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.WARNING_BADGE_IMAGE);
    }

    @Test
    public void onStatusInsufficientAmountPaymentThenShowWarningBadge() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedInsufficientAmountPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.WARNING_BADGE_IMAGE);
    }

    @Test
    public void onStatusRejectedOtherReasonPaymentThenShowErrorBadge() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedOtherPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.ERROR_BADGE_IMAGE);
    }

    @Test
    public void onEmptyPaymentResultGetDefaultBadge() {
        HeaderProps headerProps = getHeaderPropsFromContainerWith(null);

        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.DEFAULT_BADGE_IMAGE);
    }

    @Test
    public void onInvalidPaymentResultStatusGetDefaultBadge() {
        PaymentData paymentData = new PaymentData();
        paymentData.setPaymentMethod(PaymentMethods.getPaymentMethodOff());

        PaymentResult paymentResult = new PaymentResult.Builder()
                .setPaymentStatus("")
                .setPaymentStatusDetail("")
                .setPaymentData(paymentData)
                .build();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.DEFAULT_BADGE_IMAGE);
    }

    @Test
    public void onCustomizedBadgeOnApprovedStatusThenShowIt() {
        String customizedBadge = Badge.PENDING_BADGE_IMAGE;
        int badgeImage = PaymentResultContainer.PENDING_BADGE_IMAGE;

        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setBadgeApproved(customizedBadge).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.badgeImage, badgeImage);
    }

    @Test
    public void onInvalidCustomizedBadgeOnApprovedStatusThenDontShowIt() {
        String customizedBadge = "";

        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setBadgeApproved(customizedBadge).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.badgeImage, PaymentResultContainer.CHECK_BADGE_IMAGE);
    }

    @Test
    public void onApprovedPaymentThenShowApprovedTitle() {
        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getApprovedTitle());
    }

    @Test
    public void onInProcessPaymentThenShowPendingTitle() {
        PaymentResult paymentResult = PaymentResults.getStatusInProcessPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getPendingTitle());
    }

    @Test
    public void onRejectedOtherReasonPaymentThenShowTitle() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedOtherPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getRejectedOtherReasonTitle(""));
    }

    @Test
    public void onRejectedInsufficientAmountPaymentThenShowTitle() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedInsufficientAmountPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getRejectedInsufficientAmountTitle(""));
    }

    @Test
    public void onRejectedBadFilledSecuPaymentThenShowTitle() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledSecuPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getRejectedBadFilledCardTitle(""));
    }

    @Test
    public void onRejectedBadFilledDatePaymentThenShowTitle() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledDatePaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getRejectedBadFilledCardTitle(""));
    }

    @Test
    public void onRejectedBadFilledFormPaymentThenShowTitle() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledFormPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getRejectedBadFilledCardTitle(""));
    }

    @Test
    public void onRejectedCallForAuthPaymentThenShowTitle() {
        PaymentResult paymentResult = PaymentResults.getStatusCallForAuthPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getRejectedCallForAuthorizeTitle());
    }

    @Test
    public void onPaymentMethodOffPaymentThenShowInstructionsTitle() {
        PaymentResult paymentResult = PaymentResults.getPaymentMethodOffPaymentResult();
        Instruction instruction = com.mercadopago.mocks.Instructions.getRapipagoInstruction();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, instruction);

        Assert.assertEquals(headerProps.title, instruction.getTitle());
    }

    @Test
    public void onEmptyPaymentResultGetEmptyTitle() {
        HeaderProps headerProps = getHeaderPropsFromContainerWith(null);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getEmptyText());
    }

    @Test
    public void onPaymentMethodOffWithoutInstructionThenShowEmptyTitle() {
        PaymentResult paymentResult = PaymentResults.getPaymentMethodOffPaymentResult();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.title, provider.getEmptyText());
    }

    @Test
    public void onCustomizedTitleOnApprovedStatusThenShowIt() {
        String customizedTitle = "customized approved";

        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setApprovedTitle(customizedTitle).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.title, customizedTitle);
    }

    @Test
    public void onCustomizedTitleOnInProcessStatusThenShowIt() {
        String customizedTitle = "customized pending";

        PaymentResult paymentResult = PaymentResults.getStatusInProcessPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setPendingTitle(customizedTitle).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.title, customizedTitle);
    }

    @Test
    public void onCustomizedTitleOnPaymentMethodOffThenDontShowIt() {
        String customizedTitle = "customized instructions";

        PaymentResult paymentResult = PaymentResults.getPaymentMethodOffPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setPendingTitle(customizedTitle).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertNotSame(headerProps.title, customizedTitle);
    }

    @Test
    public void onCustomizedTitleOnRejectedStatusThenShowIt() {
        String customizedTitle = "customized rejected";

        PaymentResult paymentResult = PaymentResults.getStatusRejectedOtherPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setRejectedTitle(customizedTitle).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.title, customizedTitle);
    }

    @Test
    public void onApprovedPaymentThenShowEmptyLabel() {
        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getEmptyText());
    }

    @Test
    public void onInProcessPaymentThenShowEmptyLabel() {
        PaymentResult paymentResult = PaymentResults.getStatusInProcessPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getEmptyText());
    }

    @Test
    public void onPaymentMethodOffPaymentThenShowPendingLabel() {
        PaymentResult paymentResult = PaymentResults.getPaymentMethodOffPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getPendingLabel());
    }

    @Test
    public void onRejectedBadFilledStatusPaymentThenShowRejectionLabel() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedBadFilledFormPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getRejectionLabel());
    }

    @Test
    public void onRejectedInsufficientAmountStatusPaymentThenShowRejectionLabel() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedInsufficientAmountPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getRejectionLabel());
    }

    @Test
    public void onRejectedOtherReasonStatusPaymentThenShowRejectionLabel() {
        PaymentResult paymentResult = PaymentResults.getStatusRejectedOtherPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getRejectionLabel());
    }

    @Test
    public void onRejectedPaymentOffStatusPaymentThenShowRejectionLabel() {
        PaymentResult paymentResult = PaymentResults.getBoletoRejectedPaymentResult();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getRejectionLabel());
    }

    @Test
    public void onEmptyPaymentResultGetEmptyLabel() {
        HeaderProps headerProps = getHeaderPropsFromContainerWith(null);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getEmptyText());
    }

    @Test
    public void onInvalidPaymentResultStatusGetEmptyLabel() {
        PaymentData paymentData = new PaymentData();
        paymentData.setPaymentMethod(PaymentMethods.getPaymentMethodOff());

        PaymentResult paymentResult = new PaymentResult.Builder()
                .setPaymentStatus("")
                .setPaymentStatusDetail("")
                .setPaymentData(paymentData)
                .build();
        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult);
        MockedProvider provider = getMockedProvider();

        Assert.assertEquals(headerProps.label, provider.getEmptyText());
    }

    @Test
    public void onCustomizedLabelOnApprovedStatusThenShowIt() {
        String customizedLabel = "customized approved label";

        PaymentResult paymentResult = PaymentResults.getStatusApprovedPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .setApprovedLabelText(customizedLabel).build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.label, customizedLabel);
    }

    @Test
    public void onCustomizedDisabledLabelOnRejectedStatusThenHideIt() {

        PaymentResult paymentResult = PaymentResults.getStatusRejectedOtherPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .disableRejectedLabelText().build();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.label, "");
    }

    @Test
    public void onCustomizedDisabledLabelOnInvalidStatusThenShowDefaultLabel() {

        PaymentResult paymentResult = PaymentResults.getStatusInProcessPaymentResult();
        PaymentResultScreenPreference preference = new PaymentResultScreenPreference.Builder()
                .disableRejectedLabelText().build();
        MockedProvider provider = getMockedProvider();

        HeaderProps headerProps = getHeaderPropsFromContainerWith(paymentResult, preference);
        Assert.assertEquals(headerProps.label, provider.getEmptyText());
    }

    private HeaderProps getHeaderPropsFromContainerWith(PaymentResult paymentResult) {
        PaymentResultContainer container = getContainer();

        PaymentResultProps paymentResultProps = new PaymentResultProps.Builder()
                .setPaymentResult(paymentResult)
                .build();
        container.setProps(paymentResultProps);
        return container.getHeaderComponent().props;
    }

    private HeaderProps getHeaderPropsFromContainerWith(PaymentResult paymentResult, Instruction instruction) {
        PaymentResultContainer container = getContainer();

        PaymentResultProps paymentResultProps = new PaymentResultProps.Builder()
                .setPaymentResult(paymentResult)
                .setInstruction(instruction)
                .build();
        container.setProps(paymentResultProps);
        return container.getHeaderComponent().props;
    }

    private HeaderProps getHeaderPropsFromContainerWith(PaymentResult paymentResult, Instruction instruction, PaymentResultScreenPreference preference) {
        PaymentResultContainer container = getContainer();

        PaymentResultProps paymentResultProps = new PaymentResultProps.Builder()
                .setPaymentResult(paymentResult)
                .setInstruction(instruction)
                .setPaymentResultScreenPreference(preference)
                .build();
        container.setProps(paymentResultProps);
        return container.getHeaderComponent().props;
    }

    private HeaderProps getHeaderPropsFromContainerWith(PaymentResult paymentResult, PaymentResultScreenPreference preference) {
        PaymentResultContainer container = getContainer();

        PaymentResultProps paymentResultProps = new PaymentResultProps.Builder()
                .setPaymentResult(paymentResult)
                .setPaymentResultScreenPreference(preference)
                .build();
        container.setProps(paymentResultProps);
        return container.getHeaderComponent().props;
    }

    private MockedProvider getMockedProvider() {
        return new MockedProvider();
    }

    private PaymentResultContainer getContainer() {
        MockedActionDispatcher actionDispatcher = new MockedActionDispatcher();
        MockedProvider provider = getMockedProvider();

        return new PaymentResultContainer(actionDispatcher, provider);
    }


    private class MockedActionDispatcher implements ActionDispatcher {

        @Override
        public void dispatch(Action action) {

        }
    }

    private class MockedProvider implements PaymentResultProvider {

        private final static String APPROVED_TITLE = "approved title";
        private final static String PENDING_TITLE = "pending title";
        private final static String REJECTED_OTHER_REASON_TITLE = "rejected other reason title";
        private final static String REJECTED_INSUFFICIENT_AMOUNT_TITLE = "rejected insufficient amount title";
        private final static String REJECTED_BAD_FILLED_TITLE = "rejected bad filled title";
        private final static String REJECTED_CALL_FOR_AUTH_TITLE = "rejected call for auth title";
        private final static String EMPTY_TITLE = "empty title";
        private final static String PENDING_LABEL = "pending label";
        private final static String REJECTION_LABEL = "rejection label";

        @Override
        public void getInstructionsAsync(Long paymentId, String paymentTypeId, OnResourcesRetrievedCallback<Instructions> onResourcesRetrievedCallback) {

        }

        @Override
        public String getStandardErrorMessage() {
            return null;
        }

        @Override
        public String getApprovedTitle() {
            return APPROVED_TITLE;
        }

        @Override
        public String getPendingTitle() {
            return PENDING_TITLE;
        }

        @Override
        public String getRejectedOtherReasonTitle(String paymentMethodName) {
            return REJECTED_OTHER_REASON_TITLE;
        }

        @Override
        public String getRejectedInsufficientAmountTitle(String paymentMethodName) {
            return REJECTED_INSUFFICIENT_AMOUNT_TITLE;
        }

        @Override
        public String getRejectedDuplicatedPaymentTitle(String paymentMethodName) {
            return null;
        }

        @Override
        public String getRejectedCardDisabledTitle(String paymentMethodName) {
            return null;
        }

        @Override
        public String getRejectedBadFilledCardTitle(String paymentMethodName) {
            return REJECTED_BAD_FILLED_TITLE;
        }

        @Override
        public String getRejectedBadFilledCardTitle() {
            return null;
        }

        @Override
        public String getRejectedHighRiskTitle() {
            return null;
        }

        @Override
        public String getRejectedMaxAttemptsTitle() {
            return null;
        }

        @Override
        public String getRejectedInsufficientDataTitle() {
            return null;
        }

        @Override
        public String getRejectedBadFilledOther() {
            return null;
        }

        @Override
        public String getRejectedCallForAuthorizeTitle() {
            return REJECTED_CALL_FOR_AUTH_TITLE;
        }

        @Override
        public String getEmptyText() {
            return EMPTY_TITLE;
        }

        @Override
        public String getPendingLabel() {
            return PENDING_LABEL;
        }

        @Override
        public String getRejectionLabel() {
            return REJECTION_LABEL;
        }

        @Override
        public String getCancelPayment() {
            return null;
        }

        @Override
        public String getContinueShopping() {
            return null;
        }

        @Override
        public String getExitButtonDefaultText() {
            return null;
        }

        @Override
        public String getRecoverPayment() {
            return null;
        }

        @Override
        public String getChangePaymentMethodLabel() {
            return null;
        }

        @Override
        public String getCardEnabled() {
            return null;
        }
    }
}
