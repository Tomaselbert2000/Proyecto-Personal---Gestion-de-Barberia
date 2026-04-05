package com.barbershop.exceptions.product;

public class CurrentStockLimitException extends RuntimeException{

    @Override
    public String getMessage(){
        return "La cantidad ingresada supera el límite de unidades en stock de producto actualmente.";
    }
}
