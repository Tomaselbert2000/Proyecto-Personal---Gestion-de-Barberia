package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class MonthlyAppointmentCountDTO {

    private Long employeeID;
    private Long appointmentCount;
}
