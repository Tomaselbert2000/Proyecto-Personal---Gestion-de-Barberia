package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BarberServiceSalesStatsDTO {

    private String barberServiceName;
    private Long amountOfSales;
}
