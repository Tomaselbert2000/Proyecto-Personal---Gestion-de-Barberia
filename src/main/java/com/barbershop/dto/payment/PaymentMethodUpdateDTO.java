package com.barbershop.dto.payment;

import com.barbershop.enums.PaymentMethodModifierType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodUpdateDTO {

    private String newName;
    private String newDescription;
    private Boolean isActive;
    private PaymentMethodModifierType newModifierType;
    private Double priceModifier;
}
