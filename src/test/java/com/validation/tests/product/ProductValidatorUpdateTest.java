package com.validation.tests.product;

import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.exceptions.product.*;
import com.barbershop.validation.product.ProductValidator;
import com.validation.common.ValidatorUpdateTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.validation.dataset.ProductTestDataset.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProductValidatorUpdateTest implements ValidatorUpdateTestFunctions {

    private final Validator validatorEngine = Validation.buildDefaultValidatorFactory().getValidator();
    private final ProductValidator validator = new ProductValidator(validatorEngine);

    private ProductUpdateDTO updateDTO;


    @BeforeEach
    public void init() {

        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación deberá fallar y arrojará NullDTOException")
    void givenNullDTO_WhenUpdating_ThenThrows_NullDTOException() {

        updateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos NULL, la validación deberá ser exitosa y no se persistirán cambios en la entidad")
    void givenAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto por caracteres inválidos, la validación deberá fallar y arrojara ConstraintViolationException")
    void givenInvalidName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de marca en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankBrandName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setBrandName("");

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre marca que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameTooShort_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setBrandName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de marca que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setBrandName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de marca compuesto por caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidBrandName_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setBrandName(INVALID_BRAND_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción opcional en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankOptionalDescription_WhenUpating_ThenDoesNotThrowAnything() {

        updateDTO.setOptionalDescription("");

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción que supere la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenOptionalDescriptionTooLong_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setOptionalDescription(OPTIONAL_DESCRIPTION_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una medida de presentación menor o igual a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPresentationSizeLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setPresentationSize(INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de costo negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenProductCostLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setProductCost(NEGATIVE_COST);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de venta mínimo menor o igual a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenMinPriceLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setMinPrice(NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio actual de venta negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPriceLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setCurrentPrice(NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de venta menor al valor de costo, la validación deberá fallar y arrojará InvalidProductCurrentPriceException")
    void givenPriceLowerThanCost_WhenUpdating_ThenThrows_InvalidProductCurrentPriceException() {

        updateDTO.setCurrentPrice(COST - 100);

        assertThrows(InvalidProductCurrentPriceException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio actual de venta igual al valor de costo, la validación deberá fallar y arrojará InvalidProductCurrentPriceException")
    void givenPriceEqualsThanCost_WhenUpdating_ThenThrows_InvalidProductCurrentPriceException() {

        updateDTO.setCurrentPrice(COST);

        assertThrows(InvalidProductCurrentPriceException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de venta mayorista menor o igual a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenWholeSalePriceLowerOrEqualsThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setProductWholeSalePrice(NEGATIVE_COST);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de stock actual menor que cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenCurrentStockLowerThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setCurrentStockLevel(CURRENT_STOCK_LEVEL * (-1));

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de stock mínimo menor que cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenSafetyStockLevelLowerThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setSafetyStockLevel(SAFETY_STOCK_LEVEL * (-1));

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de porcentaje máximo de descuento menor a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenMaxDiscountPercentageLowerThanZero_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setMaxDiscountPercentage(DISCOUNT_PERCENTAGE_LOWER_THAN_ZERO);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor de porcentaje máximo de descuento mayor a uno, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenMaxDiscountPercentageHigherThanOne_WhenUpdating_ThenThrows_ConstraintViolationException() {

        updateDTO.setMaxDiscountPercentage(DISCOUNT_PERCENTAGE_HIGHER_THAN_ONE);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    public void setupUpdateDTO() {

        updateDTO = ProductUpdateDTO.builder()
                .name(NAME)
                .optionalDescription(OPTIONAL_DESCRIPTION)
                .brandName(BRAND_NAME)
                .presentationUnit(UNIT)
                .presentationSize(PRESENTATION_SIZE)
                .productCost(COST)
                .minPrice(MIN_PRICE)
                .currentPrice(CURRENT_PRICE)
                .productWholeSalePrice(WHOLE_SALE_PRICE)
                .maxDiscountPercentage(MAX_DISCOUNT_PERCENTAGE_VALUE)
                .category(CATEGORY)
                .currentStockLevel(CURRENT_STOCK_LEVEL)
                .safetyStockLevel(SAFETY_STOCK_LEVEL)
                .imageFilePath(FILE_PATH)
                .build();
    }

    public void validateForUpdate() {

        validator.validateDTO(updateDTO);
    }

    public void setAllFieldsOnNull() {

        updateDTO.setName(null);
        updateDTO.setOptionalDescription(null);
        updateDTO.setBrandName(null);
        updateDTO.setPresentationUnit(null);
        updateDTO.setPresentationSize(null);
        updateDTO.setProductCost(null);
        updateDTO.setMinPrice(null);
        updateDTO.setCurrentPrice(null);
        updateDTO.setProductWholeSalePrice(null);
        updateDTO.setMaxDiscountPercentage(null);
        updateDTO.setCurrentStockLevel(null);
        updateDTO.setSafetyStockLevel(null);
        updateDTO.setCategory(null);
        updateDTO.setImageFilePath(null);
    }
}
