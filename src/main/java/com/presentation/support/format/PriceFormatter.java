package com.presentation.support.format;

import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;

public class PriceFormatter {

    public static String formatPriceAsString(Double price){

        return CURRENCY_STRING_ARG + parseNumberValueToText(price);
    }
}
