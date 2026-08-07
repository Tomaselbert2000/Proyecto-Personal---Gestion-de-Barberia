package com.exceptions.appointment;

import com.exceptions.BusinessException;

public class InvalidAppointmentUpdateException extends BusinessException {

    public InvalidAppointmentUpdateException() {

        super("No es posible actualizar turnos cancelados y/o finalizados, o bien establecer un turno de estado 'Reprogramado' como 'Programado'.");
    }
}
