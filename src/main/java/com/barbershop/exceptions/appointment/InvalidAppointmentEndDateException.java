package com.barbershop.exceptions.appointment;

public class InvalidAppointmentEndDateException extends RuntimeException{

    @Override
    public String getMessage(){
        return "La fecha u hora de fin ingresada para el turno es anterior a la fecha de inicio del mismo y/o anterior a la fecha actual del sistema.";
    }
}
