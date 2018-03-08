package com.mercadopago.tracker;

import com.mercadopago.model.Card;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearch;

import java.util.List;

/**
 * Created by marlanti on 3/12/18.
 */

public class TrackingFormatter {

    private static final int MAX_LENGTH = 3000;

    public static String getFormattedPaymentMethodsForTracking(PaymentMethodSearch paymentMethodSearch) {
        List<PaymentMethod> paymentMethods = paymentMethodSearch.getPaymentMethods();
        List<Card> savedCards = null;//paymentMethodSearch.getCards();

        String internPrefix = ":";
        String externPrefix = "";

        StringBuilder formatted = new StringBuilder(MAX_LENGTH);

        if (paymentMethods != null) {
            for (PaymentMethod p : paymentMethods) {
                formatted.append(externPrefix);
                formatted.append(p.getId());
                formatted.append(internPrefix);
                formatted.append(p.getPaymentTypeId());
                externPrefix = "|";
            }
        }

        if (savedCards != null) {
            for (Card card : savedCards) {
                PaymentMethod p = card.getPaymentMethod();
                if (p != null) {
                    formatted.append(externPrefix);
                    formatted.append(p.getId());
                    formatted.append(internPrefix);
                    formatted.append(p.getPaymentTypeId());
                    formatted.append(internPrefix);
                    formatted.append(card.getId());
                    externPrefix = "|";
                }
            }
        }

        return formatted.toString();
    }
}
