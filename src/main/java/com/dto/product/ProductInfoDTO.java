package com.dto.product;

import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;
import com.enums.StockStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductInfoDTO {

    private Long id;
    private String name;
    private String brandName;
    private String optionalDescription;
    private ProductPresentationUnit presentationUnit;
    private ProductCategory category;
    private Integer presentationSize;
    private Double productCost;
    private Double currentPrice;
    private Double minPrice;
    private Double wholeSalePrice;
    private Double maxDiscountPercentage;
    private Double calculatedProfit;
    private Integer currentStockLevel;
    private Integer safetyStockLevel;
    private StockStatus currentStockStatus;
    private String imageFilePath;
}