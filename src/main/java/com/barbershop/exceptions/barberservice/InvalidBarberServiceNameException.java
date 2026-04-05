package com.barbershop.exceptions.barberservice;

public class InvalidBarberServiceNameException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El nombre de servicio de barbería no puede contener caractéres especiales y/o numéricos";
    }
}
