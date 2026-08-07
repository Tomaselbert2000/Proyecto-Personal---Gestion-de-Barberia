package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpectedIncomeStatDTO {

    private Long appointmentsToday;
    private Double expectedIncomeSumForToday;
    private Double averageTicket = 0.0;

    public ExpectedIncomeStatDTO(Long appointmentsToday, Double expectedIncomeSumForToday) {

        this.appointmentsToday = appointmentsToday;
        this.expectedIncomeSumForToday = expectedIncomeSumForToday;
    }
}
