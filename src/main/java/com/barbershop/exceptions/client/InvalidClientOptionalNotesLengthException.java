package com.barbershop.exceptions.client;

public class InvalidClientOptionalNotesLengthException extends RuntimeException{

    @Override
    public String getMessage(){

        return "La nota opcional de cliente ingresada supera el tamaño máximo de caracteres permitido.";
    }
}
