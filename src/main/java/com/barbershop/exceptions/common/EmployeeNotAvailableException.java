package com.barbershop.exceptions.common;

public class EmployeeNotAvailableException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El empleado seleccionado para el nuevo turno ya posee otro asignado dentro del mismo rango horario.";
    }
}
