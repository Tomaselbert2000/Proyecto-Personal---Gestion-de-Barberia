package com.barbershop.exceptions.client;

public class InvalidPhoneNumberLengthException extends RuntimeException{

    @Override
    public String getMessage(){
        return "La longitud permitida para números de teléfono es 10 a 15 dígitos";
    }
}
