package com.mapper.product;

import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.StockStatus;
import com.barbershop.mapper.implementation.ProductMapperImpl;
import com.barbershop.mapper.interfaces.ProductMapper;
import com.barbershop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductMapperInfoTest {

    private Product product;

    private final ProductMapper mapper = new ProductMapperImpl();

    @BeforeEach
    void init() {

        product = Product.builder()
                .productID(1L)
                .name("Cera para modelar")
                .productCost(100.0)
                .currentPrice(150.0)
                .category(ProductCategory.CERA)
                .currentStockLevel(100)
                .safetyStockLevel(30)
                .build();
    }

    @Test
    void givenProductWith_Cost100_Price150_ThenCurrentProfitIs50() {

        ProductInfoDTO infoDTO = mapper.mapProductToInfoDTO(product);

        assertEquals(50.0, infoDTO.getCurrentProfitPercentage());
    }

    @Test
    void givenProductWith_Cost0_ThenByDefault_CurrentProfitReturnsZero() {

        product.setProductCost(0.0);

        ProductInfoDTO infoDTO = mapper.mapProductToInfoDTO(product);

        assertEquals(0.0, infoDTO.getCurrentProfitPercentage());
    }

    @Test
    void givenProductWith_Cost200_Price100_ThenCurrentProfitIsNegative() {

        product.setProductCost(200.0);
        product.setCurrentPrice(100.0);

        ProductInfoDTO infoDTO = mapper.mapProductToInfoDTO(product);

        assertEquals(-50.0, infoDTO.getCurrentProfitPercentage());
    }

    @Test
    void givenProductCurrentStock_Below_25_PercentOfSafetyStockLevel_ThenStockStatusIs_CRITICO() {

        product.setSafetyStockLevel(100);
        product.setCurrentStockLevel(20);

        ProductInfoDTO infoDTO = mapper.mapProductToInfoDTO(product);

        assertEquals(StockStatus.CRITICO, infoDTO.getCurrentStockStatus());
    }

    @Test
    void givenProductCurrentStock_LowerOrEquals_ThanSafetyStockLevel_ThenStockStatusIs_BAJO() {

        product.setSafetyStockLevel(100);
        product.setCurrentStockLevel(50);

        ProductInfoDTO infoDTO = mapper.mapProductToInfoDTO(product);

        assertEquals(StockStatus.BAJO, infoDTO.getCurrentStockStatus());
    }

    @Test
    void givenProductCurrentStock_LowerOrEquals_Than3xSafetyStockLevel_ThenStockStatusIs_ATENCION() {

        product.setSafetyStockLevel(100);
        product.setCurrentStockLevel(150);

        ProductInfoDTO infoDTO = mapper.mapProductToInfoDTO(product);

        assertEquals(StockStatus.ATENCION, infoDTO.getCurrentStockStatus());
    }

    @Test
    void givenProductCurrentStock_HigherThan_DoubleSafetyStockLevel_ThenStockStatusIs_EXCEDIDO() {

        product.setSafetyStockLevel(100);
        product.setCurrentStockLevel(210);

        ProductInfoDTO infoDTO = mapper.mapProductToInfoDTO(product);

        assertEquals(StockStatus.EXCEDIDO, infoDTO.getCurrentStockStatus());
    }

    @Test
    void givenNormalCurrentStock_ThenStockStatusIs_SUFICIENTE() {

        product.setSafetyStockLevel(20);
        product.setCurrentStockLevel(35);

        ProductInfoDTO infoDTO = mapper.mapProductToInfoDTO(product);

        assertEquals(StockStatus.SUFICIENTE, infoDTO.getCurrentStockStatus());
    }
}
