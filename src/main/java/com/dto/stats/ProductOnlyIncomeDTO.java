package com.dto.stats;

import lombok.*;

import java.math.BigDecimal;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOnlyIncomeDTO {

    @Builder.Default
    private BigDecimal productTotalIncome = BigDecimal.valueOf(0.0);

    @Builder.Default
    private String mostSoldProductName = NO_DATA;
}
