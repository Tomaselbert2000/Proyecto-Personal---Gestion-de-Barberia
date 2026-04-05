package com.validation.tests.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceUpdateDTO;
import com.barbershop.exceptions.barberservice.*;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.barberservice.BarberServiceValidator;
import com.validation.common.ValidatorUpdateTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.BarberServiceTestDataset.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BarberServiceValidatorUpdateTest implements ValidatorUpdateTestFunctions {

    private BarberServiceUpdateDTO updateDTO;

    private final Validator validatorEngine = generateValidatorEngine();
    private final BarberServiceValidator validator = new BarberServiceValidator(validatorEngine);

    @BeforeEach
    public void init() {

        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación fallará y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        updateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos en NULL, la validación será exitosa y no se persistirán cambios")
    void givenAllFieldsOnNull_WhenUpdating_ThenDoesNotThrowsAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud mínima, la validación fallará y arrojará ConstraintViolationException")
    void givenNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto por caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación fallará y arrojará ConstraintViolationException")
    void givenBlankName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con notas internas que no cumplan longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenInternalNotesTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setInternalNotes(INTERNAL_NOTES_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio negativo o cero, la validación fallará y arrojará ConstraintViolationException")
    void givenPriceLesserOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setPrice(INVALID_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    public void setupUpdateDTO() {

        updateDTO = BarberServiceUpdateDTO.builder()
                .name(SERVICE_NAME)
                .price(PRICE)
                .serviceCategory(CATEGORY)
                .internalNotes(INTERNAL_NOTES)
                .build();
    }

    public void validateForUpdate() {

        validator.validateDTO(updateDTO);
    }

    public void setAllFieldsOnNull() {

        updateDTO.setName(null);
        updateDTO.setPrice(null);
        updateDTO.setServiceCategory(null);
        updateDTO.setInternalNotes(null);
    }
}
