package com.barbershop.exceptions.appointment;

public class OverlappedAppointmentException extends RuntimeException{

    @Override
    public String getMessage(){
        return "La información del turno ingresado ya fue registrada anteriormente.";
    }
}
