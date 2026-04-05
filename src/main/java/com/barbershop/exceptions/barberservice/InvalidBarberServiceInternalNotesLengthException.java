package com.barbershop.exceptions.barberservice;

public class InvalidBarberServiceInternalNotesLengthException extends RuntimeException{

    @Override
    public String getMessage(){

        return "La longitud máxima permitida para el campo de notas internas de servicio es de 256 caractéres.";
    }
}
