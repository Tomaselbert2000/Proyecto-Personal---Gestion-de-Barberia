package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockValueStatsDTO {

    private Double totalStockValue;
    private Long totalUnits;
}
