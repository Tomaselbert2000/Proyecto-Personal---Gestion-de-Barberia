package com.barbershop.dto.product;

import com.barbershop.enums.StockStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInfoDTO {

    private String name;
    private Double productCost;
    private Double currentPrice;
    private Double currentProfitPercentage;
    private Integer currentStockLevel;
    private Integer safetyStockLevel;
    private StockStatus currentStockStatus;
    private String imageFilePath;
}