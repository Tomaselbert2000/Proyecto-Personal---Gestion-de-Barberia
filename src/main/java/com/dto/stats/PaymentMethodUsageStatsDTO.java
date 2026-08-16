package com.dto.stats;

import lombok.*;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PaymentMethodUsageStatsDTO {

    @Builder.Default
    private String paymentMethodName = NO_DATA;
    @Builder.Default
    private Long amountOfSalesWhereIsUsed = 0L;
}
