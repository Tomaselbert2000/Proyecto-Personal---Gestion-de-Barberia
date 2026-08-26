package com.dto.stats;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyIncomeStatsDTO {

    public MonthlyIncomeStatsDTO(Double currentMonthTotal, Double lastMonthTotal) {

        this.currentMonthTotal = BigDecimal.valueOf(currentMonthTotal);
        this.lastMonthTotal = BigDecimal.valueOf(lastMonthTotal);
    }

    @Builder.Default
    private BigDecimal currentMonthTotal = BigDecimal.valueOf(0.0);

    @Builder.Default
    private BigDecimal lastMonthTotal = BigDecimal.valueOf(0.0);

    @Builder.Default
    private Double percentageTrendVsLastMonth = 0.0;
}