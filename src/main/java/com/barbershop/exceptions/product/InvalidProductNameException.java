package com.barbershop.exceptions.product;

public class InvalidProductNameException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El nombre de producto ingresado contiene caractéres inválidos";
    }
}
