package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientAcquisitionStatsDTO {

    public ClientAcquisitionStatsDTO(Long newClientsThisMonth, Long newClientsLastMonth) {
        this.newClientsThisMonth = newClientsThisMonth;
        this.newClientsLastMonth = newClientsLastMonth;
    }

    private Long newClientsThisMonth;
    private Long newClientsLastMonth;
    private Double percentageVsLastMonth = 0.0;
}
