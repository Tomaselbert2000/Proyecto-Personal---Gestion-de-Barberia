package com.barbershop.exceptions.barberservice;

public class DuplicatedBarberServiceNameException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El nombre de servicio de barbería ya fue registrado anteriormente.";
    }
}
