package com.exceptions.client;

import com.exceptions.BusinessException;

public class BlankClientNationalIDCardNumberException extends BusinessException {

    public BlankClientNationalIDCardNumberException() {

        super("El campo de número de DNI de cliente no puede quedar en blanco.");
    }
}
