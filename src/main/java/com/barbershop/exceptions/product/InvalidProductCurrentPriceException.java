package com.barbershop.exceptions.product;

public class InvalidProductCurrentPriceException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El precio de venta de producto no puede ser negativo y/o menor al valor de costo del mismo.";
    }
}
