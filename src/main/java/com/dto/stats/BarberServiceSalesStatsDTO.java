package com.dto.stats;

import lombok.*;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BarberServiceSalesStatsDTO {

    @Builder.Default
    private String barberServiceName = NO_DATA;
    @Builder.Default
    private Long amountOfSales = 0L;
}
