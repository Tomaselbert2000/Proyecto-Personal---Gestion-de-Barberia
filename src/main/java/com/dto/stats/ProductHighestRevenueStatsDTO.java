package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductHighestRevenueStatsDTO {

    private String productName;
    private Double revenue;
}
