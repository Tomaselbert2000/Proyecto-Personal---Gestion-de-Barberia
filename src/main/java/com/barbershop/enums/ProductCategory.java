package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum ProductCategory implements DescribableEnum{

    TODOS("Todas las categorias"),
    SHAMPOO("Shampoos"),
    CERA("Ceras"),
    POMADA("Pomadas para pelo"),
    GEL("Gel"),
    TINTURA("Tinturas"),
    HOJA_DE_AFEITAR("Hojas de afeitar"),
    NAVAJA("Navajas"),
    BROCHA("Brochas"),
    POLVO_DE_TINTURA("Polvos de tintura");

    private final String displayName;

    ProductCategory(String displayName) {

        this.displayName = displayName;
    }
}
