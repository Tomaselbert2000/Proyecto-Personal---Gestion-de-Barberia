package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodUsageStatsDTO {

    private String paymentMethodName;
    private Long amountOfSalesWhereIsUsed;
}
