package com.barbershop.exceptions.client;

public class InvalidNationalIDCardNumberException extends RuntimeException {

    @Override
    public String getMessage() {

        return "El campo DNI solo puede contener caractéres numéricos.";
    }
}
