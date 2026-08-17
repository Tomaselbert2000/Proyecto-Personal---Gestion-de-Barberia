package com.presentation.support.format;

import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.constants.StringResource.StringFormat.PRICE_FORMAT;

public class PriceFormatter {

    public static String format(Double price) {

        return CURRENCY_STRING_ARG + String.format(PRICE_FORMAT, price);
    }

    public static String formatPriceAsString(Double price) {

        return format(price);
    }
}
