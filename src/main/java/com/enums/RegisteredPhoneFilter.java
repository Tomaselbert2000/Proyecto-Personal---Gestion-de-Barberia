package com.enums;

import lombok.Getter;

@SuppressWarnings("ALL")
@Getter
public enum RegisteredPhoneFilter implements DescribableEnum {

    TODOS("Todos"),
    SIN_TELEFONO_REGISTRADO("Sin teléfono registrado"),
    CON_TELEFONO_REGISTRADO("Con teléfono registrado");

    private final String displayName;

    RegisteredPhoneFilter(String displayName) {

        this.displayName = displayName;
    }
}
