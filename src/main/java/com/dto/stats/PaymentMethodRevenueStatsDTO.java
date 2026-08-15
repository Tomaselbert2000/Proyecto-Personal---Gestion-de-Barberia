package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PaymentMethodRevenueStatsDTO {

    private String paymentMethod;
    private double revenueAmount;
}
