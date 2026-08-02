package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentTodayStatsDTO {

    private Long appointmentCount;
    private Long totalAmountAsFinished;
}
