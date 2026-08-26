package com.enums;

import lombok.Getter;

@Getter
public enum SaleCompositionFilter implements DescribableEnum {

    TODOS("Todas las ventas"),
    SOLO_SERVICIO("Solo servicios"),
    SOLO_PRODUCTOS("Solo productos"),
    VENTA_MIXTA("Servicio + productos");

    private final String displayName;

    SaleCompositionFilter(String displayName) {

        this.displayName = displayName;
    }
}
