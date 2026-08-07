package com.exceptions.client;

import com.exceptions.BusinessException;

public class InvalidEmailException extends BusinessException {

    public InvalidEmailException() {

        super("El email ingresado no es válido.");
    }
}
