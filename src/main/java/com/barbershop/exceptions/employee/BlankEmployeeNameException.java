package com.barbershop.exceptions.employee;

public class BlankEmployeeNameException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Los campos de nombre y/o apellido de empleado no pueden quedar en blanco.";
    }
}
