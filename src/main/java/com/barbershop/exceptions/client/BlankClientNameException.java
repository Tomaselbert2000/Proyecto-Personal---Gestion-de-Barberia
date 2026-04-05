package com.barbershop.exceptions.client;

public class BlankClientNameException extends RuntimeException{

    @Override
    public String getMessage(){
        return "Los campos de nombre y/o apellido de cliente no pueden quedar en blanco.";
    }
}
