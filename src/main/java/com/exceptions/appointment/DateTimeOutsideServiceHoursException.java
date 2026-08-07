package com.exceptions.appointment;

import com.exceptions.BusinessException;

public class DateTimeOutsideServiceHoursException extends BusinessException {

    public DateTimeOutsideServiceHoursException() {

        super("El horario de inicio y/o cierre de turno ingresado está fuera del horario de atención al cliente.");
    }
}
