package com.mapper.product;

import com.dto.product.ProductCreationDTO;
import com.mapper.implementation.ProductMapperImpl;
import com.mapper.interfaces.ProductMapper;
import com.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.ProductTestDataFactory.buildValidProductCreationDTO;
import static com.test_constant.ProductTestConstants.CreationValidData.*;
import static com.test_constant.ProductTestConstants.MapperData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperCreationTest {

    private final ProductMapper productMapper = new ProductMapperImpl();

    private final ProductCreationDTO creationDTO = buildValidProductCreationDTO();

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de producto con espacios innecesarios, al mapear serán eliminados")
    void givenNameWithSpaces_ThenIsTrimmed() {

        creationDTO.setName(PRODUCT_NAME_WITH_SPACES);

        Product mappedProduct = mapEntity();

        assertEquals(PRODUCT_NAME, mappedProduct.getName());
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de producto en minúsculas, al mapear se agregará la mayúscula")
    void givenLowercaseName_ThenIsCapitalized() {

        creationDTO.setName(LOWERCASE_PRODUCT_NAME);

        Product mappedProduct = mapEntity();

        assertEquals(PRODUCT_NAME, mappedProduct.getName());
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca con espacios innecesarios, al mapear serán eliminados")
    void givenBrandNameWithUnnecessarySpaces_WhenCreating_ThenIsTrimmed() {

        creationDTO.setBrandName(PRODUCT_BRAND_NAME_WITH_SPACES);

        Product mappedProduct = mapEntity();

        assertEquals(PRODUCT_BRAND_NAME, mappedProduct.getBrandName());
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de marca en minúsculas, al mapear se agregará la mayúscula")
    void givenLowercaseBrandName_WhenCreating_ThenIsCapitalized() {

        creationDTO.setBrandName(LOWERCASE_BRAND_NAME);

        Product mappedProduct = mapEntity();

        assertEquals(PRODUCT_BRAND_NAME, mappedProduct.getBrandName());
    }

    @Test
    @DisplayName("Dado un DTO de creaicón con una descripción opcional con espacios innecesarios, al mapear serán eliminados")
    void givenOptionalDescriptionWithUnnecessarySpaces_WhenCreating_ThenIsTrimmed() {

        creationDTO.setOptionalDescription(PRODUCT_OPTIONAL_DESCRIPTION_WITH_SPACES);

        Product mappedProduct = mapEntity();

        assertEquals(PRODUCT_OPTIONAL_DESCRIPTION, mappedProduct.getOptionalDescription());
    }

    private Product mapEntity() {
        return productMapper.mapProductCreationDTOtoEntity(creationDTO);
    }
}
