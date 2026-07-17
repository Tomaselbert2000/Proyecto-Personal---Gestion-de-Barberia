package com.exceptions.client;

public class InvalidEmailException extends RuntimeException {

    @Override
    public String getMessage() {
        return "El email ingresado no es válido.";
    }
}
