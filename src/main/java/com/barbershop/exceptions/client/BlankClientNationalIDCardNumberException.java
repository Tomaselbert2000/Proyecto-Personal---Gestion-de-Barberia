package com.barbershop.exceptions.client;

public class BlankClientNationalIDCardNumberException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El campo de número de DNI de cliente no puede quedar en blanco.";
    }
}
