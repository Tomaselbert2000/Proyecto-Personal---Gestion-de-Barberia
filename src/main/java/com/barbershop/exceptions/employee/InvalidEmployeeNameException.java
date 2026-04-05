package com.barbershop.exceptions.employee;

public class InvalidEmployeeNameException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Los campos de nombre y/o apellido de empleado no pueden contener caractéres númericos o especiales.";
    }
}
