package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ClientRegistrationTrendStatDTO {

    public ClientRegistrationTrendStatDTO(Long clientsRegisteredDuringThisMonth, Long clientsRegisteredDuringTheLastMonth) {

        this.clientsRegisteredDuringThisMonth = clientsRegisteredDuringThisMonth;
        this.clientsRegisteredDuringTheLastMonth = clientsRegisteredDuringTheLastMonth;
    }

    @Builder.Default
    private Long clientsRegisteredDuringThisMonth = 0L;
    @Builder.Default
    private Long clientsRegisteredDuringTheLastMonth = 0L;
    @Builder.Default
    private Double trendPercentage = 0.0;
}
