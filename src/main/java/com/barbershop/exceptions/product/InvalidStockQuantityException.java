package com.barbershop.exceptions.product;

public class InvalidStockQuantityException extends RuntimeException {

    @Override
    public String getMessage() {
        return "No es posible insertar valores negativos al ingresar/retirar stock.";
    }
}
