package com.barbershop.exceptions.client;

public class InvalidClientNameLengthException extends RuntimeException {

    @Override
    public String getMessage() {

        return "La longitud permitida para nombres de clientes es 3 a 100 caractéres.";
    }
}
