package com.validation.tests.app_user;

import com.barbershop.dto.app_user.AppUserUpdateDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.app_user.AppUserValidator;
import com.validation.common.ValidatorUpdateTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.AppUserDataset.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AppUserValidatorUpdateTest implements ValidatorUpdateTestFunctions {

    private AppUserUpdateDTO updateDTO;

    private final Validator validatorEngine = generateValidatorEngine();

    private final AppUserValidator appUserValidator = new AppUserValidator(validatorEngine);

    @Override
    @BeforeEach
    public void init() {

        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        updateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos NULL, la validación deberá ser exitosa y no arrojará excepción")
    void givenUpdateDTOWithAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de usuario en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankUsername_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setUsername("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una contraseña de usuario en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankPassword_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setPassword("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de usuario que supere la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenUsernameLongerThanMaxLength_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setUsername(APP_USERNAME_WITH_INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con datos correctos, la validación será exitosa y no arrojará excepción")
    void givenUpdateDTOWithValidData_WhenUpdating_ThenDoesNotThrowAnything() {

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Override
    public void setupUpdateDTO() {

        updateDTO = AppUserUpdateDTO.builder()
                .username(APP_USERNAME)
                .password(APP_PASSWORD)
                .build();
    }

    @Override
    public void setAllFieldsOnNull() {

        updateDTO.setAppUserId(null);
        updateDTO.setUsername(null);
        updateDTO.setPassword(null);
    }

    @Override
    public void validateForUpdate() {

        appUserValidator.validateDTO(updateDTO);
    }
}
