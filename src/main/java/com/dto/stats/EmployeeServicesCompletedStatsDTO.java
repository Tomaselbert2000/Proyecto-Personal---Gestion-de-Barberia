package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeServicesCompletedStatsDTO {

    private String employeFirstName;
    private String employeLastName;
    private Long totalServices;
}
