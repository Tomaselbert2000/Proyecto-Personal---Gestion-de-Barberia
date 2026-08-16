package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppointmentTomorrowStatsDTO {

    @Builder.Default
    private Long totalPendingAppointments = 0L;
    @Builder.Default
    private Long scheduledAppointmentsTomorrow = 0L;
}
