package com.barbershop.exceptions.client;

public class DuplicatedNationalIDCardNumberException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El número de DNI ingresado ya fue registrado anteriormente.";
    }
}
