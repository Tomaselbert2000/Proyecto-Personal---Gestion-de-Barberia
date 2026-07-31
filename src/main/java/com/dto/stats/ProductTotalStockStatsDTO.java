package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTotalStockStatsDTO {

    private Long productCount;
    private Long onLowOrCriticalStockCount;
}
