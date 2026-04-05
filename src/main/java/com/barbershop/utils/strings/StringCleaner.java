package com.barbershop.utils.strings;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.util.List;
import java.util.stream.Collectors;

public class StringCleaner {

    public static String formatAsProperName(String properName) {

        if (properName != null) return WordUtils.capitalizeFully(properName.trim());

        return null;
    }

    public static String formatAsSentence(String sentence) {

        if (sentence != null) return StringUtils.capitalize(sentence.trim());

        return null;
    }

    public static List<String> trimList(List<String> stringList) {

        if (stringList != null) return stringList.stream().map(String::trim).collect(Collectors.toList());

        return null;
    }
}
