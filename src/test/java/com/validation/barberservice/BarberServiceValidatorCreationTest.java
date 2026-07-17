package com.validation.barberservice;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.barbershopservice.BarberServiceCreationDTO;
import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.BarberServiceTestDataFactory.buildValidBarberServiceCreationDTO;
import static com.test_constant.BarberServiceTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BarberServiceValidatorCreationTest extends BaseValidatorTest<BarberServiceValidator, BarberServiceCreationDTO> {

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación fallará y arrojará NullDTOException")
    void givenNullCreationDTO_WhenCreating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullBarberServiceName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullPrice_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPrice(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una categoría NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullCategory_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setServiceCategory(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con notas internas NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullInternalNotes_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setInternalNotes(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla longitud mínima, la validación fallará y arrojará ConstraintViolationException")
    void givenNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con nombre que no cumpla la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto por caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un string de notas internas con una longitud que exceda el límite, la validación fallará y arrojará ConstraintViolationException")
    void givenInternalNotesTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setInternalNotes(INTERNAL_NOTES_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un string de notas internas en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankInternalNotes_WhenCreating_ThenDoesNotThrowAnything() {

        inputDTO.setInternalNotes("");

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPriceLesserOrEqualThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPrice(INVALID_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidBarberServiceCreationDTO();
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
