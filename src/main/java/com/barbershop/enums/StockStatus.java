package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum StockStatus implements DescribableEnum {

    TODOS("Todos los estados de stock"),
    CRITICO("Nivel crítico"),
    BAJO("Bajo"),
    ATENCION("Atención"),
    SUFICIENTE("Suficiente"),
    EXCEDIDO("Sobrestockeado");

    private final String displayName;

    StockStatus(String displayName) {

        this.displayName = displayName;
    }
}
