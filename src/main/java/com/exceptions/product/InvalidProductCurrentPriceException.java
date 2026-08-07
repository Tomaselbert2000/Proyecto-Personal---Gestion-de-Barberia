package com.exceptions.product;

import com.exceptions.BusinessException;

public class InvalidProductCurrentPriceException extends BusinessException {

    public InvalidProductCurrentPriceException() {

        super("El precio de venta de producto no puede ser negativo y/o menor al valor de costo del mismo.");
    }
}
