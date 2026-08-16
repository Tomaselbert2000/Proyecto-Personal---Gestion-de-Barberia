package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class InventoryAlertStatsDTO {

    @Builder.Default
    private Long lowStockProductsCount = 0L;
    @Builder.Default
    private Long outOfStockProductsCount = 0L;
}
