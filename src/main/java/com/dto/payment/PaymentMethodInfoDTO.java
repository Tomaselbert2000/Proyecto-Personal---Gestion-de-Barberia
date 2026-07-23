package com.dto.payment;

import com.enums.PaymentMethodModifierType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodInfoDTO {

    private String name;
    private String description;
    private PaymentMethodModifierType modifierType;
    private Double priceModifier;
    private Boolean isActive;
}
