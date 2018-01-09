package com.mercadopago.reviewandconfirm;

import com.mercadopago.constants.Sites;
import com.mercadopago.model.Item;
import com.mercadopago.paymentresult.formatter.BodyAmountFormatter;
import com.mercadopago.reviewconfirm.components.ReviewItem;
import com.mercadopago.reviewconfirm.props.ItemProps;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by vaserber on 09/01/2018.
 */

public class ReviewItemTest {

    public final static String ITEM_TITLE = "Title";
    public final static String ITEM_DESCRIPTION = "Description";
    public final static String ITEM_PICTURE_URL = "https://imgur.com/a/0QITs";

    @Test
    public void testItemWithImageUrl() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item(ITEM_DESCRIPTION, 1, itemAmount, ITEM_PICTURE_URL);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertEquals(component.getImageUrl(), ITEM_PICTURE_URL);
    }

    @Test
    public void testItemWithoutImageUrl() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item(ITEM_DESCRIPTION, 1, itemAmount);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertNull(component.getImageUrl());
    }

    @Test
    public void testItemTitleWithNoTitle() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item(ITEM_DESCRIPTION, 1, itemAmount, ITEM_PICTURE_URL);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertNull(component.getTitle());
    }

    @Test
    public void testItemTitleWithTitle() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item(ITEM_DESCRIPTION, 1, itemAmount, ITEM_PICTURE_URL);
        item.setTitle(ITEM_TITLE);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertEquals(component.getTitle(), ITEM_TITLE);
    }

    @Test
    public void testItemTitleWithDescription() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item(ITEM_DESCRIPTION, 1, itemAmount, ITEM_PICTURE_URL);
        item.setTitle(ITEM_TITLE);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertEquals(component.getDescription(), ITEM_DESCRIPTION);
    }

    @Test
    public void testItemTitleWithEmptyDescription() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item("", 1, itemAmount, ITEM_PICTURE_URL);
        item.setTitle(ITEM_TITLE);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertEquals(component.getDescription(), "");
    }

    @Test
    public void testItemTitleWithQuantity() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item("", 3, itemAmount, ITEM_PICTURE_URL);
        item.setTitle(ITEM_TITLE);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertEquals(component.getQuantity(), 3);
    }

    @Test
    public void testItemTitleWithEmptyQuantity() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item("",  itemAmount, ITEM_PICTURE_URL);
        item.setTitle(ITEM_TITLE);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertEquals(component.getQuantity(), 1);
    }

    @Test
    public void testItemTitleWithUnitPrice() {
        BigDecimal itemAmount = new BigDecimal(129);
        Item item = new Item("", 1, itemAmount, ITEM_PICTURE_URL);
        item.setTitle(ITEM_TITLE);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertEquals(component.getAmount(), amountFormatter.formatNumber(itemAmount));
    }

    @Test
    public void testItemTitleWithInvalidUnitPrice() {
        BigDecimal itemAmount = null;
        Item item = new Item("", 1, itemAmount, ITEM_PICTURE_URL);
        item.setTitle(ITEM_TITLE);
        BodyAmountFormatter amountFormatter = new BodyAmountFormatter(Sites.ARGENTINA.getCurrencyId(), itemAmount);
        ReviewItem component = getItemComponent(item, amountFormatter);
        Assert.assertEquals(component.getAmount(), "");
    }

    private ReviewItem getItemComponent(Item item, BodyAmountFormatter amountFormat) {

        final ItemProps itemProps = new ItemProps.Builder()
                .setItem(item)
                .setAmountFormatter(amountFormat)
                .build();
        final ReviewItem component = new ReviewItem(itemProps);
        return component;
    }
}
