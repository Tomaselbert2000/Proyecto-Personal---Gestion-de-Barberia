package com.validation.tests.employee;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.employee.EmployeeValidator;
import com.validation.common.ValidatorCreationTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.EmployeeTestDataset.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmployeeValidatorCreationTest implements ValidatorCreationTestFunctions {

    private final Validator validatorEngine = generateValidatorEngine();
    private final EmployeeValidator validator = new EmployeeValidator(validatorEngine);

    private EmployeeCreationDTO creationDTO;

    @BeforeEach
    public void init() {

        setupCreationDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenCreating_ThenThrows_NullDTOException() {

        creationDTO = null;

        assertThrows(NullDTOException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre NULL, arrojará ConstraintViolationException")
    void givenNullFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido NULL, arrojará ConstraintViolationException")
    void givenNullLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha de contratación NULL, arrojará ConstraintViolationException")
    void givenNullHireDate_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setHireDate(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de comisión NULL, arrojará ConstraintViolationException")
    void givenNullCommissionPercentage_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setCommissionPercentage(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, arrojará ConstraintViolationException")
    void givenBlankFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido en blanco, arrojará ConstraintViolationException")
    void givenBlankLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto de caractéres inválidos, arrojará ConstraintViolationException")
    void givenFirstNameWithInvalidCharacter_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido compuesto de caractéres inválidos, arrojará ConstraintViolationException")
    void givenLastNameWithInvalidCharacter_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenFirstNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenFirstNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setFirstName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setLastName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de comisión negativo, arrojará ConstraintViolationException")
    void givenCommissionPercentage_LowerThanZero_ThenThrows_ConstraintViolationException() {

        creationDTO.setCommissionPercentage(COMMISSION_PERCENTAGE_LOWER_THAN_ZERO);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de comisión mayor a 1, arrojará ConstraintViolationException")
    void givenCommissionPercentageHigherThanOne_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setCommissionPercentage(COMMISSION_PERCENTAGE_HIGHER_THAN_ONE);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    public void setupCreationDTO() {

        creationDTO = EmployeeCreationDTO.builder()
                .firstName(NAME)
                .lastName(SURNAME)
                .hireDate(HIRE_DATE)
                .commissionPercentage(COMMISSION_PERCENTAGE)
                .build();
    }

    public void validateForCreation() {

        validator.validateDTO(creationDTO);
    }
}
