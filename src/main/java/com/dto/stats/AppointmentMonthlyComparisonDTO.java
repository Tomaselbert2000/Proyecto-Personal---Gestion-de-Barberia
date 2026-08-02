package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentMonthlyComparisonDTO {

    private Long currentMonthAppointments;
    private Long previousMonthAppointments;
}
