package com.validation.tests.app_user;

import com.barbershop.dto.app_user.AppUserCreationDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.app_user.AppUserValidator;
import com.validation.common.ValidatorCreationTestFunctions;
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
public class AppUserValidatorCreationTest implements ValidatorCreationTestFunctions {

    private AppUserCreationDTO creationDTO;

    private final Validator validatorEngine = generateValidatorEngine();

    private final AppUserValidator appUserValidator = new AppUserValidator(validatorEngine);

    @Override
    @BeforeEach
    public void init() {

        setupCreationDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación de usuario NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullCreationDTO_whenValidating_thenThrows_NullDTOException() {

        creationDTO = null;

        assertThrows(NullDTOException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación de usuario con un username NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullUsername_whenValidating_thenThrows_ConstraintViolationException() {

        creationDTO.setUsername(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una contraseña NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPassword_whenValidating_thenThrows_ConstraintViolationException() {

        creationDTO.setPassword(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de usuario en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankUsername_whenValidating_thenThrows_ConstraintViolationException() {

        creationDTO.setUsername("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una contraseña en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankPassword_whenValidating_thenThrows_ConstraintViolationException() {

        creationDTO.setPassword("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de usuario que supere la longitud máxima permitida, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenUsernameTooLong_whenValidating_thenThrows_ConstraintViolationException() {

        creationDTO.setUsername(APP_USERNAME_WITH_INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con todos sus datos válidos, la validación deberá ser exitosa y no arrojará excepción")
    void givenValidData_whenValidating_thenNoExceptionIsThrown() {

        assertDoesNotThrow(this::validateForCreation);
    }

    @Override
    public void setupCreationDTO() {

        creationDTO = AppUserCreationDTO.builder()
                .username(APP_USERNAME)
                .password(APP_PASSWORD)
                .build();
    }

    @Override
    public void validateForCreation() {

        appUserValidator.validateForCreation(creationDTO);
    }
}
