package com.barbershop.exceptions.client;

public class DuplicatedPhoneInListException extends RuntimeException{

    @Override
    public String getMessage(){
        return "La lista de números de teléfono de cliente contiene duplicados";
    }
}
