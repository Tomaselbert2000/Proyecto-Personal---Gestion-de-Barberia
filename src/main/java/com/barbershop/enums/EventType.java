package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum EventType implements DescribableEnum{

    NUEVO_CLIENTE("Nuevo cliente"),
    NUEVO_EMPLEADO("Nuevo empleado"),
    NUEVO_PRODUCTO("Nuevo producto"),
    NUEVO_TURNO("Nuevo turno"),
    TURNO_FINALIZADO("Turno finalizado"),
    TURNO_CANCELADO("Turno cancelado"),
    ALERTA_STOCK_BAJO("Alerta de Stock"),
    EMPLEADO_INACTIVO("Empleado temporalmente inactivo"),
    EMPLEADO_DESVINCULADO("Empleado desvinculado");

    private final String displayName;

    EventType(String displayName) {

        this.displayName = displayName;
    }
}
