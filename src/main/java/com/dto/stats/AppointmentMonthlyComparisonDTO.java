package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppointmentMonthlyComparisonDTO {

    @Builder.Default
    private Long currentMonthAppointments = 0L;
    @Builder.Default
    private Long previousMonthAppointments = 0L;
}
