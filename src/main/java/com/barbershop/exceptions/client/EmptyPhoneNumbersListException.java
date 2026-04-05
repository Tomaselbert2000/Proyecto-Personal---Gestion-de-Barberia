package com.barbershop.exceptions.client;

public class EmptyPhoneNumbersListException extends RuntimeException {

    @Override
    public String getMessage() {

        return "La lista de teléfonos de contacto de cliente está vacía.";
    }
}
