package com.presentation.controller.paymentmethod;

public final class PaymentMethodControllerHelper {

    private static final double PRICE_MODIFIER_VALUE_IF_NULL_OR_EMPTY = -1.0;

    public static double convertPriceStringToDouble(String priceString) {

        if (priceString == null || priceString.isBlank()) return PRICE_MODIFIER_VALUE_IF_NULL_OR_EMPTY;

        return Double.parseDouble(priceString);
    }
}
