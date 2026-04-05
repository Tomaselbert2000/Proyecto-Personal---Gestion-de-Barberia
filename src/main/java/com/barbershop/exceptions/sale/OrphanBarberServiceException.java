package com.barbershop.exceptions.sale;

public class OrphanBarberServiceException extends RuntimeException{

    @Override
    public String getMessage(){

        return "No es posible registrar el servicio de barbería, el ID de empleado ingresado es NULL.";
    }
}
