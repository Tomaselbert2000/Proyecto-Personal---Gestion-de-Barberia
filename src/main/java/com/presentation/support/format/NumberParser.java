package com.presentation.support.format;

import com.exceptions.barberservice.BlankBarberServicePriceException;

public final class NumberParser {

    private NumberParser() {
    }

    public static Double parseTextToDouble(String text, Double defaultValue) {

        if (text == null || text.isBlank()) return defaultValue;

        return Double.valueOf(text);
    }

    public static Integer parseTextToInteger(String text, Integer defaultValue) {

        if (text == null || text.isBlank()) return defaultValue;

        return Integer.valueOf(text);
    }

    public static Double parsePercentageFraction(String text, Double defaultValue) {

        if (text == null || text.isBlank()) return defaultValue;

        return Double.parseDouble(text) / 100;
    }

    public static Double parsePrice(String text) {

        if (text == null || text.isBlank()) throw new BlankBarberServicePriceException();

        return Double.valueOf(text);
    }
}
