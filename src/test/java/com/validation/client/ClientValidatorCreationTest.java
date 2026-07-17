package com.validation.client;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.client.ClientCreationDTO;
import com.exceptions.client.DuplicatedPhoneInListException;
import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.ClientTestDataFactory.buildValidClientCreationDTO;
import static com.test_constant.ClientTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientValidatorCreationTest extends BaseValidatorTest<ClientValidator, ClientCreationDTO> {

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, la actualización deberá ser exitosa y no arrojará excepción")
    void givenValidDTO_WhenCreating_ThenDoesNotThrowAnything() {

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullCreationDTO_WhenCreating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con DNI NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNull_NationalIDCardNumber_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setNationalIdentityCardNumber(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con nombre en NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con apellido en NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con email en NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullEmail_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setEmail(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con lista de teléfonos en NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPhoneList_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhoneNumbersList(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una nota opcional NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullOptionalNotesNULL_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setOptionalNotes(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankNationalIDCardNumber_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setNationalIdentityCardNumber("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankEmail_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setEmail("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI que no cumpla límites de longitud, la validación deberá fallar y arrojara ConstraintViolationException")
    void givenNationalIDCardNumberWithInvalidLength_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setNationalIdentityCardNumber(INVALID_LENGTH_NATIONAL_ID_CARD_NUMBER);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud mínima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenClientFirstNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido que no cumpla la longitud mínima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenClientLastNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud máxima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenClientFirstNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido que no cumpla la longitud máxima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenClientLastNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI compuesto por caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidNationalIDCardNumberException_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setNationalIdentityCardNumber(INVALID_NATIONAL_ID_CARD_NUMBER);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto por caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(INVALID_FIRST_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido compuesto por caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(INVALID_LAST_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email compuesto por caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidEmail_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setEmail(INVALID_EMAIL);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de teléfonos que contenga al menos 1 inválido, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidPhone_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhoneNumbersList(INVALID_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de teléfonos vacía, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenEmptyPhoneNumberList_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhoneNumbersList(EMPTY_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de teléfonos que contenga valores duplicados, la validación deberá fallar y arrojará DuplicatedPhoneInListException")
    void givenPhoneListWithDuplicatedValues_WhenCreating_ThenThrows_DuplicatedPhoneNumberInListException() {

        inputDTO.setPhoneNumbersList(PHONE_LIST_WITH_DUPLICATED_VALUES);

        assertThrows(DuplicatedPhoneInListException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de teléfonos que contenga 1 o más valores que no cumplan límites de longitud" +
            ", la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPhoneListWithInvalidLength_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhoneNumbersList(INVALID_LENGTH_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una nota opcional que supere la longitud máxima, arrojará ConstraintViolationException")
    void givenOptionalNotesTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setOptionalNotes(OPTIONAL_NOTES_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un campo de notas opcionales en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankOptionalNotes_WhenCreating_ThenDoesNotThrowAnything() {

        inputDTO.setOptionalNotes("");

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidClientCreationDTO();
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
