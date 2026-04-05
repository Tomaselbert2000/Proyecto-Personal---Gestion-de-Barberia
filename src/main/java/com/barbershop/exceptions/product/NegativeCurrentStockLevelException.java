package com.barbershop.exceptions.product;

public class NegativeCurrentStockLevelException extends RuntimeException{

    @Override
    public String getMessage(){
        return "No es posible ingresar un producto nuevo con valor de stock actual menor a cero.";
    }
}
