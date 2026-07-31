package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductHighestRevenueStatsDTO {

    private String productName;
    private Double revenue;
}
