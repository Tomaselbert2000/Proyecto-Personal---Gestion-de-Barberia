package com.exceptions.client;

import com.exceptions.BusinessException;

public class DuplicatedNationalIDCardNumberException extends BusinessException {

    public DuplicatedNationalIDCardNumberException() {

        super("El número de DNI ingresado ya fue registrado anteriormente.");
    }
}
