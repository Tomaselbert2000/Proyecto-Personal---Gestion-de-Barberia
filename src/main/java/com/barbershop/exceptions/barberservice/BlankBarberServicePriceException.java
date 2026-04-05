package com.barbershop.exceptions.barberservice;

public class BlankBarberServicePriceException extends RuntimeException {

    @Override
    public String getMessage() {

        return "El precio de servicio no puede quedar en blanco.";
    }
}
