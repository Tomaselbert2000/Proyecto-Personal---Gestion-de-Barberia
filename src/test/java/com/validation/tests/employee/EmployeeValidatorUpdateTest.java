package com.validation.tests.employee;

import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.employee.EmployeeValidator;
import com.validation.common.ValidatorUpdateTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.EmployeeTestDataset.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EmployeeValidatorUpdateTest implements ValidatorUpdateTestFunctions {

    private final Validator validatorEngine = generateValidatorEngine();
    private final EmployeeValidator validator = new EmployeeValidator(validatorEngine);

    private EmployeeUpdateDTO updateDTO;

    @BeforeEach
    public void init() {

        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, arrojará NullUpdateDTOException")
    void givenNullUpdateDTO_WhenUpdating_ThenThrows_NullUpdateDTOException() {

        updateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos NULL, la validación será exitosa y la entidad deberá conservar sus datos")
    void givenAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, arrojará ConstraintViolationException")
    void givenBlankFirstName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setFirstName("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido en blanco, arrojará ConstraintViolationException")
    void givenBlankLastName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setLastName("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto de caractéres inválidos, arrojará ConstraintViolationException")
    void givenFirstNameWithInvalidCharacters_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setFirstName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido compuesto de caractéres inválidos, arrojará ConstraintViolationException")
    void givenLastNameWithInvalidCharacters_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setLastName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud mínima, la validación fallará y arrojará ConstraintViolationException")
    void givenFirstNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setFirstName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenFirstNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setFirstName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setLastName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un apellido que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenLastNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setLastName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de comisión negativo, arrojará ConstraintViolationException")
    void givenCommmissionPercentage_LowerThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setCommissionPercentage(COMMISSION_PERCENTAGE_LOWER_THAN_ZERO);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de comisión mayor a 1, arrojará ConstraintViolationException")
    void givenCommissionPercentageHigherThanOne_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setCommissionPercentage(COMMISSION_PERCENTAGE_HIGHER_THAN_ONE);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    public void setupUpdateDTO() {

        updateDTO = EmployeeUpdateDTO.builder()
                .firstName(NAME)
                .lastName(SURNAME)
                .isActive(IS_ACTIVE)
                .terminationDate(TERMINATION_DATE)
                .commissionPercentage(NEW_COMMISSION_PERCENTAGE)
                .build();
    }

    public void setAllFieldsOnNull() {

        updateDTO.setFirstName(null);
        updateDTO.setLastName(null);
        updateDTO.setIsActive(null);
        updateDTO.setTerminationDate(null);
        updateDTO.setCommissionPercentage(null);
    }

    public void validateForUpdate() {

        validator.validateDTO(updateDTO);
    }
}
