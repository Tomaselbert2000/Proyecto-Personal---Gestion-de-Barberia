package com.exceptions.product;

public class ProductNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "No se encontraron productos con el ID proporcionado.";
    }
}
