package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppointmentTomorrowStatsDTO {

    private Long totalPendingAppointments;
    private Long scheduledAppointmentsTomorrow;
}
