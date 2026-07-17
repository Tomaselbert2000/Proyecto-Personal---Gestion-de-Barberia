package com.test_constant;

import com.enums.PaymentMethodModifierType;
import com.utils.strings.RegexPatterns;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.service.helper.ServiceTestVerificationHelper.addTabs;
import static com.service.helper.ServiceTestVerificationHelper.returnAsLowercase;
import static com.test_constant.PaymentMethodTestConstants.CreationValidData.PAYMENT_METHOD_NAME;

public final class PaymentMethodTestConstants {

    public static final class CreationValidData {

        public static final Long PAYMENT_METHOD_ID = 1L;
        public static final LocalDate REGISTERED_DATE = LocalDate.of(2026, 1, 1);
        public static final String PAYMENT_METHOD_NAME = "Mercado Pago";
        public static final String PAYMENT_METHOD_DESCRIPTION = "Pasarela de pagos de Mercado Libre SRL";
        public static final PaymentMethodModifierType PAYMENT_METHOD_MODIFIER_TYPE = PaymentMethodModifierType.RECARGO;
        public static final Double PAYMENT_METHOD_PRICE_MODIFIER_VALUE = 0.06;
    }

    public static final class UpdateValidData {

        public static final String UPDATED_PAYMENT_METHOD_NAME = "PayPal";
        public static final String UPDATED_PAYMENT_METHOD_DESCRIPTION = "Pasarela de pagos global";
        public static final PaymentMethodModifierType UPDATED_PAYMENT_METHOD_MODIFIER_TYPE = PaymentMethodModifierType.DESCUENTO;
        public static final Double UPDATED_PRICE_MODIFIER_VALUE = 0.10;
        public static final Boolean PAYMENT_METHOD_IS_ACTIVE = true;
    }

    public static final class MapperData {

        public static final Clock PAYMENT_METHOD_TEST_CLOCK = Clock.fixed(Instant.parse("2026-01-01T15:00:00Z"), ZoneId.of("America/Argentina/Buenos_Aires"));
        public static final String PAYMENT_METHOD_NAME_WITH_SPACES = addTabs(PAYMENT_METHOD_NAME);
        public static final String LOWERCASE_PAYMENT_METHOD_NAME = returnAsLowercase(PAYMENT_METHOD_NAME);
    }

    public static final class InvalidData {

        public static final String PAYMENT_METHOD_INVALID_NAME = "M3rc@d0 PAgø";
        public static final String PAYMENT_METHOD_NAME_TOO_SHORT = "xyz";
        public static final String PAYMENT_METHOD_NAME_TOO_LONG = "mUGjjPTHbRqvudfetqPvuCYdAuieHfUgawyvuynycXwMnJpVnmJHKHFZQAfqhUrYEFfkTgWrxXJNgnZwJnFpdCgRLZDAweVnpDJiWkxBXRdDMeDduCuHLzSf";
        public static final String PAYMENT_METHOD_DESCRIPTION_TOO_LONG = RegexPatterns.returnLoremIpsum();
        public static final Double PAYMENT_METHOD_NEGATIVE_PRICE_MODIFIER_VALUE = -0.05;
    }
}
