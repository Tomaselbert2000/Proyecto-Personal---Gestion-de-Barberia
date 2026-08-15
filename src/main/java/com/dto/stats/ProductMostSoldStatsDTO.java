package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductMostSoldStatsDTO {

    private String productName;
    private Long unitsSold;
}
