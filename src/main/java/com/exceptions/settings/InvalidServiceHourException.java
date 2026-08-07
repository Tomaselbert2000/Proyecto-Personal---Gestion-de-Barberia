package com.exceptions.settings;

import com.exceptions.BusinessException;

public class InvalidServiceHourException extends BusinessException {

    public InvalidServiceHourException() {

        super("El horario de cierre especificado es anterior al horario de apertura.");
    }
}
