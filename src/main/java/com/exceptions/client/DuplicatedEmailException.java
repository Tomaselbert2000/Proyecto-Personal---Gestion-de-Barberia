package com.exceptions.client;

import com.exceptions.BusinessException;

public class DuplicatedEmailException extends BusinessException {

    public DuplicatedEmailException() {
        super("El email ingresado ya fue registrado anteriormente.");
    }
}
