package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class EmployeeRevenueStatsDTO {

    private String employeeFirstname;
    private String employeeLastname;
    private Double totalRevenue;
}
