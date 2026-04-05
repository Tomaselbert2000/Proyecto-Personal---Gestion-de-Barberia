package com.barbershop.exceptions.common;

public class NullMapperInputException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Uno o más objetos requeridos para el mapeo de nueva entidad son NULL.";
    }
}
