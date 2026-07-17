package com.validation.paymentmethod;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.payment.PaymentMethodUpdateDTO;
import com.exceptions.common.NullDTOException;
import com.validation.payment.PaymentMethodValidator;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethodUpdateDTO;
import static com.test_constant.PaymentMethodTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentMethodValidatorUpdateTest extends BaseValidatorTest<PaymentMethodValidator, PaymentMethodUpdateDTO> {

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus campos NULL, la validación deberá ser exitosa y la entidad no persistirá cambios")
    void givenDTO_WillAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsNull();

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación deberá fallar y arrojará BlankPaymentMethodNameException")
    void givenBlankName_WhenUpdating_ThenThrows_BlankPaymentMethodNameException() {

        inputDTO.setNewName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto por caractéres inválidos, la validación deberá fallar y arrojará InvalidPaymentMethodNameException")
    void givenInvalidName_WhenUpdating_ThenThrows_InvalidPaymentMethodNameException() {

        inputDTO.setNewName(PAYMENT_METHOD_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla longitud mínima, la validación deberá fallar y arrojará InvalidPaymentMethodNameLengthException")
    void givenNameTooShort_WhenUpdating_ThenThrows_InvalidPaymentMethodNameLengthException() {

        inputDTO.setNewName(PAYMENT_METHOD_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud máxima, la validación deberá fallar y arrojará InvalidPaymentMethodNameLengthException")
    void givenNameTooLong_WhenUpdating_ThenThrows_InvalidPaymentMethodNameLengthException() {

        inputDTO.setNewName(PAYMENT_METHOD_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankDescription_WhenUpdating_ThenDoesNotThrowAnything() {

        inputDTO.setNewDescription("");

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción que no cumpla longitud máxima, la validación deberá fallar y arrojárá InvalidPaymentMethodDescriptionLengthException")
    void givenDescriptionTooLong_WhenUpdating_ThenThrows_InvalidPaymentMethodDescriptionLengthException() {

        inputDTO.setNewDescription(PAYMENT_METHOD_DESCRIPTION_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor modificador de precio menor que 0.0, la validación deberá fallar y arrojará InvalidDecimalValueException")
    void givenPriceModifierValueLowerThanZero_WhenUpdating_ThenThrows_InvalidDecimalValueException() {

        inputDTO.setPriceModifier(PAYMENT_METHOD_NEGATIVE_PRICE_MODIFIER_VALUE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    private void setAllFieldsNull() {

        inputDTO.setNewName(null);
        inputDTO.setNewDescription(null);
        inputDTO.setIsActive(null);
        inputDTO.setNewModifierType(null);
        inputDTO.setPriceModifier(null);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidPaymentMethodUpdateDTO();
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
