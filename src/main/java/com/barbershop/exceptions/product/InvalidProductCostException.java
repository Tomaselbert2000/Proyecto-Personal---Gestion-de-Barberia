package com.barbershop.exceptions.product;

public class InvalidProductCostException extends RuntimeException {

    @Override
    public String getMessage() {
        return "El costo de un producto no puede ser menor o igual a cero.";
    }
}
