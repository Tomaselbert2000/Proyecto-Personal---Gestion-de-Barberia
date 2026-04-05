package com.barbershop.exceptions.product;

public class BlankProductNameException extends RuntimeException{

    @Override
    public String getMessage() {
        return "El nombre de producto no puede quedar en blanco.";
    }
}
