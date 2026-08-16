package com.enums;

import lombok.Getter;

@SuppressWarnings("ALL")
@Getter
public enum ClientNotesFilter implements DescribableEnum {

    TODOS("Todos los registros"),
    SIN_OBSERVACIONES("Sin observaciones"),
    CON_OBSERVACIONES("Con observaciones");

    private final String displayName;

    ClientNotesFilter(String displayName) {

        this.displayName = displayName;
    }
}
