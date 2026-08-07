package com.exceptions.product;

import com.exceptions.BusinessException;

public class NullProductInputDataException extends BusinessException {

    public NullProductInputDataException() {
        super("Los atributos de producto no pueden ser NULL.");
    }
}
