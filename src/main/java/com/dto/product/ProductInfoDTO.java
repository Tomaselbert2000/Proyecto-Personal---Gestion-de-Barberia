package com.dto.product;

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
    private Double productCost;
    private Double currentPrice;
    private Double calculatedProfit;
    private Integer currentStockLevel;
    private Integer safetyStockLevel;
    private StockStatus currentStockStatus;
    private String imageFilePath;
}