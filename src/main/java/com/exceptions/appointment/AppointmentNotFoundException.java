package com.exceptions.appointment;

import com.exceptions.BusinessException;

public class AppointmentNotFoundException extends BusinessException {

    public AppointmentNotFoundException() {

        super("No se encontraron coincidencias de turnos con el ID proporcionado.");
    }
}
