package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class TotalClientsStatsDTO {

    @Builder.Default
    private Long totalClientsCount = 0L;
    @Builder.Default
    private Long clientsRegisteredThisMonth = 0L;
}
