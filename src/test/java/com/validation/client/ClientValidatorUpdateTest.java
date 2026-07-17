package com.validation.client;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.client.ClientUpdateDTO;
import com.exceptions.client.DuplicatedPhoneInListException;
import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.ClientTestDataFactory.buildValidClientUpdateDTO;
import static com.test_constant.ClientTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientValidatorUpdateTest extends BaseValidatorTest<ClientValidator, ClientUpdateDTO> {

    @Test
    @DisplayName("Dado un DTO de actualización con datos correctos, la validación deberá ser exitosa y no arrojará excepción")
    void givenUpdateDTO_WithValidData_ThenDoesNotThrowAnything() {

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización no NULL, pero con todos sus campos individuales NULL, la validación deberá ser exitosa conservando todos los valores anteriores")
    void givenAllNullAttributes_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con DNI igual a NULL, la validación deberá ser exitosa conservando el valor anterior")
    void givenNullNationalIDCardNumber_WhenUpdating_ThenValidationShouldBeSuccessfull() {

        inputDTO.setNationalIdentityCardNumber(null);

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankNationalIDCardNumber_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setNationalIdentityCardNumber("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankFirstName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankLastName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un email en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankEmail_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setEmail("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de teléfonos en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenEmptyPhoneList_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhoneNumbersList(EMPTY_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI con caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidNationalIDCardNumber_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setNationalIdentityCardNumber(INVALID_NATIONAL_ID_CARD_NUMBER);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre con caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidFirstName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(INVALID_FIRST_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido con caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidLastName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(INVALID_LAST_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un email con caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidEmail_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setEmail(INVALID_EMAIL);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI que no cumpla límites de longitud, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNationalIDCardNumberWithInvalidLength_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setNationalIdentityCardNumber(INVALID_LENGTH_NATIONAL_ID_CARD_NUMBER);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenFirstNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenFirstNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de teléfonos que contenga 1 o más valores inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidPhoneNumberList_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhoneNumbersList(INVALID_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de teléfonos que contenga 1 o más valores duplicados, la validación deberá fallar y arrojará DuplicatedPhoneInListException")
    void givenPhoneNumberListWithDuplicatedValues_WhenUpdating_ThenThrows_DuplicatedPhoneInListException() {

        inputDTO.setPhoneNumbersList(PHONE_LIST_WITH_DUPLICATED_VALUES);

        assertThrows(DuplicatedPhoneInListException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de teléfonos que contenga 1 o más valores que no cumplan límites de longitud" +
            ", la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPhoneListWithInvalidLength_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhoneNumbersList(INVALID_LENGTH_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un string de notas opcionales que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenOptionalNotesTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setOptionalNotes(OPTIONAL_NOTES_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    public void setAllFieldsOnNull() {

        inputDTO.setNationalIdentityCardNumber(null);
        inputDTO.setFirstName(null);
        inputDTO.setLastName(null);
        inputDTO.setEmail(null);
        inputDTO.setPhoneNumbersList(null);
        inputDTO.setOptionalNotes(null);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidClientUpdateDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new ClientValidator(validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}