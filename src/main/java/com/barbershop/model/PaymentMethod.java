package com.barbershop.model;

import com.barbershop.enums.PaymentMethodModifierType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodID;
    private String name;
    private String description;
    private Boolean isActive;
    private LocalDate createdAt;
    private PaymentMethodModifierType modifierType;
    private Double priceModifier;
}
