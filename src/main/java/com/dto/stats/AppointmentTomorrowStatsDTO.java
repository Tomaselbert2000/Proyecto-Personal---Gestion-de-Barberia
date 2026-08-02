package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentTomorrowStatsDTO {

    private Long totalPendingAppointments;
    private Long scheduledAppointmentsTomorrow;
}
