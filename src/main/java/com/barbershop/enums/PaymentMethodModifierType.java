package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodModifierType implements DescribableEnum {

    TODOS("Todos los medios de pago"),
    NINGUNO("No aplicable"),
    DESCUENTO("Descuento"),
    RECARGO("Recargo");

    private final String displayName;

    PaymentMethodModifierType(String displayName) {

        this.displayName = displayName;
    }
}
