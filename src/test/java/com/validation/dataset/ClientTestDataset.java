package com.validation.dataset;

import com.barbershop.utils.strings.RegexPatterns;

import java.util.List;

public class ClientTestDataset {

    public static final String NATIONAL_ID_CARD_NUMBER = "1234567";
    public static final String FIRST_NAME = "Tomas Gabriel";
    public static final String LAST_NAME = "Elbert";
    public static final String EMAIL = "tomas@gmail.com";
    public static final List<String> PHONE_LIST = List.of("1122334455", "2244668800");
    public static final String OPTIONAL_NOTES = "El cliente presenta alergias y/o condiciones médicas. Consultar antes de prestar servicios.";

    public static final String INVALID_NATIONAL_ID_CARD_NUMBER = "ABC1234";
    public static final String INVALID_LENGTH_NATIONAL_ID_CARD_NUMBER = "1234567890";
    public static final String INVALID_FIRST_NAME = "T0m@s gAbr1el";
    public static final String INVALID_LAST_NAME = "E1berT";
    public static final String INVALID_EMAIL = "qwertw12e2112@ſßæð@ſy_@asdsa.sadq";
    public static final List<String> INVALID_PHONE_LIST = List.of("11AA33BB55", "1122334455");
    public static final List<String> EMPTY_PHONE_LIST = List.of();
    public static final List<String> PHONE_LIST_WITH_DUPLICATED_VALUES = List.of("1122334455", "1122334455");
    public static final List<String> INVALID_LENGTH_PHONE_LIST = List.of("11223344556677889900");
    public static final String NAME_TOO_SHORT = "Abc";
    public static final String NAME_TOO_LONG = "FUMzLuBzrUtcycKBpbjdyCcAaNRUZYhnwGqguTyHidnDShRPTKgfcQbHvHjUNWAzHGvvaxcbDWaTLvZGidmXAABudxxcxRBFeGkLdkPPpTrVUvgpFnxrFnGj";
    public static final String OPTIONAL_NOTES_TOO_LONG = RegexPatterns.returnLoremIpsum();
}
