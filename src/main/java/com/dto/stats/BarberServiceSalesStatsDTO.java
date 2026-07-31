package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceSalesStatsDTO {

    private String barberServiceName;
    private Long amountOfSales;
}
