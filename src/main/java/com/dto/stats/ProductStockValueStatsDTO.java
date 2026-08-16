package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductStockValueStatsDTO {

    @Builder.Default
    private Double totalStockValue = 0.0;
    @Builder.Default
    private Long totalUnits = 0L;
}
