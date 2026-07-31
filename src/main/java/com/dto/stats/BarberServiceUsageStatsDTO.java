package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceUsageStatsDTO {

    private String barberServiceName;
    private Long totalUsage;
}
