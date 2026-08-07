package com.exceptions.client;

import com.exceptions.BusinessException;

public class InvalidPhoneNumberException extends BusinessException {

    public InvalidPhoneNumberException() {

        super("Uno o más números de teléfono ingresados no son válidos.");
    }
}
