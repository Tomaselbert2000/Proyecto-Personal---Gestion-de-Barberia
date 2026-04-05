package com.validation.tests.product;

import com.barbershop.dto.product.ProductCreationDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.exceptions.product.*;
import com.barbershop.validation.product.ProductValidator;
import com.validation.common.ValidatorCreationTestFunctions;
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
public class ProductValidatorCreationTest implements ValidatorCreationTestFunctions {

    private final Validator validatorEngine = Validation.buildDefaultValidatorFactory().getValidator();
    private final ProductValidator validator = new ProductValidator(validatorEngine);

    private ProductCreationDTO creationDTO;

    @BeforeEach
    public void init() {

        setupCreationDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos válidos, la validación deberá ser exitosa")
    void givenDTOWithValidData_WhenCreating_ThenDoesNotThrowAnything() {

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, la validación deberá fallar y arrojar NullDTOException")
    void givenNullDTO_WhenCreating_ThenThrows_NullDTOException() {

        creationDTO = null;

        assertThrows(NullDTOException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una marca NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullBrandName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setBrandName(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación cuyo valor de presentación NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPresentationValue_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPresentationSize(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una unidad de medida de presentación NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullPresentationUnit_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPresentationUnit(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un costo NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullCost_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setProductCost(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta mínimo NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullMinPrice_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setMinPrice(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullCurrentPrice_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setCurrentPrice(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio mayorista NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullWholeSalePrice_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setProductWholeSalePrice(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de descuento máximo permitido NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullMaxDiscountPercentage_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setMaxDiscountPercentage(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una categoría NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullCategory_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setCategory(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un stock actual NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullCurrentStock_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setCurrentStockLevel(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un stock mínimo NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullSafetyStockLevel_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setSafetyStockLevel(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una ruta de archivo de imagen NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullFilePathString_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setImageFilePath(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre con caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenInvalidName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName(INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud mínima, la validación deberá fallar y arrojara ConstraintViolationException")
    void givenNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca en blanco, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBlankBrandName_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setBrandName("");

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca compuesto por caractéres inválidos, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameWithInvalidCharacters_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setBrandName(INVALID_BRAND_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca que no cumpla la longitud mínima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameTooShort_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setBrandName(NAME_TOO_SHORT);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca que no cumpla la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenBrandNameTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setBrandName(NAME_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción en blanco, la validación deberá ser exitosa y no arrojará excepción")
    void givenBlankOptionalDescription_WhenCreating_ThenDoesNotThrowAnything() {

        creationDTO.setOptionalDescription("");

        assertDoesNotThrow(this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción que supere la longitud máxima, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenOptionalDescriptionTooLong_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setOptionalDescription(OPTIONAL_DESCRIPTION_TOO_LONG);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una medida de presentación menor o igual a cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenPresentationSizeLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setPresentationSize(INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de costo negativo o cero, la validación deberá fallar y arrojara ConstraintViolationException")
    void givenCostLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setProductCost(NEGATIVE_COST);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio actual negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenCurrentPriceLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setCurrentPrice(NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta mínimo negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenMinPriceLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setMinPrice(NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio mayorista negativo o cero, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenWholeSalePriceLowerOrEqualsThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setProductWholeSalePrice(NEGATIVE_PRICE);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta actual menor al costo del producto, la validación deberá fallar y arrojará InvalidProductCurrentPriceException")
    void givenCurrentPriceLowerThanCost_WhenCreating_ThenThrows_InvalidProductCurrentPriceException() {

        creationDTO.setCurrentPrice(COST - 100.0);

        assertThrows(InvalidProductCurrentPriceException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta igual al precio de costo del producto, la validación deberá fallar y arrojará InvalidProductCurrentPriceException")
    void givenCurrentPriceEqualsThanCost_WhenCreating_ThenThrows_InvalidProductCurrentPriceException() {

        creationDTO.setCurrentPrice(COST);

        assertThrows(InvalidProductCurrentPriceException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de porcentaje de descuento máximo menor que cero, la validación fallará y arrojará ConstraintViolationException")
    void givenMaxDiscountPercentageLowerThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setMaxDiscountPercentage(DISCOUNT_PERCENTAGE_LOWER_THAN_ZERO);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de porcentaje de descuento máximo mayot que uno, la validación fallará y arrojará ConstraintViolationException")
    void givenMaxDiscountPercentageHigherThanOne_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setMaxDiscountPercentage(DISCOUNT_PERCENTAGE_HIGHER_THAN_ONE);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un stock actual menor a cero, la validación fallará y arrojará ConstraintViolationException")
    void givenCurrentStockLowerThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setCurrentStockLevel(CURRENT_STOCK_LEVEL * (-1));

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un stock mínimo menor que cero, la validación fallará y arrojará ConstraintViolationException")
    void givenSafetyStockLevelLowerThanZero_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setSafetyStockLevel(SAFETY_STOCK_LEVEL * (-1));

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una ruta de archivo en blanco, se asumirá que no tendrá archivo asociado y la validación deberá ser exitosa")
    void givenBlankFilePath_WhenCreating_ThenDoesNotThrowAnything() {

        creationDTO.setImageFilePath("");

        assertDoesNotThrow(this::validateForCreation);
    }

    public void setupCreationDTO() {

        creationDTO = ProductCreationDTO.builder()
                .name(NAME)
                .optionalDescription(OPTIONAL_DESCRIPTION)
                .brandName(BRAND_NAME)
                .presentationSize(PRESENTATION_SIZE)
                .presentationUnit(UNIT)
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

    public void validateForCreation() {

        validator.validateDTO(creationDTO);
    }
}
