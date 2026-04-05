package com.barbershop.exceptions.product;

public class NegativeStockLevelException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El stock actual de un producto no puede ser menor a cero.";
    }
}
