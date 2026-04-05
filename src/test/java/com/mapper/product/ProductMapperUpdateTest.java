package com.mapper.product;

import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.mapper.implementation.ProductMapperImpl;
import com.barbershop.mapper.interfaces.ProductMapper;
import com.barbershop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperUpdateTest {

    private final ProductMapper mapper = new ProductMapperImpl();

    private ProductUpdateDTO updateDTO;
    private Product product;

    @BeforeEach
    void init() {

        product = new Product();
        updateDTO = new ProductUpdateDTO();
    }

    @Test
    void givenNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        String productNameWithSpaces = "   Cera para modelar   ";

        updateDTO.setName(productNameWithSpaces);

        Product updatedProduct = mapper.mapProductUpdateDTOtoEntity(product, updateDTO);

        assertEquals("Cera para modelar", updatedProduct.getName());
    }

    @Test
    void givenLowercaseName_WhenUpdating_ThenIsCapitalized() {

        String lowercaseName = "shampoo";

        updateDTO.setName(lowercaseName);

        Product updatedProduct = mapper.mapProductUpdateDTOtoEntity(product, updateDTO);

        assertEquals("Shampoo", updatedProduct.getName());
    }

    @Test
    void givenNullName_WhenUpdating_ThenEntityKeepsCurrentName() {

        updateDTO.setName(null);
        product.setName("Producto 1");

        Product result = mapper.mapProductUpdateDTOtoEntity(product, updateDTO);

        assertEquals("Producto 1", result.getName());
    }

    @Test
    void givenNullProductCost_WhenUpdating_ThenEntityKeepsCurrentCost() {

        product.setProductCost(3500.0);

        updateDTO.setProductCost(null);

        Product result = mapper.mapProductUpdateDTOtoEntity(product, updateDTO);

        assertEquals(3500.0, result.getProductCost());
    }

    @Test
    void givenNullProductCategory_WhenUpdating_ThenEntityKeepsCurrentCategory() {

        product.setCategory(ProductCategory.HOJA_DE_AFEITAR);

        updateDTO.setCategory(null);

        Product result = mapper.mapProductUpdateDTOtoEntity(product, updateDTO);

        assertEquals(ProductCategory.HOJA_DE_AFEITAR, result.getCategory());
    }

    @Test
    void givenNullCurrentStockLevel_WhenUpdating_ThenEntityKeepsCurrentStockValue() {

        product.setCurrentStockLevel(150);

        updateDTO.setCurrentStockLevel(null);

        Product result = mapper.mapProductUpdateDTOtoEntity(product, updateDTO);

        assertEquals(150, result.getCurrentStockLevel());
    }

    @Test
    void givenNullSafetyStockLevel_WhenUpdating_ThenEntityKeepsCurrentSafetyStockValue() {

        product.setSafetyStockLevel(150);

        updateDTO.setSafetyStockLevel(null);

        Product result = mapper.mapProductUpdateDTOtoEntity(product, updateDTO);

        assertEquals(150, result.getSafetyStockLevel());
    }
}
