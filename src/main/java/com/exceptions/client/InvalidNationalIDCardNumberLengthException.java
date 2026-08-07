package com.exceptions.client;

import com.exceptions.BusinessException;

public class InvalidNationalIDCardNumberLengthException extends BusinessException {

    public InvalidNationalIDCardNumberLengthException() {

        super("La longitud permitida para el campo DNI es entre 7 y 8 dígitos");
    }
}
