package com.barbershop.exceptions.sale;

public class InvalidSaleDateTimeException extends RuntimeException{

    @Override
    public String getMessage(){

        return "La fecha de venta ingresada no es válida.";
    }
}
