package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppointmentTodayStatsDTO {

    private Long appointmentCount;
    private Long totalAmountAsFinished;
}
