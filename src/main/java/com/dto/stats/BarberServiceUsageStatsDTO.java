package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BarberServiceUsageStatsDTO {

    private String barberServiceName;
    private Long totalUsage;
}
