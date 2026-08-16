package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppointmentCanceledStatsDTO {

    public AppointmentCanceledStatsDTO(Long canceledAppointmentThisMonth, Long totalAppointmentsThisMonth) {
        this.canceledAppointmentThisMonth = canceledAppointmentThisMonth;
        this.totalAppointmentsThisMonth = totalAppointmentsThisMonth;
    }

    @Builder.Default
    private Long canceledAppointmentThisMonth = 0L;
    @Builder.Default
    private Long totalAppointmentsThisMonth = 0L;
    @Builder.Default
    private Double canceledAppointmentPercentage = 0.0;
}
