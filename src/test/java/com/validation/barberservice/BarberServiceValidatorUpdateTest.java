package com.validation.barberservice;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.barbershopservice.BarberServiceUpdateDTO;
import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.BarberServiceTestDataFactory.buildValidBarberServiceUpdateDTO;
import static com.test_constant.BarberServiceTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BarberServiceValidatorUpdateTest extends BaseValidatorTest<BarberServiceValidator, BarberServiceUpdateDTO> {

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación fallará y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos en NULL, la validación será exitosa y no se persistirán cambios")
    void givenAllFieldsOnNull_WhenUpdating_ThenDoesNotThrowsAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud mínima, la validación fallará y arrojará ConstraintViolationException")
    void givenNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto por caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación fallará y arrojará ConstraintViolationException")
    void givenBlankName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con notas internas que no cumplan longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenInternalNotesTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setInternalNotes(INTERNAL_NOTES_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio negativo o cero, la validación fallará y arrojará ConstraintViolationException")
    void givenPriceLesserOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPrice(INVALID_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    public void setAllFieldsOnNull() {

        inputDTO.setName(null);
        inputDTO.setPrice(null);
        inputDTO.setServiceCategory(null);
        inputDTO.setInternalNotes(null);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidBarberServiceUpdateDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new BarberServiceValidator(validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}
