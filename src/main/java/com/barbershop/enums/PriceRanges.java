package com.barbershop.enums;

import lombok.Getter;

@Getter
public enum PriceRanges implements DescribableEnum {

    TODOS("Todos", null, null),
    DESDE_1_HASTA_10000("ARS $1 ~ $10000", 1.0, 10000.0),
    DESDE_10000_HASTA_20000("ARS $10000 ~ $20000", 10000.0, 20000.0),
    MAS_DE_20000("ARS $20000 o superior", 20000.0, null);

    private final String displayName;
    private final Double minPrice;
    private final Double maxPrice;

    PriceRanges(String displayName, Double minPrice, Double maxPrice) {

        this.displayName = displayName;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
