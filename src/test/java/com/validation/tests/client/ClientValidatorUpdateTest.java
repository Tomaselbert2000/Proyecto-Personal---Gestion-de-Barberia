package com.validation.tests.client;

import com.barbershop.dto.client.ClientUpdateDTO;
import com.barbershop.exceptions.client.*;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.client.ClientValidator;
import com.validation.common.ValidatorUpdateTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.ClientTestDataset.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ClientValidatorUpdateTest implements ValidatorUpdateTestFunctions {

    private final Validator validatorEngine = generateValidatorEngine();
    private final ClientValidator validator = new ClientValidator(validatorEngine);

    private ClientUpdateDTO updateDTO;

    @BeforeEach
    public void init() {

        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con datos correctos, la validación deberá ser exitosa y no arrojará excepción")
    void givenUpdateDTO_WithValidData_ThenDoesNotThrowAnything() {

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        updateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización no NULL, pero con todos sus campos individuales NULL, la validación deberá ser exitosa conservando todos los valores anteriores")
    void givenAllNullAttributes_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con DNI igual a NULL, la validación deberá ser exitosa conservando el valor anterior")
    void givenNullNationalIDCardNumber_WhenUpdating_ThenValidationShouldBeSuccessfull() {

        updateDTO.setNationalIdentityCardNumber(null);

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankNationalIDCardNumber_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setNationalIdentityCardNumber("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankFirstName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setFirstName("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankLastName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setLastName("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un email en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankEmail_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setEmail("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de teléfonos en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenEmptyPhoneList_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setPhoneNumbersList(EMPTY_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI con caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidNationalIDCardNumber_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setNationalIdentityCardNumber(INVALID_NATIONAL_ID_CARD_NUMBER);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre con caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidFirstName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setFirstName(INVALID_FIRST_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido con caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidLastName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setLastName(INVALID_LAST_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un email con caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidEmail_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setEmail(INVALID_EMAIL);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI que no cumpla límites de longitud, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNationalIDCardNumberWithInvalidLength_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setNationalIdentityCardNumber(INVALID_LENGTH_NATIONAL_ID_CARD_NUMBER);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenFirstNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setFirstName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setLastName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenFirstNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setFirstName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setLastName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de teléfonos que contenga 1 o más valores inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidPhoneNumberList_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setPhoneNumbersList(INVALID_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de teléfonos que contenga 1 o más valores duplicados, la validación deberá fallar y arrojará DuplicatedPhoneInListException")
    void givenPhoneNumberListWithDuplicatedValues_WhenUpdating_ThenThrows_DuplicatedPhoneInListException() {

        updateDTO.setPhoneNumbersList(PHONE_LIST_WITH_DUPLICATED_VALUES);

        assertThrows(DuplicatedPhoneInListException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de teléfonos que contenga 1 o más valores que no cumplan límites de longitud" +
            ", la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPhoneListWithInvalidLength_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setPhoneNumbersList(INVALID_LENGTH_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un string de notas opcionales que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenOptionalNotesTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setOptionalNotes(OPTIONAL_NOTES_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    public void setupUpdateDTO() {

        updateDTO = ClientUpdateDTO.builder()
                .nationalIdentityCardNumber(NATIONAL_ID_CARD_NUMBER)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .phoneNumbersList(PHONE_LIST)
                .optionalNotes(OPTIONAL_NOTES)
                .build();
    }

    public void validateForUpdate() {

        validator.validateDTO(updateDTO);
    }

    public void setAllFieldsOnNull() {

        updateDTO.setNationalIdentityCardNumber(null);
        updateDTO.setFirstName(null);
        updateDTO.setLastName(null);
        updateDTO.setEmail(null);
        updateDTO.setPhoneNumbersList(null);
        updateDTO.setOptionalNotes(null);
    }
}