package com.exceptions.settings;

public class InvalidServiceHourException extends RuntimeException {

    @Override
    public String getMessage() {

        return "El horario de cierre especificado es anterior al horario de apertura.";
    }
}
