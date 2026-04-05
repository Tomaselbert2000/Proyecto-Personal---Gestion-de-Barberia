package com.barbershop.exceptions.client;

public class NullClientInputDataException extends RuntimeException{

    @Override
    public String getMessage(){
        return "Los atributos de cliente no pueden ser NULL.";
    }
}
