package com.exceptions.product;

import com.exceptions.BusinessException;

public class DuplicatedProductNameException extends BusinessException {

    public DuplicatedProductNameException() {

        super("El nombre ingresado para el producto ya fue registrado anteriormente.");
    }
}
