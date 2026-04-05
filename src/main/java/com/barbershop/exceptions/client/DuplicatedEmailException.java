package com.barbershop.exceptions.client;

public class DuplicatedEmailException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El email ingresado ya fue registrado anteriormente.";
    }
}
