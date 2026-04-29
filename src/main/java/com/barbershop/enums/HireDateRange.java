package com.barbershop.enums;

import lombok.Getter;

@SuppressWarnings("ALL")
@Getter
public enum HireDateRange implements DescribableEnum {

    TODOS("Todos los rangos"),
    ULTIMOS_SEIS_MESES("Últimos 6 meses"),
    ESTE_AÑO("Este año"),
    MAS_DE_UN_AÑO("Más de un año (antiguos)");

    private final String displayName;

    HireDateRange(String displayName) {

        this.displayName = displayName;
    }
}
