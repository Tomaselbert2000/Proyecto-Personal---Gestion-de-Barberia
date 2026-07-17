package com.validation.employee;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.employee.EmployeeUpdateDTO;
import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.EmployeeTestDataFactory.buildValidEmployeeUpdateDTO;
import static com.test_constant.EmployeeTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeValidatorUpdateTest extends BaseValidatorTest<EmployeeValidator, EmployeeUpdateDTO> {

    @Test
    @DisplayName("Dado un DTO de actualización NULL, arrojará NullUpdateDTOException")
    void givenNullUpdateDTO_WhenUpdating_ThenThrows_NullUpdateDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos NULL, la validación será exitosa y la entidad deberá conservar sus datos")
    void givenAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, arrojará ConstraintViolationException")
    void givenBlankFirstName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido en blanco, arrojará ConstraintViolationException")
    void givenBlankLastName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto de caractéres inválidos, arrojará ConstraintViolationException")
    void givenFirstNameWithInvalidCharacters_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(EMPLOYEE_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido compuesto de caractéres inválidos, arrojará ConstraintViolationException")
    void givenLastNameWithInvalidCharacters_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(EMPLOYEE_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud mínima, la validación fallará y arrojará ConstraintViolationException")
    void givenFirstNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(EMPLOYEE_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenFirstNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setFirstName(EMPLOYEE_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(EMPLOYEE_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setLastName(EMPLOYEE_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de comisión negativo, arrojará ConstraintViolationException")
    void givenCommmissionPercentage_LowerThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCommissionPercentage(EMPLOYEE_COMMISSION_PERCENTAGE_LOWER_THAN_ZERO);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de comisión mayor a 1, arrojará ConstraintViolationException")
    void givenCommissionPercentageHigherThanOne_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCommissionPercentage(EMPLOYEE_COMMISSION_PERCENTAGE_HIGHER_THAN_ONE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    public void setAllFieldsOnNull() {

        inputDTO.setFirstName(null);
        inputDTO.setLastName(null);
        inputDTO.setIsActive(null);
        inputDTO.setTerminationDate(null);
        inputDTO.setCommissionPercentage(null);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidEmployeeUpdateDTO();
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
