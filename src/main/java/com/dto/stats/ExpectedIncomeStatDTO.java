package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ExpectedIncomeStatDTO {

    @Builder.Default
    private Long appointmentsToday = 0L;
    @Builder.Default
    private Double expectedIncomeSumForToday = 0.0;
    @Builder.Default
    private Double averageTicket = 0.0;

}
