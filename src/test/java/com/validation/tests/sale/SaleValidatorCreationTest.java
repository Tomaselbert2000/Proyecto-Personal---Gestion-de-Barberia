package com.validation.tests.sale;

import com.barbershop.dto.sale.SaleCreationDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.exceptions.sale.*;
import com.barbershop.validation.sale.SaleValidator;
import com.validation.common.ValidatorCreationTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;

import static com.barbershop.validation.common.CommonValidationFunctions.generateClockInstance;
import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.SaleTestDataset.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SaleValidatorCreationTest implements ValidatorCreationTestFunctions {

    private SaleCreationDTO creationDTO;

    private final Clock clock = generateClockInstance(INSTANT, ZONE_ID);

    private final Validator validatorEngine = generateValidatorEngine();
    private final SaleValidator validator = new SaleValidator(clock, validatorEngine);


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
    @DisplayName("Dado un DTO de creación con una fecha y hora NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullDateTime_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setDateAndTime(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de cliente NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullClientID_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setClientID(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de medio de pago NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPaymentMethodID_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPaymentMethodID(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL, y una lista de productos que no esté vacía, la validación deberá ser exitosa y no arrojará excepción")
    void givenNullBarberServiceID_WithProductItemListNotNullOrNotEmpty_WhenCreating_ThenThrows_DoesNotThrowAnything() {

        creationDTO.setBarberServiceID(null);
        creationDTO.setProductsDetail(NON_EMPTY_LIST);

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de productos vacía, pero que indique un ID de servicio no NULL, la validación deberá ser exitosa y no arrojará excepción")
    void givenEmptyProductItemList_WithBarberServiceIDNotNull_WhenCreating_ThenDoesNotThrowAnything() {

        creationDTO.setProductsDetail(EMPTY_LIST);

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL y una lista de productos NULL, la validación deberá fallar y arrojará EmptyProductItemListException")
    void givenNullBarberServiceID_AndNullProductItemList_WhenCreating_ThenThrows_EmptyProductItemListException() {

        creationDTO.setProductsDetail(null);
        creationDTO.setBarberServiceID(null);

        assertThrows(EmptyProductItemListException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de empleado NULL, pero cuyo ID de servicio no sea NULL, la validación deberá fallar y arrojará OrphanBarberServiceException")
    void givenNullEmployeeID_AndNotNullBarberServiceID_WhenCreating_ThenThrows_OrphanBarberServiceException() {

        creationDTO.setEmployeeID(null);

        assertThrows(OrphanBarberServiceException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL y una lista de productos no vacía, el ID de empleado será ignorado y no arrojará excepción")
    void givenNullBarberServiceID_AndProductListNotEmpty_WhenCreating_Then_EmployeeID_IsIgnored() {

        creationDTO.setBarberServiceID(null);
        creationDTO.setProductsDetail(NON_EMPTY_LIST);

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL y una lista vacía, la validación deberá fallar y arrojará EmptyProductItemListException")
    void givenNullBarberServiceID_AndEmptyProductItemList_WhenCreating_ThenThrows_EmptyProductItemListException() {

        creationDTO.setBarberServiceID(null);
        creationDTO.setProductsDetail(EMPTY_LIST);

        assertThrows(EmptyProductItemListException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora posteriores a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidSaleDateTimeException")
    void givenSaleDateTimeAfterCurrentSystemDate_WhenCreating_ThenThrows_InvalidSaleDateTimeException() {

        creationDTO.setDateAndTime(INVALID_REGISTRATION_TIMESTAMP);

        assertThrows(InvalidSaleDateTimeException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y horas dentro de las últimas 24 horas, permitirá registrar la venta exitosamente y no arrojará excepción")
    void givenSaleDateTimeUpTo24HoursBeforeCurrentSystemDateTime_WhenCreating_ThenDoesNotThrowAnything() {

        creationDTO.setDateAndTime(REGISTRATION_TIMESTAMP_IN_TIME_RANGE_LIMIT);

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y horas fuera de las últimas 24 horas, la validación deberá fallar y arrojará SaleDateTimeOutOfRangeException")
    void givenSaleDateTimeOutsideThe24HoursLimit_WhenCreating_ThenThrows_SaleDateTimeOutOfRangeException() {

        creationDTO.setDateAndTime(REGISTRATION_TIMESTAMP_OUTSIDE_RANGE_LIMIT);

        assertThrows(SaleDateTimeOutOfRangeException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación que contenga un product con ID NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenProductItemWithNullID_ThenThrows_ConstraintViolationException() {

        creationDTO.setProductsDetail(LIST_WITH_NULL_PRODUCT);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista que contenga un producto con una cantidad NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenProductItemWithNullQuantity_ThenThrows_ConstraintViolationException() {

        creationDTO.setProductsDetail(LIST_WITH_NULL_QUANTITY);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de productos que incluya un producto con cantidad negativa o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenProductItem_WithNegativeQuantity_ThenThrows_ConstraintViolationException() {

        creationDTO.setProductsDetail(LIST_WITH_NEGATIVE_QUANTITY);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Override
    public void setupCreationDTO() {

        creationDTO = SaleCreationDTO.builder()
                .dateAndTime(REGISTRATION_TIMESTAMP)
                .clientID(CLIENT_ID)
                .employeeID(EMPLOYEE_ID)
                .paymentMethodID(PAYMENT_METHOD_ID)
                .barberServiceID(BARBER_SERVICE_ID)
                .productsDetail(List.of())
                .build();
    }

    @Override
    public void validateForCreation() {

        validator.validateDTO(creationDTO);
    }
}
