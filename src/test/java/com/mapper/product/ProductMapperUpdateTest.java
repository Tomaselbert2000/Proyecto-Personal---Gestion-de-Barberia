package com.mapper.product;

import com.dto.product.ProductUpdateDTO;
import com.mapper.implementation.ProductMapperImpl;
import com.mapper.interfaces.ProductMapper;
import com.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.ProductTestDataFactory.buildValidProduct;
import static com.factory.ProductTestDataFactory.buildValidProductUpdateDTO;
import static com.test_constant.ProductTestConstants.CreationValidData.*;
import static com.test_constant.ProductTestConstants.MapperData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperUpdateTest {

    private final ProductMapper mapper = new ProductMapperImpl();

    private final ProductUpdateDTO updateDTO = buildValidProductUpdateDTO();
    private final Product product = buildValidProduct();

    @Test
    @DisplayName("Dado un DTO de actualización con nombre de producto con espacios innecesarios, al mapear serán eliminados")
    void givenNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setName(PRODUCT_NAME_WITH_SPACES);

        Product updatedProduct = mapEntity(product, updateDTO);

        assertEquals(PRODUCT_NAME, updatedProduct.getName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombre de producto en minúsculas, al mapear se agregará la mayúscula correspondiente")
    void givenLowercaseName_WhenUpdating_ThenIsCapitalized() {

        updateDTO.setName(LOWERCASE_PRODUCT_NAME);

        Product updatedProduct = mapEntity(product, updateDTO);

        assertEquals(PRODUCT_NAME, updatedProduct.getName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombre de marca con espacios innecesarios, al mapear serán eliminados")
    void givenBrandNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setBrandName(PRODUCT_BRAND_NAME_WITH_SPACES);

        Product updatedProduct = mapEntity(product, updateDTO);

        assertEquals(PRODUCT_BRAND_NAME, updatedProduct.getBrandName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre de marca en minúsculas, al mapear se agregará la mayúscula correspondiente")
    void givenLowercaseBrandName_WhenUpdating_ThenIsCapitalized() {

        updateDTO.setBrandName(LOWERCASE_BRAND_NAME);

        Product updatedProduct = mapEntity(product, updateDTO);

        assertEquals(PRODUCT_BRAND_NAME, updatedProduct.getBrandName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un string de descripción opcional con espacios innecesarios, al mapear serán eliminados")
    void givenOptionalDescriptionWithUnnecessarySpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setOptionalDescription(PRODUCT_OPTIONAL_DESCRIPTION_WITH_SPACES);

        Product updatedProduct = mapEntity(product, updateDTO);

        assertEquals(PRODUCT_OPTIONAL_DESCRIPTION, updatedProduct.getOptionalDescription());
    }

    private Product mapEntity(Product product, ProductUpdateDTO updateDTO) {
        return mapper.mapProductUpdateDTOtoEntity(product, updateDTO);
    }
}
