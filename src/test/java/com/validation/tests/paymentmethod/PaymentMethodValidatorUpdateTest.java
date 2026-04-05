package com.validation.tests.paymentmethod;

import com.barbershop.dto.payment.PaymentMethodUpdateDTO;
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
public class PaymentMethodValidatorUpdateTest {

    private final PaymentMethodValidator validator = new PaymentMethodValidator();

    private PaymentMethodUpdateDTO updateDTO;

    private static final String NAME = "Mercado Pago";
    private static final String DESCRIPTION = "Pasarela de pagos de Mercado Libre SRL";
    private static final PaymentMethodModifierType MODIFIER_TYPE = PaymentMethodModifierType.RECARGO;
    private static final Double PRICE_MODIFIER = 0.06;

    private static final String INVALID_NAME = "M3rc@d0 PAgø";
    private static final String NAME_TOO_SHORT = "xyz";
    private static final String NAME_TOO_LONG = "mUGjjPTHbRqvudfetqPvuCYdAuieHfUgawyvuynycXwMnJpVnmJHKHFZQAfqhUrYEFfkTgWrxXJNgnZwJnFpdCgRLZDAweVnpDJiWkxBXRdDMeDduCuHLzSf";
    private static final String DESCRIPTION_TOO_LONG = RegexPatterns.returnLoremIpsum();
    private static final Boolean IS_ACTIVE = true;

    @BeforeEach
    void init() {

        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        updateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus campos NULL, la validación deberá ser exitosa y la entidad no persistirá cambios")
    void givenDTO_WillAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsNull();

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación deberá fallar y arrojará BlankPaymentMethodNameException")
    void givenBlankName_WhenUpdating_ThenThrows_BlankPaymentMethodNameException() {

        updateDTO.setNewName("");

        assertThrows(BlankPaymentMethodNameException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto por caractéres inválidos, la validación deberá fallar y arrojará InvalidPaymentMethodNameException")
    void givenInvalidName_WhenUpdating_ThenThrows_InvalidPaymentMethodNameException() {

        updateDTO.setNewName(INVALID_NAME);

        assertThrows(InvalidPaymentMethodNameException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla longitud mínima, la validación deberá fallar y arrojará InvalidPaymentMethodNameLengthException")
    void givenNameTooShort_WhenUpdating_ThenThrows_InvalidPaymentMethodNameLengthException() {

        updateDTO.setNewName(NAME_TOO_SHORT);

        assertThrows(InvalidPaymentMethodNameLengthException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud máxima, la validación deberá fallar y arrojará InvalidPaymentMethodNameLengthException")
    void givenNameTooLong_WhenUpdating_ThenThrows_InvalidPaymentMethodNameLengthException() {

        updateDTO.setNewName(NAME_TOO_LONG);

        assertThrows(InvalidPaymentMethodNameLengthException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankDescription_WhenUpdating_ThenDoesNotThrowAnything() {

        updateDTO.setNewDescription("");

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción que no cumpla longitud máxima, la validación deberá fallar y arrojárá InvalidPaymentMethodDescriptionLengthException")
    void givenDescriptionTooLong_WhenUpdating_ThenThrows_InvalidPaymentMethodDescriptionLengthException() {

        updateDTO.setNewDescription(DESCRIPTION_TOO_LONG);

        assertThrows(InvalidPaymentMethodDescriptionLengthException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor modificador de precio menor que 0.0, la validación deberá fallar y arrojará InvalidDecimalValueException")
    void givenPriceModifierValueLowerThanZero_WhenUpdating_ThenThrows_InvalidDecimalValueException() {

        updateDTO.setPriceModifier(-0.5);

        assertThrows(InvalidDecimalValueException.class, this::validateForUpdate);
    }

    private void setupUpdateDTO() {

        updateDTO = PaymentMethodUpdateDTO.builder()
                .newName(NAME)
                .newDescription(DESCRIPTION)
                .isActive(IS_ACTIVE)
                .newModifierType(MODIFIER_TYPE)
                .priceModifier(PRICE_MODIFIER)
                .build();
    }

    private void validateForUpdate() {

        validator.validateForUpdate(updateDTO);
    }

    private void setAllFieldsNull() {

        updateDTO.setNewName(null);
        updateDTO.setNewDescription(null);
        updateDTO.setIsActive(null);
        updateDTO.setNewModifierType(null);
        updateDTO.setPriceModifier(null);
    }
}
