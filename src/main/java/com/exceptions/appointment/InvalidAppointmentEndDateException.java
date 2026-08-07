package com.exceptions.appointment;

import com.exceptions.BusinessException;

public class InvalidAppointmentEndDateException extends BusinessException {

    public InvalidAppointmentEndDateException() {

        super("La fecha u hora de fin ingresada para el turno es anterior a la fecha de inicio del mismo y/o anterior a la fecha actual del sistema.");
    }
}
