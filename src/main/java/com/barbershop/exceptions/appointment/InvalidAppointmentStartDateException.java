package com.barbershop.exceptions.appointment;

public class InvalidAppointmentStartDateException extends RuntimeException{

    @Override
    public String getMessage(){
        return "La fecha u hora de inicio ingresada para el turno es anterior a la fecha actual del sistema.";
    }
}
