package com.validation.tests.paymentmethod;

import com.barbershop.dto.payment.PaymentMethodCreationDTO;
import com.barbershop.enums.PaymentMethodModifierType;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.exceptions.paymentmethod.*;
import com.barbershop.utils.strings.RegexPatterns;
import com.barbershop.validation.payment.PaymentMethodValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodValidatorCreationTest {

    private final PaymentMethodValidator validator = new PaymentMethodValidator();

    private PaymentMethodCreationDTO creationDTO;

    private static final String NAME = "Mercado Pago";
    private static final String DESCRIPTION = "Pasarela de pagos de Mercado Libre SRL";
    private static final PaymentMethodModifierType MODIFIER_TYPE = PaymentMethodModifierType.RECARGO;
    private static final Double PRICE_MODIFIER = 0.06;

    private static final String INVALID_NAME = "M3rc@d0 PAgø";
    private static final String NAME_TOO_SHORT = "xyz";
    private static final String NAME_TOO_LONG = "mUGjjPTHbRqvudfetqPvuCYdAuieHfUgawyvuynycXwMnJpVnmJHKHFZQAfqhUrYEFfkTgWrxXJNgnZwJnFpdCgRLZDAweVnpDJiWkxBXRdDMeDduCuHLzSf";
    private static final String DESCRIPTION_TOO_LONG = RegexPatterns.returnLoremIpsum();

    @BeforeEach
    void init() {

        setupCreationDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación fallará y arrojará NullDTOException")
    void givenNullDTO_WhenCreating_ThenThrows_NullDTOException() {

        creationDTO = null;

        assertThrows(NullDTOException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre NULL, la validación deberá fallar y arrojará NullPaymentMethodInputDataException")
    void givenNullName_WhenCreating_ThenThrows_NullPaymentMethodInputDataException() {

        creationDTO.setName(null);

        assertThrows(NullPaymentMethodInputDataException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción NULL, la validación deberá fallar y arrojará NullPaymentMethodInputDataException")
    void givenNullDescription_WhenCreating_ThenThrows_NullPaymentMethodInputDataException() {

        creationDTO.setDescription(null);

        assertThrows(NullPaymentMethodInputDataException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un objeto modificador de precio NULL, la validación deberá fallar y arrojará NullPaymentMethodInputDataException")
    void givenNullPriceModifierType_WhenCreating_ThenThrows_NullPaymentMethodInputDataException() {

        creationDTO.setPriceModifierType(null);

        assertThrows(NullPaymentMethodInputDataException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de modificador de precio NULL, la validación deberá fallar y arrojará NullPaymentMethodInputDataException")
    void givenNullPriceModifierValue_WhenCreating_ThenThrows_NullPaymentMethodInputDataException() {

        creationDTO.setPriceModifier(null);

        assertThrows(NullPaymentMethodInputDataException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con nombre en blanco, la validación deberá fallar y arrojará BlankPaymentMethodNameException")
    void givenBlankName_WhenCreating_ThenThrows_BlankPaymentMethodNameException() {

        creationDTO.setName("");

        assertThrows(BlankPaymentMethodNameException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto por caractéres inválidos, la validación deberá fallar y arrojará InvalidPaymentMethodNameException")
    void givenInvalidName_WhenCreating_ThenThrows_InvalidPaymentMethodNameException() {

        creationDTO.setName(INVALID_NAME);

        assertThrows(InvalidPaymentMethodNameException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankDescription_WhenCreating_ThenDoesNotThrowAnything() {

        creationDTO.setDescription("");

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla longitud mínima, la validación deberá fallar y arrojará InvalidPaymentMethodNameLengthException")
    void givenNameTooShort_WhenCreating_ThenThrows_InvalidPaymentMethodNameLengthException() {

        creationDTO.setName(NAME_TOO_SHORT);

        assertThrows(InvalidPaymentMethodNameLengthException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla longitud máxima, la validación deberá fallar y arrojará InvalidPaymentMethodNameLengthException")
    void givenNameTooLong_WhenCreating_ThenThrows_InvalidPaymentMethodNameLengthException() {

        creationDTO.setName(NAME_TOO_LONG);

        assertThrows(InvalidPaymentMethodNameLengthException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción que no cumpla la longitud máxima, la validación deberá fallar y arrojará InvalidPaymentMethodDescriptionLengthException")
    void givenDescriptionTooLong_WhenCreating_ThenThrows_InvalidPaymentMethodDescriptionLengthException() {

        creationDTO.setDescription(DESCRIPTION_TOO_LONG);

        assertThrows(InvalidPaymentMethodDescriptionLengthException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor modificador de precio menor que 0.0, la validación deberá fallar y arrojará InvalidDecimalValueException")
    void givenPriceModifierValueLowerThanZero_WhenCreating_ThenThrows_InvalidDecimalValueException() {

        creationDTO.setPriceModifier(-0.05);

        assertThrows(InvalidDecimalValueException.class, this::validateForCreation);
    }

    private void setupCreationDTO() {

        creationDTO = PaymentMethodCreationDTO.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .priceModifierType(MODIFIER_TYPE)
                .priceModifier(PRICE_MODIFIER)
                .build();
    }

    private void validateForCreation() {

        validator.validateForCreation(creationDTO);
    }
}
