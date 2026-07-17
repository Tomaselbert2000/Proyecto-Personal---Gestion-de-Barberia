package com.exceptions.product;

public class NullProductInputDataException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Los atributos de producto no pueden ser NULL.";
    }
}
