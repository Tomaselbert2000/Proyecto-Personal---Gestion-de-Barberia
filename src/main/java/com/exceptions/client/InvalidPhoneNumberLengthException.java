package com.exceptions.client;

import com.exceptions.BusinessException;

public class InvalidPhoneNumberLengthException extends BusinessException {

    public InvalidPhoneNumberLengthException() {

        super("La longitud permitida para números de teléfono es 10 a 15 dígitos");
    }
}
