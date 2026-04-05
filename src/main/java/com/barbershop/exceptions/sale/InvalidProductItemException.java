package com.barbershop.exceptions.sale;

public class InvalidProductItemException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Los campos de atributo de item de producto no pueden ser NULL.";
    }
}
