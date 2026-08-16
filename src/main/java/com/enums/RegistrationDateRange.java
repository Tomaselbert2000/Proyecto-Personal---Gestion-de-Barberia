package com.enums;

import lombok.Getter;

@SuppressWarnings("ALL")
@Getter
public enum RegistrationDateRange implements DescribableEnum {

    TODOS("Todos los rangos"),
    ULTIMOS_30_DIAS("Ultimos 30 días"),
    ESTE_MES("Este mes"),
    ESTE_AÑO("Este año");

    private final String displayName;

    RegistrationDateRange(String displayName) {

        this.displayName = displayName;
    }
}
