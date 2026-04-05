package com.barbershop.exceptions.client;

public class BlankClientEmailException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El campo de email de cliente no puede quedar en blanco.";
    }
}
