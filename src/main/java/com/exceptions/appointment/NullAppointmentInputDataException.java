package com.exceptions.appointment;

import com.exceptions.BusinessException;

public class NullAppointmentInputDataException extends BusinessException {

    public NullAppointmentInputDataException() {

        super("Los atributos de turno no pueden ser NULL.");
    }
}
