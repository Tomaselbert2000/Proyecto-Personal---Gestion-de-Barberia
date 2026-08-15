package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BarberServiceRevenueStatsDTO {

    private String barberServiceName;
    private Double totalRevenue;
}
