package com.dto.stats;

import lombok.*;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductHighestRevenueStatsDTO {

    @Builder.Default
    private String productName = NO_DATA;
    @Builder.Default
    private Double revenue = 0.0;
}
