package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceRevenueStatsDTO {

    private String barberServiceName;
    private Double totalRevenue;
}
