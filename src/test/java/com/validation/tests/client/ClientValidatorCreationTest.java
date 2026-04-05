package com.validation.tests.client;

import com.barbershop.dto.client.ClientCreationDTO;
import com.barbershop.exceptions.client.*;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.client.ClientValidator;
import com.validation.common.ValidatorCreationTestFunctions;
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
public class ClientValidatorCreationTest implements ValidatorCreationTestFunctions {

    private ClientCreationDTO creationDTO;

    private final Validator validatorEngine = generateValidatorEngine();
    private final ClientValidator validator = new ClientValidator(validatorEngine);

    @BeforeEach
    public void init() {

        setupCreationDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, la actualización deberá ser exitosa y no arrojará excepción")
    void givenValidDTO_WhenCreating_ThenDoesNotThrowAnything() {

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullCreationDTO_WhenCreating_ThenThrows_NullDTOException() {

        creationDTO = null;

        assertThrows(NullDTOException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con DNI NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNull_NationalIDCardNumber_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setNationalIdentityCardNumber(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con nombre en NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con apellido en NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con email en NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullEmail_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setEmail(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con lista de teléfonos en NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPhoneList_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPhoneNumbersList(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una nota opcional NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullOptionalNotesNULL_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setOptionalNotes(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankNationalIDCardNumber_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setNationalIdentityCardNumber("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankEmail_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setEmail("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI que no cumpla límites de longitud, la validación deberá fallar y arrojara ConstraintViolationException")
    void givenNationalIDCardNumberWithInvalidLength_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setNationalIdentityCardNumber(INVALID_LENGTH_NATIONAL_ID_CARD_NUMBER);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud mínima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenClientFirstNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido que no cumpla la longitud mínima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenClientLastNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud máxima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenClientFirstNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido que no cumpla la longitud máxima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenClientLastNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI compuesto por caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidNationalIDCardNumberException_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setNationalIdentityCardNumber(INVALID_NATIONAL_ID_CARD_NUMBER);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto por caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName(INVALID_FIRST_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido compuesto por caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName(INVALID_LAST_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email compuesto por caracteres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidEmail_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setEmail(INVALID_EMAIL);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de teléfonos que contenga al menos 1 inválido, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidPhone_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPhoneNumbersList(INVALID_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de teléfonos vacía, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenEmptyPhoneNumberList_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPhoneNumbersList(EMPTY_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de teléfonos que contenga valores duplicados, la validación deberá fallar y arrojará DuplicatedPhoneInListException")
    void givenPhoneListWithDuplicatedValues_WhenCreating_ThenThrows_DuplicatedPhoneNumberInListException() {

        creationDTO.setPhoneNumbersList(PHONE_LIST_WITH_DUPLICATED_VALUES);

        assertThrows(DuplicatedPhoneInListException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de teléfonos que contenga 1 o más valores que no cumplan límites de longitud" +
            ", la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPhoneListWithInvalidLength_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPhoneNumbersList(INVALID_LENGTH_PHONE_LIST);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una nota opcional que supere la longitud máxima, arrojará ConstraintViolationException")
    void givenOptionalNotesTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setOptionalNotes(OPTIONAL_NOTES_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un campo de notas opcionales en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankOptionalNotes_WhenCreating_ThenDoesNotThrowAnything() {

        creationDTO.setOptionalNotes("");

        assertDoesNotThrow(this::validateForCreation);
    }

    public void setupCreationDTO() {

        creationDTO = ClientCreationDTO.builder()
                .nationalIdentityCardNumber(NATIONAL_ID_CARD_NUMBER)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .phoneNumbersList(PHONE_LIST)
                .optionalNotes(OPTIONAL_NOTES)
                .build();
    }

    public void validateForCreation() {

        validator.validateDTO(creationDTO);
    }
}
