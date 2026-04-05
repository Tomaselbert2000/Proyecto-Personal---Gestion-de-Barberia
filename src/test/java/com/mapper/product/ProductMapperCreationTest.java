package com.mapper.product;

import com.barbershop.dto.product.ProductCreationDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.mapper.implementation.ProductMapperImpl;
import com.barbershop.mapper.interfaces.ProductMapper;
import com.barbershop.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperCreationTest {

    private final ProductMapper productMapper = new ProductMapperImpl();


    @Test
    void givenNameWithSpaces_ThenIsTrimmed() {

        String productNameWithSpaces = "   Shampoo   ";

        ProductCreationDTO creationDTO = ProductCreationDTO.builder().name(productNameWithSpaces)
                .productCost(1200.0)
                .category(ProductCategory.POMADA)
                .currentStockLevel(125)
                .safetyStockLevel(40)
                .build();

        Product mappedProduct = productMapper.mapProductCreationDTOtoEntity(creationDTO);

        assertEquals("Shampoo", mappedProduct.getName());
    }

    @Test
    void givenLowercaseName_ThenIsCapitalized() {

        String lowercaseName = "shampoo";

        ProductCreationDTO creationDTO = ProductCreationDTO.builder().name(lowercaseName)
                .productCost(1200.0)
                .category(ProductCategory.POMADA)
                .currentStockLevel(125)
                .safetyStockLevel(40)
                .build();

        Product mappedProduct = productMapper.mapProductCreationDTOtoEntity(creationDTO);

        assertEquals("Shampoo", mappedProduct.getName());
    }
}
