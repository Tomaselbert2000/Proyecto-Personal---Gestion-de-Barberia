package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class EmployeeServicesCompletedStatsDTO {

    private String employeFirstName;
    private String employeLastName;
    private Long totalServices;
}
