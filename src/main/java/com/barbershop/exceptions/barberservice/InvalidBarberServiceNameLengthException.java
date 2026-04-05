package com.barbershop.exceptions.barberservice;

public class InvalidBarberServiceNameLengthException extends RuntimeException {

    @Override
    public String getMessage() {
        return "La longitud en caractéres permitida para nombres de servicio es 4-100";
    }
}
