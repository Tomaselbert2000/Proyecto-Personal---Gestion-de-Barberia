package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryAlertStatsDTO {

    private Long lowStockProductsCount;
    private Long outOfStockProductsCount;
}
