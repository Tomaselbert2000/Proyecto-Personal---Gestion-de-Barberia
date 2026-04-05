package com.barbershop.exceptions.barberservice;

public class NegativeBarberServicePriceException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El precio de servicio de barbería no puede ser un valor negativo y/o cero.";
    }
}
