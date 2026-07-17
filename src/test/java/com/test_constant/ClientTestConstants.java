package com.test_constant;

import com.utils.strings.RegexPatterns;

import java.time.LocalDate;
import java.util.List;

import static com.service.helper.ServiceTestVerificationHelper.addTabs;
import static com.service.helper.ServiceTestVerificationHelper.returnAsLowercase;
import static com.test_constant.ClientTestConstants.CreationValidData.*;

public final class ClientTestConstants {

    private ClientTestConstants() {
    }

    public static final class CreationValidData {

        public static final String NATIONAL_ID_CARD_NUMBER = "1234567";
        public static final String FIRST_NAME = "Tomas Gabriel";
        public static final String LAST_NAME = "Elbert";
        public static final String EMAIL = "tomas@gmail.com";
        public static final List<String> PHONE_LIST = List.of("1122334455", "2244668800");
        public static final String OPTIONAL_NOTES = "El cliente presenta alergias y/o condiciones médicas. Consultar antes de prestar servicios.";
    }

    public static final class UpdateValidData {

        public static final String NEW_NATIONAL_ID_CARD_NUMBER = "7654321";
        public static final String NEW_FIRST_NAME = "Juan";
        public static final String NEW_LAST_NAME = "Perez";
        public static final String NEW_EMAIL = "juanperez@gmail.com";
        public static final List<String> NEW_PHONE_LIST = List.of("9988776655");
        public static final String NEW_OPTIONAL_NOTES = "El cliente no presenta ningun tipo de alergia";
    }

    public static final class MapperData {

        public static final Long CLIENT_ID = 1L;
        public static final LocalDate REGISTRATION_DATE = LocalDate.of(2026, 1, 1);
        public static final String NATIONAL_ID_CARD_NUMBER_WITH_SPACES = addTabs(NATIONAL_ID_CARD_NUMBER);
        public static final String FIRST_NAME_WITH_SPACES = addTabs(FIRST_NAME);
        public static final String LAST_NAME_WITH_SPACES = addTabs(LAST_NAME);
        public static final String EMAIL_WITH_SPACES = addTabs(EMAIL);
        public static final String OPTIONAL_NOTES_WITH_SPACES = addTabs(OPTIONAL_NOTES);
        public static final String PHONE_WITH_SPACES = addTabs(PHONE_LIST.getFirst());
        public static final List<String> PHONE_LIST_THAT_CONTAINS_PHONE_WITH_SPACES = List.of(PHONE_WITH_SPACES);

        public static final String LOWERCASE_FIRST_NAME = returnAsLowercase(FIRST_NAME);
        public static final String LOWERCASE_LAST_NAME = returnAsLowercase(LAST_NAME);
    }

    public static final class InvalidData {

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
}
