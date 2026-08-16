package com.dto.stats;

import lombok.*;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PaymentMethodRevenueStatsDTO {

    @Builder.Default
    private String paymentMethod = NO_DATA;
    @Builder.Default
    private double revenueAmount = 0.0;
}
