package com.validation.product;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.product.ProductCreationDTO;
import com.exceptions.common.NullDTOException;
import com.exceptions.product.InvalidProductCurrentPriceException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.ProductTestDataFactory.buildValidProductCreationDTO;
import static com.test_constant.ProductTestConstants.CreationValidData.*;
import static com.test_constant.ProductTestConstants.InvalidData.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductValidatorCreationTest extends BaseValidatorTest<ProductValidator, ProductCreationDTO> {

    @Test
    @DisplayName("Dado un DTO de creación con datos válidos, la validación deberá ser exitosa")
    void givenDTOWithValidData_WhenCreating_ThenDoesNotThrowAnything() {

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación deberá fallar y arrojar NullDTOException")
    void givenNullDTO_WhenCreating_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una marca NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullBrandName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un string de notas opcionales NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullOptionalDescription_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setOptionalDescription(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación cuyo valor de presentación NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPresentationValue_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPresentationSize(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una unidad de medida de presentación NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPresentationUnit_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPresentationUnit(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un costo NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullCost_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductCost(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta mínimo NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullMinPrice_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setMinPrice(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullCurrentPrice_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCurrentPrice(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio mayorista NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullWholeSalePrice_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductWholeSalePrice(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de descuento máximo permitido NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullMaxDiscountPercentage_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setMaxDiscountPercentage(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una categoría NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullCategory_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCategory(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un stock actual NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullCurrentStock_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCurrentStockLevel(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un stock mínimo NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullSafetyStockLevel_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setSafetyStockLevel(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una ruta de archivo de imagen NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullFilePathString_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setImageFilePath(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre con caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PRODUCT_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud mínima, la validación deberá fallar y arrojara ConstraintViolationException")
    void givenNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PRODUCT_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(PRODUCT_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankBrandName_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName("");

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca compuesto por caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameWithInvalidCharacters_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName(PRODUCT_INVALID_BRAND_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName(PRODUCT_NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBrandName(PRODUCT_NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankOptionalDescription_WhenCreating_ThenDoesNotThrowAnything() {

        inputDTO.setOptionalDescription("");

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción que supere la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenOptionalDescriptionTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setOptionalDescription(PRODUCT_OPTIONAL_DESCRIPTION_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una medida de presentación menor o igual a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPresentationSizeLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setPresentationSize(PRODUCT_INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de costo negativo o cero, la validación deberá fallar y arrojara ConstraintViolationException")
    void givenCostLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductCost(PRODUCT_NEGATIVE_COST);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio actual negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenCurrentPriceLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCurrentPrice(PRODUCT_NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta mínimo negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenMinPriceLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setMinPrice(PRODUCT_NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio mayorista negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenWholeSalePriceLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setProductWholeSalePrice(PRODUCT_NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta actual menor al costo del producto, la validación deberá fallar y arrojará InvalidProductCurrentPriceException")
    void givenCurrentPriceLowerThanCost_WhenCreating_ThenThrows_InvalidProductCurrentPriceException() {

        inputDTO.setCurrentPrice(PRODUCT_COST - 100.0);

        assertThrows(InvalidProductCurrentPriceException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta igual al precio de costo del producto, la validación deberá fallar y arrojará InvalidProductCurrentPriceException")
    void givenCurrentPriceEqualsThanCost_WhenCreating_ThenThrows_InvalidProductCurrentPriceException() {

        inputDTO.setCurrentPrice(PRODUCT_COST);

        assertThrows(InvalidProductCurrentPriceException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de porcentaje de descuento máximo menor que cero, la validación fallará y arrojará ConstraintViolationException")
    void givenMaxDiscountPercentageLowerThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setMaxDiscountPercentage(PRODUCT_DISCOUNT_PERCENTAGE_LOWER_THAN_ZERO);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de porcentaje de descuento máximo mayot que uno, la validación fallará y arrojará ConstraintViolationException")
    void givenMaxDiscountPercentageHigherThanOne_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setMaxDiscountPercentage(PRODUCT_DISCOUNT_PERCENTAGE_HIGHER_THAN_ONE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un stock actual menor a cero, la validación fallará y arrojará ConstraintViolationException")
    void givenCurrentStockLowerThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setCurrentStockLevel(PRODUCT_CURRENT_STOCK_LEVEL * (-1));

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un stock mínimo menor que cero, la validación fallará y arrojará ConstraintViolationException")
    void givenSafetyStockLevelLowerThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setSafetyStockLevel(PRODUCT_SAFETY_STOCK_LEVEL * (-1));

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una ruta de archivo en blanco, se asumirá que no tendrá archivo asociado y la validación deberá ser exitosa")
    void givenBlankFilePath_WhenCreating_ThenDoesNotThrowAnything() {

        inputDTO.setImageFilePath("");

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidProductCreationDTO();
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
