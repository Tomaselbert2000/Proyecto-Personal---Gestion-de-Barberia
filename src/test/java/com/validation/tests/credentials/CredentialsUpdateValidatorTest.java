package com.validation.tests.credentials;

import com.barbershop.dto.credentials_management.CredentialsUpdateDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.exceptions.credentials.PasswordMismatchException;
import com.barbershop.validation.credentials.CredentialsUpdateValidator;
import com.validation.common.ValidatorUpdateTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.CredentialUpdateDataset.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CredentialsUpdateValidatorTest implements ValidatorUpdateTestFunctions {

    private CredentialsUpdateDTO credentialsUpdateDTO;

    private final Validator validatorEngine = generateValidatorEngine();

    private final CredentialsUpdateValidator credentialValidator = new CredentialsUpdateValidator(validatorEngine);

    @Override
    @BeforeEach
    public void init() {

        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización de credenciales NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullCredentialsUpdateDTO_WhenValidating_ThenThrows_NullDTOException() {

        credentialsUpdateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización de credenciales con cualquiera de sus datos NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenCredentialsUpdateDTOWithAnyFieldNull_WhenValidating_ThenThrows_ConstraintViolationException() {

        setAllFieldsOnNull();

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización de credenciales con cualquiera de sus datos en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankFields_WhenValidating_ThenThrows_ConstraintViolationException() {

        setAllFieldsOnBlank();

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización cuyas contraseñas no coinciden, la validación deberá fallar y arrojará PasswordMismatchException")
    void givenPasswordsDoNotMatch_WhenValidating_ThenThrows_PasswordMismatchException() {

        credentialsUpdateDTO.setConfirmPassword("ABC123#$%&");

        assertThrows(PasswordMismatchException.class, this::validateForUpdate);
    }

    @Override
    public void setupUpdateDTO() {

        credentialsUpdateDTO = CredentialsUpdateDTO.builder()
                .username(USERNAME).password(PASSWORD)
                .confirmPassword(CONFIRM_PASSWORD)
                .build();
    }

    @Override
    public void setAllFieldsOnNull() {

        credentialsUpdateDTO.setUsername(null);
        credentialsUpdateDTO.setPassword(null);
        credentialsUpdateDTO.setConfirmPassword(null);
    }

    @Override
    public void validateForUpdate() {

        credentialValidator.validateDTO(credentialsUpdateDTO);
    }

    private void setAllFieldsOnBlank() {

        credentialsUpdateDTO.setUsername("");
        credentialsUpdateDTO.setPassword("");
        credentialsUpdateDTO.setConfirmPassword("");
    }
}
