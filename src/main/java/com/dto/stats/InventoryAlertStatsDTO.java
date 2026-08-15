package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class InventoryAlertStatsDTO {

    private Long lowStockProductsCount;
    private Long outOfStockProductsCount;
}
