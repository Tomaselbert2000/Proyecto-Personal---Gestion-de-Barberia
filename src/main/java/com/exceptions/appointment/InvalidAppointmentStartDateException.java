package com.exceptions.appointment;

import com.exceptions.BusinessException;

public class InvalidAppointmentStartDateException extends BusinessException {

    public InvalidAppointmentStartDateException() {

        super("La fecha u hora de inicio ingresada para el turno es anterior a la fecha actual del sistema.");
    }
}
