package com.barbershop.exceptions.product;

public class InvalidSafetyStockLevelException extends RuntimeException{

    @Override
    public String getMessage(){
        return "El valor de stock de seguridad no puede ser mayor al stock actual.";
    }
}
