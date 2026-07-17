package com.validation.credentials;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.credentials_management.CredentialsUpdateDTO;
import com.exceptions.common.NullDTOException;
import com.exceptions.credentials.PasswordMismatchException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.CredentialsTestDataFactory.buildValidCredentialsUpdateDTO;
import static com.test_constant.CredentialUpdateTestConstants.InvalidData.INVALID_CONFIRM_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CredentialsUpdateValidatorTest extends BaseValidatorTest<CredentialsUpdateValidator, CredentialsUpdateDTO> {

    @Test
    @DisplayName("Dado un DTO de actualización de credenciales NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullCredentialsUpdateDTO_WhenValidating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización de credenciales con cualquiera de sus datos NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenCredentialsUpdateDTOWithAnyFieldNull_WhenValidating_ThenThrows_ConstraintViolationException() {

        setAllFieldsOnNull();

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización de credenciales con cualquiera de sus datos en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankFields_WhenValidating_ThenThrows_ConstraintViolationException() {

        setAllFieldsOnBlank();

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización cuyas contraseñas no coinciden, la validación deberá fallar y arrojará PasswordMismatchException")
    void givenPasswordsDoNotMatch_WhenValidating_ThenThrows_PasswordMismatchException() {

        inputDTO.setConfirmPassword(INVALID_CONFIRM_PASSWORD);

        assertThrows(PasswordMismatchException.class, this::validateInputDTO);
    }

    public void setAllFieldsOnNull() {

        inputDTO.setUsername(null);
        inputDTO.setPassword(null);
        inputDTO.setConfirmPassword(null);
    }

    private void setAllFieldsOnBlank() {

        inputDTO.setUsername("");
        inputDTO.setPassword("");
        inputDTO.setConfirmPassword("");
    }

    @Override
    protected void setupInputDTO() {
        inputDTO = buildValidCredentialsUpdateDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new CredentialsUpdateValidator(validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}
