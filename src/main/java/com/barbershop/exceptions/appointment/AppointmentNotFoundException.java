package com.barbershop.exceptions.appointment;

public class AppointmentNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "No se encontraron coincidencias de turnos con el ID proporcionado.";
    }
}
