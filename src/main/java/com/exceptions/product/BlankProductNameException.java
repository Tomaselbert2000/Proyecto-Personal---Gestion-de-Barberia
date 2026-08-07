package com.exceptions.product;

import com.exceptions.BusinessException;

public class BlankProductNameException extends BusinessException {

    public BlankProductNameException() {

        super("El nombre de producto no puede quedar en blanco.");
    }
}
