package com.barbershop.exceptions.employee;

public class InvalidCommissionPercentageException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El valor de comisión de empleado no puede ser un número negativo y/o cero";
    }
}
