package com.barbershop.exceptions.product;

public class NegativeSafetyStockLevelException extends RuntimeException {

    @Override
    public String getMessage() {
        return "No es posible ingresar un producto nuevo con un valor de stock de seguridad menor a cero.";
    }
}
