package com.exceptions.product;

public class DuplicatedProductNameException extends RuntimeException {

    @Override
    public String getMessage() {
        return "El nombre ingresado para el producto ya fue registrado anteriormente.";
    }
}
