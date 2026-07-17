package com.validation.sale;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.sale.SaleCreationDTO;
import com.exceptions.common.NullDTOException;
import com.exceptions.sale.EmptyProductItemListException;
import com.exceptions.sale.InvalidSaleDateTimeException;
import com.exceptions.sale.OrphanBarberServiceException;
import com.exceptions.sale.SaleDateTimeOutOfRangeException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.SaleTestDataFactory.buildValidSaleCreationDTO;
import static com.test_constant.SaleTestConstants.CreationValidData.NON_EMPTY_LIST;
import static com.test_constant.SaleTestConstants.CreationValidData.REGISTRATION_TIMESTAMP_IN_TIME_RANGE_LIMIT;
import static com.test_constant.SaleTestConstants.InvalidData.*;
import static com.test_constant.SaleTestConstants.TimeConfigData.SALE_TEST_CLOCK;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SaleValidatorCreationTest extends BaseValidatorTest<SaleValidator, SaleCreationDTO> {

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenCreating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullDateTime_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setDateAndTime(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de cliente NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullClientID_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setClientID(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de medio de pago NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPaymentMethodID_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPaymentMethodID(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL, y una lista de productos que no esté vacía, la validación deberá ser exitosa y no arrojará excepción")
    void givenNullBarberServiceID_WithProductItemListNotNullOrNotEmpty_WhenCreating_ThenThrows_DoesNotThrowAnything() {

        inputDTO.setBarberServiceID(null);
        inputDTO.setProductsDetail(NON_EMPTY_LIST);

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de productos vacía, pero que indique un ID de servicio no NULL, la validación deberá ser exitosa y no arrojará excepción")
    void givenEmptyProductItemList_WithBarberServiceIDNotNull_WhenCreating_ThenDoesNotThrowAnything() {

        inputDTO.setProductsDetail(EMPTY_LIST);

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL y una lista de productos NULL, la validación deberá fallar y arrojará EmptyProductItemListException")
    void givenNullBarberServiceID_AndNullProductItemList_WhenCreating_ThenThrows_EmptyProductItemListException() {

        inputDTO.setProductsDetail(null);
        inputDTO.setBarberServiceID(null);

        assertThrows(EmptyProductItemListException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de empleado NULL, pero cuyo ID de servicio no sea NULL, la validación deberá fallar y arrojará OrphanBarberServiceException")
    void givenNullEmployeeID_AndNotNullBarberServiceID_WhenCreating_ThenThrows_OrphanBarberServiceException() {

        inputDTO.setEmployeeID(null);

        assertThrows(OrphanBarberServiceException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL y una lista de productos no vacía, el ID de empleado será ignorado y no arrojará excepción")
    void givenNullBarberServiceID_AndProductListNotEmpty_WhenCreating_Then_EmployeeID_IsIgnored() {

        inputDTO.setBarberServiceID(null);
        inputDTO.setProductsDetail(NON_EMPTY_LIST);

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL y una lista vacía, la validación deberá fallar y arrojará EmptyProductItemListException")
    void givenNullBarberServiceID_AndEmptyProductItemList_WhenCreating_ThenThrows_EmptyProductItemListException() {

        inputDTO.setBarberServiceID(null);
        inputDTO.setProductsDetail(EMPTY_LIST);

        assertThrows(EmptyProductItemListException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora posteriores a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidSaleDateTimeException")
    void givenSaleDateTimeAfterCurrentSystemDate_WhenCreating_ThenThrows_InvalidSaleDateTimeException() {

        inputDTO.setDateAndTime(INVALID_REGISTRATION_TIMESTAMP);

        assertThrows(InvalidSaleDateTimeException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y horas dentro de las últimas 24 horas, permitirá registrar la venta exitosamente y no arrojará excepción")
    void givenSaleDateTimeUpTo24HoursBeforeCurrentSystemDateTime_WhenCreating_ThenDoesNotThrowAnything() {

        inputDTO.setDateAndTime(REGISTRATION_TIMESTAMP_IN_TIME_RANGE_LIMIT);

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y horas fuera de las últimas 24 horas, la validación deberá fallar y arrojará SaleDateTimeOutOfRangeException")
    void givenSaleDateTimeOutsideThe24HoursLimit_WhenCreating_ThenThrows_SaleDateTimeOutOfRangeException() {

        inputDTO.setDateAndTime(REGISTRATION_TIMESTAMP_OUTSIDE_RANGE_LIMIT);

        assertThrows(SaleDateTimeOutOfRangeException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación que contenga un product con ID NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenProductItemWithNullID_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductsDetail(LIST_WITH_NULL_PRODUCT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista que contenga un producto con una cantidad NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenProductItemWithNullQuantity_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductsDetail(LIST_WITH_NULL_QUANTITY);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una lista de productos que incluya un producto con cantidad negativa o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenProductItem_WithNegativeQuantity_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductsDetail(LIST_WITH_NEGATIVE_QUANTITY);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidSaleCreationDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new SaleValidator(SALE_TEST_CLOCK, validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}
