package com.validation.tests.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.barberservice.BarberServiceValidator;
import com.validation.common.ValidatorCreationTestFunctions;
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
public class BarberServiceValidatorCreationTest implements ValidatorCreationTestFunctions {

    private final Validator validatorEngine = generateValidatorEngine();
    private final BarberServiceValidator validator = new BarberServiceValidator(validatorEngine);

    private BarberServiceCreationDTO creationDTO;

    @BeforeEach
    public void init() {

        setupCreationDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación fallará y arrojará NullDTOException")
    void givenNullCreationDTO_WhenCreating_ThenThrows_NullDTOException() {

        creationDTO = null;

        assertThrows(NullDTOException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullBarberServiceName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullPrice_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPrice(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una categoría NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullCategory_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setServiceCategory(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con notas internas NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullInternalNotes_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setInternalNotes(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla longitud mínima, la validación fallará y arrojará ConstraintViolationException")
    void givenNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con nombre que no cumpla la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto por caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un string de notas internas con una longitud que exceda el límite, la validación fallará y arrojará ConstraintViolationException")
    void givenInternalNotesTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setInternalNotes(INTERNAL_NOTES_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un string de notas internas en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankInternalNotes_WhenCreating_ThenDoesNotThrowAnything() {

        creationDTO.setInternalNotes("");

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPriceLesserOrEqualThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPrice(INVALID_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    public void setupCreationDTO() {

        creationDTO = BarberServiceCreationDTO.builder()
                .name(SERVICE_NAME)
                .price(PRICE)
                .serviceCategory(CATEGORY)
                .internalNotes(INTERNAL_NOTES)
                .build();
    }

    public void validateForCreation() {

        validator.validateDTO(creationDTO);
    }
}
