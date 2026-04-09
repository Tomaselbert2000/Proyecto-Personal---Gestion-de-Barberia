package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum AppointmentStatus implements DescribableEnum {

    TODOS("Todos los estados de turno"),
    PROGRAMADO("Programado"),
    REPROGRAMADO("Reprogramado"),
    CANCELADO("Cancelado"),
    FINALIZADO("Finalizado");

    private final String displayName;

    AppointmentStatus(String displayName) {

        this.displayName = displayName;
    }
}
