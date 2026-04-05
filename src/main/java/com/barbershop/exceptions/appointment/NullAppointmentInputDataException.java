package com.barbershop.exceptions.appointment;

public class NullAppointmentInputDataException extends RuntimeException{

    @Override
    public String getMessage(){
        return "Los atributos de turno no pueden ser NULL.";
    }
}
