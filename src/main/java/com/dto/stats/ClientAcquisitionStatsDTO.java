package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ClientAcquisitionStatsDTO {

    public ClientAcquisitionStatsDTO(Long newClientsThisMonth, Long newClientsLastMonth) {
        this.newClientsThisMonth = newClientsThisMonth;
        this.newClientsLastMonth = newClientsLastMonth;
    }

    @Builder.Default
    private Long newClientsThisMonth = 0L;
    @Builder.Default
    private Long newClientsLastMonth = 0L;
    @Builder.Default
    private Double percentageVsLastMonth = 0.0;
}
