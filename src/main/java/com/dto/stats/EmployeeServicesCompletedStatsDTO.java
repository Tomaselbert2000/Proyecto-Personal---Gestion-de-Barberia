package com.dto.stats;

import lombok.*;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class EmployeeServicesCompletedStatsDTO {

    @Builder.Default
    private String employeFirstName = NO_DATA;
    @Builder.Default
    private String employeLastName = "";
    @Builder.Default
    private Long totalServices = 0L;
}
