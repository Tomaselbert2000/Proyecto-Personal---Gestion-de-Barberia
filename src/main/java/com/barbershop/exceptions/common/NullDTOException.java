package com.barbershop.exceptions.common;

public class NullDTOException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El objeto de transferencia de datos enviado es NULL";
    }
}
