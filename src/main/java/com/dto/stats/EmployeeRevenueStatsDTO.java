package com.dto.stats;

import lombok.*;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class EmployeeRevenueStatsDTO {

    @Builder.Default
    private String employeeFirstname = NO_DATA;
    @Builder.Default
    private String employeeLastname = NO_DATA;
    @Builder.Default
    private Double totalRevenue = 0.0;
}
