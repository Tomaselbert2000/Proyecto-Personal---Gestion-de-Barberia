package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PaymentMethodUsageStatsDTO {

    private String paymentMethodName;
    private Long amountOfSalesWhereIsUsed;
}
