package com.barbershop.exceptions.appointment;

public class DateTimeOutsideServiceHoursException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El horario de inicio y/o cierre de turno ingresado está fuera del horario de atención al cliente.";
    }
}
