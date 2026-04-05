package com.barbershop.exceptions.barberservice;

public class NullBarberServiceInputDataException extends RuntimeException{

    @Override
    public String getMessage(){
        return "Los atributos de servicio de barbería no pueden ser NULL";
    }
}
