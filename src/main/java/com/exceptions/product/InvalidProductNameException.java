package com.exceptions.product;

import com.exceptions.BusinessException;

public class InvalidProductNameException extends BusinessException {

    public InvalidProductNameException() {

        super("El nombre de producto ingresado contiene caractéres inválidos");
    }
}
