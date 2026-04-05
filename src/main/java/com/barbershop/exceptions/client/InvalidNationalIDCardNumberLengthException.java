package com.barbershop.exceptions.client;

public class InvalidNationalIDCardNumberLengthException extends RuntimeException{

    @Override
    public String getMessage(){
        return "La longitud permitida para el campo DNI es entre 7 y 8 dígitos";
    }
}
