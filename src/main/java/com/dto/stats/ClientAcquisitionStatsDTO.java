package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ClientAcquisitionStatsDTO {

    @Builder.Default
    private Long newClientsThisMonth = 0L;
    @Builder.Default
    private Long newClientsLastMonth = 0L;
    @Builder.Default
    private Double percentageVsLastMonth = 0.0;
}
