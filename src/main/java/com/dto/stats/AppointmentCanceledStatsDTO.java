package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentCanceledStatsDTO {

    public AppointmentCanceledStatsDTO(Long canceledAppointmentThisMonth, Long totalAppointmentsThisMonth) {
        this.canceledAppointmentThisMonth = canceledAppointmentThisMonth;
        this.totalAppointmentsThisMonth = totalAppointmentsThisMonth;
    }

    private Long canceledAppointmentThisMonth;
    private Long totalAppointmentsThisMonth;
    private Double canceledAppointmentPercentage = 0.0;
}
