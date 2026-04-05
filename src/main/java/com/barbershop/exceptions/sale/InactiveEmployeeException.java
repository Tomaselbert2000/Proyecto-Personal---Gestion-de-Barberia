package com.barbershop.exceptions.sale;

public class InactiveEmployeeException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El empleado ingresado en el registro de venta no es encuentra activo actualmente.";
    }
}
