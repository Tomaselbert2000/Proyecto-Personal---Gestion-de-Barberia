package com.dto.stats;

import lombok.*;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BarberServiceUsageStatsDTO {

    @Builder.Default
    private String barberServiceName = NO_DATA;
    @Builder.Default
    private Long totalUsage = 0L;
}
