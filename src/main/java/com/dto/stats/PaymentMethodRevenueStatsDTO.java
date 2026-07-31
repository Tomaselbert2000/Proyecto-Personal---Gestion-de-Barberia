package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodRevenueStatsDTO {

    private String paymentMethod;
    private double revenueAmount;
}
