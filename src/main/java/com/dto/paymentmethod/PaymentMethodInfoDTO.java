package com.dto.paymentmethod;

import com.enums.PaymentMethodModifierType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PaymentMethodInfoDTO {

    private Long id;
    private String name;
    private String description;
    private PaymentMethodModifierType modifierType;
    private Double priceModifier;
    private Boolean isActive;
}
