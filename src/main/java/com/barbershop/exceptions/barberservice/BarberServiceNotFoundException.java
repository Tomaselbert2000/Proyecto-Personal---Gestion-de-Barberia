package com.barbershop.exceptions.barberservice;

public class BarberServiceNotFoundException extends RuntimeException{

    @Override
    public String getMessage(){
        return "No se encontraron coincidencias de servicios para el ID proporcionado.";
    }
}
