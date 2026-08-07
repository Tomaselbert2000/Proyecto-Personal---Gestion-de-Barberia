package com.exceptions.client;

import com.exceptions.BusinessException;

public class EmptyPhoneNumbersListException extends BusinessException {

    public EmptyPhoneNumbersListException() {

        super("La lista de teléfonos de contacto de cliente está vacía.");
    }
}
