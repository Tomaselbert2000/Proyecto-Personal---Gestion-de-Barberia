package com.exceptions.client;

import com.exceptions.BusinessException;

public class DuplicatedPhoneNumberException extends BusinessException {

    public DuplicatedPhoneNumberException() {

        super("El número de teléfono ingresado ya fue registrado anteriormente.");
    }
}
