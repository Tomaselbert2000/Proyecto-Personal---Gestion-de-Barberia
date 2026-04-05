package com.barbershop.dto.payment;

import com.barbershop.enums.PaymentMethodModifierType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodCreationDTO {

    private String name;
    private String description;
    private PaymentMethodModifierType priceModifierType;
    private Double priceModifier;
}
