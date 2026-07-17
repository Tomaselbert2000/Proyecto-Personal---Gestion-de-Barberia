package com.validation.app_user;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.app_user.AppUserUpdateDTO;
import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.AppUserTestDataFactory.buildValidAppUserUpdateDTO;
import static com.test_constant.AppUserTestConstants.InvalidData.APP_USERNAME_WITH_INVALID_SIZE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppUserValidatorUpdateTest extends BaseValidatorTest<AppUserValidator, AppUserUpdateDTO> {

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos NULL, la validación deberá ser exitosa y no arrojará excepción")
    void givenUpdateDTOWithAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de usuario en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankUsername_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setUsername("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una contraseña de usuario en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankPassword_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPassword("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de usuario que supere la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenUsernameLongerThanMaxLength_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setUsername(APP_USERNAME_WITH_INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con datos correctos, la validación será exitosa y no arrojará excepción")
    void givenUpdateDTOWithValidData_WhenUpdating_ThenDoesNotThrowAnything() {

        assertDoesNotThrow(this::validateInputDTO);
    }

    public void setAllFieldsOnNull() {

        inputDTO.setUsername(null);
        inputDTO.setPassword(null);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidAppUserUpdateDTO();
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
