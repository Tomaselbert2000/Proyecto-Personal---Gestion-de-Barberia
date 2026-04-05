package com.barbershop.exceptions.client;

public class InvalidPhoneNumberException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Uno o más números de teléfono ingresados no son válidos.";
    }
}
