package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum EmployeeStatus implements DescribableEnum {

    TODOS("Todos los estados"),
    ACTIVO("En actividad"),
    INACTIVO("No activo");

    private final String displayName;

    EmployeeStatus(String displayName) {

        this.displayName = displayName;
    }
}
