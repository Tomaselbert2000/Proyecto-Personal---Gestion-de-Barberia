package com.validation.product;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.product.ProductUpdateDTO;
import com.exceptions.common.NullDTOException;
import com.exceptions.product.InvalidProductCurrentPriceException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.ProductTestDataFactory.buildValidProductUpdateDTO;
import static com.test_constant.ProductTestConstants.CreationValidData.*;
import static com.test_constant.ProductTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductValidatorUpdateTest extends BaseValidatorTest<ProductValidator, ProductUpdateDTO> {

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos NULL, la validación deberá ser exitosa y no se persistirán cambios en la entidad")
    void givenAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto por caracteres inválidos, la validación deberá fallar y arrojara ConstraintViolationException")
    void givenInvalidName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PRODUCT_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PRODUCT_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PRODUCT_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de marca en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankBrandName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre marca que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName(PRODUCT_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de marca que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName(PRODUCT_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de marca compuesto por caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidBrandName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName(PRODUCT_INVALID_BRAND_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción opcional en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankOptionalDescription_WhenUpating_ThenDoesNotThrowAnything() {

        inputDTO.setOptionalDescription("");

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción que supere la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenOptionalDescriptionTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setOptionalDescription(PRODUCT_OPTIONAL_DESCRIPTION_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una medida de presentación menor o igual a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPresentationSizeLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPresentationSize(PRODUCT_INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de costo negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenProductCostLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductCost(PRODUCT_NEGATIVE_COST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de venta mínimo menor o igual a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenMinPriceLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setMinPrice(PRODUCT_NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio actual de venta negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPriceLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCurrentPrice(PRODUCT_NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de venta menor al valor de costo, la validación deberá fallar y arrojará InvalidProductCurrentPriceException")
    void givenPriceLowerThanCost_WhenUpdating_ThenThrows_InvalidProductCurrentPriceException() {

        inputDTO.setCurrentPrice(PRODUCT_COST - 100);

        assertThrows(InvalidProductCurrentPriceException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio actual de venta igual al valor de costo, la validación deberá fallar y arrojará InvalidProductCurrentPriceException")
    void givenPriceEqualsThanCost_WhenUpdating_ThenThrows_InvalidProductCurrentPriceException() {

        inputDTO.setCurrentPrice(PRODUCT_COST);

        assertThrows(InvalidProductCurrentPriceException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de venta mayorista menor o igual a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenWholeSalePriceLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductWholeSalePrice(PRODUCT_NEGATIVE_COST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de stock actual menor que cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenCurrentStockLowerThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCurrentStockLevel(PRODUCT_CURRENT_STOCK_LEVEL * (-1));

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de stock mínimo menor que cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenSafetyStockLevelLowerThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setSafetyStockLevel(PRODUCT_SAFETY_STOCK_LEVEL * (-1));

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de porcentaje máximo de descuento menor a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenMaxDiscountPercentageLowerThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setMaxDiscountPercentage(PRODUCT_DISCOUNT_PERCENTAGE_LOWER_THAN_ZERO);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de porcentaje máximo de descuento mayor a uno, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenMaxDiscountPercentageHigherThanOne_WhenUpdating_ThenThrows_ConstraintViolationException() {

        inputDTO.setMaxDiscountPercentage(PRODUCT_DISCOUNT_PERCENTAGE_HIGHER_THAN_ONE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    public void setAllFieldsOnNull() {

        inputDTO.setName(null);
        inputDTO.setOptionalDescription(null);
        inputDTO.setBrandName(null);
        inputDTO.setPresentationUnit(null);
        inputDTO.setPresentationSize(null);
        inputDTO.setProductCost(null);
        inputDTO.setMinPrice(null);
        inputDTO.setCurrentPrice(null);
        inputDTO.setProductWholeSalePrice(null);
        inputDTO.setMaxDiscountPercentage(null);
        inputDTO.setCurrentStockLevel(null);
        inputDTO.setSafetyStockLevel(null);
        inputDTO.setCategory(null);
        inputDTO.setImageFilePath(null);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidProductUpdateDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new ProductValidator(validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}
