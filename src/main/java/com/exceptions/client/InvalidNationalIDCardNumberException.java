package com.exceptions.client;

import com.exceptions.BusinessException;

public class InvalidNationalIDCardNumberException extends BusinessException {

    public InvalidNationalIDCardNumberException() {

        super("El campo DNI solo puede contener caractéres numéricos.");
    }
}
