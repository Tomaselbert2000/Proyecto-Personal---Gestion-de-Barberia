package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum BarberServiceCategory implements DescribableEnum{
    TODOS("Todas las categorias", ""),
    CORTE("Corte", "Servicio básico de corte de pelo"),
    CORTE_Y_BARBA("Corte y barba", "Servicio de corte de pelo con servicio de barba incluido"),
    COLOR("Color", "Coloración y tintura de pelo");

    private final String displayName;
    private final String description;

    BarberServiceCategory(String displayName, String description) {

        this.displayName = displayName;
        this.description = description;
    }
}
