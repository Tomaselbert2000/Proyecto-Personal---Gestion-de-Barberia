package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppointmentTodayStatsDTO {

    @Builder.Default
    private Long appointmentCount = 0L;
    @Builder.Default
    private Long totalAmountAsFinished = 0L;
}
