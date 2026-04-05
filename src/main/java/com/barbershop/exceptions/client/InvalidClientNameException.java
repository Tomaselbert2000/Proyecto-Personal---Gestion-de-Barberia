package com.barbershop.exceptions.client;

public class InvalidClientNameException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Los campos de nombre y apellido de cliente no pueden contener caractéres especiales y/o numéricos.";
    }
}
