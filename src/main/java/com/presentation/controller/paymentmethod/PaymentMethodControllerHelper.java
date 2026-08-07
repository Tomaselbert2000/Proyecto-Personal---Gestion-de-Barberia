package com.presentation.controller.paymentmethod;

import static com.presentation.constants.ControllerConstants.NULL_NUMERIC_INPUT_VALUE;

public final class PaymentMethodControllerHelper {

    public static double convertPriceStringToDouble(String priceString) {

        if (priceString == null || priceString.isBlank()) return NULL_NUMERIC_INPUT_VALUE;

        return Double.parseDouble(priceString);
    }
}
