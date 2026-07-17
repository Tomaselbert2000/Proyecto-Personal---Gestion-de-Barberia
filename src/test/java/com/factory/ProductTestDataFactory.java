package com.factory;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductUpdateDTO;
import com.model.Product;

import static com.test_constant.ProductTestConstants.CreationValidData.*;
import static com.test_constant.ProductTestConstants.UpdateValidData.*;

public class ProductTestDataFactory {

    private ProductTestDataFactory() {
    }

    public static ProductCreationDTO buildValidProductCreationDTO() {

        return ProductCreationDTO.builder()
                .name(PRODUCT_NAME)
                .optionalDescription(PRODUCT_OPTIONAL_DESCRIPTION)
                .brandName(PRODUCT_BRAND_NAME)
                .presentationUnit(PRODUCT_PRESENTATION_UNIT)
                .presentationSize(PRODUCT_PRESENTATION_SIZE)
                .productCost(PRODUCT_COST)
                .minPrice(PRODUCT_MIN_PRICE)
                .currentPrice(PRODUCT_CURRENT_PRICE)
                .productWholeSalePrice(PRODUCT_WHOLE_SALE_PRICE)
                .maxDiscountPercentage(PRODUCT_MAX_DISCOUNT_PERCENTAGE_VALUE)
                .category(PRODUCT_CATEGORY)
                .currentStockLevel(PRODUCT_CURRENT_STOCK_LEVEL)
                .safetyStockLevel(PRODUCT_SAFETY_STOCK_LEVEL)
                .imageFilePath(PRODUCT_FILE_PATH)
                .build();
    }

    public static ProductUpdateDTO buildValidProductUpdateDTO() {

        return ProductUpdateDTO.builder()
                .name(NEW_PRODUCT_NAME)
                .optionalDescription(NEW_OPTIONAL_DESCRIPTION)
                .brandName(NEW_PRODUCT_BRAND_NAME)
                .presentationUnit(NEW_PRODUCT_PRESENTATION_UNIT)
                .presentationSize(NEW_PRODUCT_PRESENTATION_SIZE)
                .productCost(NEW_PRODUCT_COST)
                .minPrice(NEW_PRODUCT_MIN_PRICE)
                .currentPrice(NEW_PRODUCT_CURRENT_PRICE)
                .productWholeSalePrice(NEW_PRODUCT_WHOLE_SALE_PRICE)
                .maxDiscountPercentage(NEW_PRODUCT_MAX_DISCOUNT_PERCENTAGE_VALUE)
                .category(NEW_PRODUCT_CATEGORY)
                .currentStockLevel(NEW_PRODUCT_CURRENT_STOCK_LEVEL)
                .safetyStockLevel(NEW_PRODUCT_SAFETY_STOCK_LEVEL)
                .imageFilePath(NEW_PRODUCT_FILE_PATH).
                build();
    }

    public static Product buildValidProduct(){

        return Product.builder()
                .productID(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .optionalDescription(PRODUCT_OPTIONAL_DESCRIPTION)
                .brandName(PRODUCT_BRAND_NAME)
                .presentationUnit(PRODUCT_PRESENTATION_UNIT)
                .presentationSize(PRODUCT_PRESENTATION_SIZE)
                .productCost(PRODUCT_COST)
                .minPrice(PRODUCT_MIN_PRICE)
                .currentPrice(PRODUCT_CURRENT_PRICE)
                .productWholeSalePrice(PRODUCT_WHOLE_SALE_PRICE)
                .maxDiscountPercentage(PRODUCT_MAX_DISCOUNT_PERCENTAGE_VALUE)
                .currentStockLevel(PRODUCT_CURRENT_STOCK_LEVEL)
                .safetyStockLevel(PRODUCT_SAFETY_STOCK_LEVEL)
                .category(PRODUCT_CATEGORY)
                .creationDate(PRODUCT_CREATION_DATE)
                .imageFilePath(PRODUCT_FILE_PATH)
                .build();
    }
}
