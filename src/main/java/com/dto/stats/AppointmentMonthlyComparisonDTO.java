package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppointmentMonthlyComparisonDTO {

    private Long currentMonthAppointments;
    private Long previousMonthAppointments;
}
