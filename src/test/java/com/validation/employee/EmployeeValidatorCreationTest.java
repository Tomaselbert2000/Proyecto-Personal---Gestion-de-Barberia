package com.validation.employee;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.employee.EmployeeCreationDTO;
import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.EmployeeTestDataFactory.buildValidEmployeeCreationDTO;
import static com.test_constant.EmployeeTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeValidatorCreationTest extends BaseValidatorTest<EmployeeValidator, EmployeeCreationDTO> {

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenCreating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre NULL, arrojará ConstraintViolationException")
    void givenNullFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido NULL, arrojará ConstraintViolationException")
    void givenNullLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha de contratación NULL, arrojará ConstraintViolationException")
    void givenNullHireDate_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setHireDate(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de comisión NULL, arrojará ConstraintViolationException")
    void givenNullCommissionPercentage_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCommissionPercentage(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, arrojará ConstraintViolationException")
    void givenBlankFirstName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido en blanco, arrojará ConstraintViolationException")
    void givenBlankLastName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto de caractéres inválidos, arrojará ConstraintViolationException")
    void givenFirstNameWithInvalidCharacter_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(EMPLOYEE_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido compuesto de caractéres inválidos, arrojará ConstraintViolationException")
    void givenLastNameWithInvalidCharacter_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(EMPLOYEE_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenFirstNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(EMPLOYEE_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenFirstNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(EMPLOYEE_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(EMPLOYEE_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un apellido que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(EMPLOYEE_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de comisión negativo, arrojará ConstraintViolationException")
    void givenCommissionPercentage_LowerThanZero_ThenThrows_ConstraintViolationException() {

        inputDTO.setCommissionPercentage(EMPLOYEE_COMMISSION_PERCENTAGE_LOWER_THAN_ZERO);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de comisión mayor a 1, arrojará ConstraintViolationException")
    void givenCommissionPercentageHigherThanOne_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCommissionPercentage(EMPLOYEE_COMMISSION_PERCENTAGE_HIGHER_THAN_ONE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidEmployeeCreationDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new EmployeeValidator(validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}
