package com.barbershop.exceptions.employee;

public class InvalidEmployeeTerminationDateException extends RuntimeException{

    @Override
    public String getMessage(){

        return "La fecha de fin de relación laboral especificada es anterior a la fecha de contratación del empleado";
    }
}
