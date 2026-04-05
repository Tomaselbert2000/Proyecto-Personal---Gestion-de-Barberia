package com.barbershop.exceptions.sale;

public class InvalidProductQuantityException extends RuntimeException{

    @Override
    public String getMessage(){

        return "La cantidad ingresada de productos es inválida.";
    }
}
