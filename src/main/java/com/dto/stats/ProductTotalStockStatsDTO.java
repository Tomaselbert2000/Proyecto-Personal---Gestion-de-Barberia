package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductTotalStockStatsDTO {

    @Builder.Default
    private Long productCount = 0L;
    @Builder.Default
    private Long onLowOrCriticalStockCount = 0L;
}
