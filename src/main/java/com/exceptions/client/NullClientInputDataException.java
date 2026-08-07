package com.exceptions.client;

import com.exceptions.BusinessException;

public class NullClientInputDataException extends BusinessException {

    public NullClientInputDataException() {

        super("Los atributos de cliente no pueden ser NULL.");
    }
}
