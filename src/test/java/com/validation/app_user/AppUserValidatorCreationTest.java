package com.validation.app_user;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.app_user.AppUserCreationDTO;
import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.AppUserTestDataFactory.buildValidAppUserCreationDTO;
import static com.test_constant.AppUserTestConstants.InvalidData.APP_USERNAME_WITH_INVALID_SIZE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppUserValidatorCreationTest extends BaseValidatorTest<AppUserValidator, AppUserCreationDTO> {

    @Test
    @DisplayName("Dado un DTO de creación de usuario NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullCreationDTO_whenValidating_thenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación de usuario con un username NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullUsername_whenValidating_thenThrows_ConstraintViolationException() {

        inputDTO.setUsername(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una contraseña NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPassword_whenValidating_thenThrows_ConstraintViolationException() {

        inputDTO.setPassword(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un booleano de derechos de administrador NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullAdminRights_WhenValidating_ThenThrows_ConstraintViolationException() {

        inputDTO.setHasAdminRights(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de usuario en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankUsername_whenValidating_thenThrows_ConstraintViolationException() {

        inputDTO.setUsername("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una contraseña en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankPassword_whenValidating_thenThrows_ConstraintViolationException() {

        inputDTO.setPassword("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de usuario que supere la longitud máxima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenUsernameTooLong_whenValidating_thenThrows_ConstraintViolationException() {

        inputDTO.setUsername(APP_USERNAME_WITH_INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con todos sus datos válidos, la validación deberá ser exitosa y no arrojará excepción")
    void givenValidData_whenValidating_thenNoExceptionIsThrown() {

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidAppUserCreationDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new AppUserValidator(validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}
