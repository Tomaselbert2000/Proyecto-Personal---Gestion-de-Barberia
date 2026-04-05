package com.barbershop.exceptions.barberservice;

public class BlankBarberServiceNameException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El nombre de servicio de barbería no puede quedar en blanco.";
    }
}
