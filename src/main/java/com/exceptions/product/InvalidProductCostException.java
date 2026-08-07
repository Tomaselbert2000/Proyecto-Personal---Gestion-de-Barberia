package com.exceptions.product;

import com.exceptions.BusinessException;

public class InvalidProductCostException extends BusinessException {

    public InvalidProductCostException() {

        super("El costo de un producto no puede ser menor o igual a cero.");
    }
}
