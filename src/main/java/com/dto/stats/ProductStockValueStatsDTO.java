package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductStockValueStatsDTO {

    private Double totalStockValue;
    private Long totalUnits;
}
