package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum ProductPresentationUnit implements DescribableEnum {

    TODOS("Todas las categorias"),
    KILOGRAMOS("Kg"),
    GRAMOS("gr"),
    LITROS("Lt"),
    MILILITROS("mL"),
    ONZAS("Oz");

    private final String displayName;

    ProductPresentationUnit(String displayName) {

        this.displayName = displayName;
    }
}
