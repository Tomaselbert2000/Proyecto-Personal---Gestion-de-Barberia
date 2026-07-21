package com.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodStatus implements DescribableEnum {

    TODOS("Todos los estados"),
    ACTIVO("Medio habilitado"),
    INACTIVO("Medio deshabilitado");

    private final String displayName;

    PaymentMethodStatus(String displayName) {

        this.displayName = displayName;
    }
}
