package com.validation.paymentmethod;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.payment.PaymentMethodCreationDTO;
import com.exceptions.common.NullDTOException;
import com.validation.payment.PaymentMethodValidator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethodCreationDTO;
import static com.test_constant.PaymentMethodTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentMethodValidatorCreationTest extends BaseValidatorTest<PaymentMethodValidator, PaymentMethodCreationDTO> {

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación fallará y arrojará NullDTOException")
    void givenNullDTO_WhenCreating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullDescription_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setDescription(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un objeto modificador de precio NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPriceModifierType_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPriceModifierType(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de modificador de precio NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPriceModifierValue_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPriceModifier(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto por caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PAYMENT_METHOD_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankDescription_WhenCreating_ThenDoesNotThrowAnything() {

        inputDTO.setDescription("");

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PAYMENT_METHOD_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PAYMENT_METHOD_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenDescriptionTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setDescription(PAYMENT_METHOD_DESCRIPTION_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor modificador de precio menor que 0.0, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPriceModifierValueLowerThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPriceModifier(PAYMENT_METHOD_NEGATIVE_PRICE_MODIFIER_VALUE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidPaymentMethodCreationDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new PaymentMethodValidator(validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}
