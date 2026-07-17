package com.exceptions.client;

public class DuplicatedPhoneNumberException extends RuntimeException {

    @Override
    public String getMessage() {

        return "El número de teléfono ingresado ya fue registrado anteriormente.";
    }
}
