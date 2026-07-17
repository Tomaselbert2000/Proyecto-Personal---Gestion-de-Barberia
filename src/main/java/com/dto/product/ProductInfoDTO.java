package com.dto.product;

import com.enums.StockStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfoDTO {

    private Long id;
    private String name;
    private Double productCost;
    private Double currentPrice;
    private Double currentProfitPercentage;
    private Integer currentStockLevel;
    private Integer safetyStockLevel;
    private StockStatus currentStockStatus;
    private String imageFilePath;
}