package com.barbershop.exceptions.product;

public class InvalidOperationArgumentException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El identificador de operación ingresado no es válido.";
    }
}
