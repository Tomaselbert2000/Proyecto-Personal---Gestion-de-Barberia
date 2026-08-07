package com.exceptions.client;

import com.exceptions.BusinessException;

public class DuplicatedPhoneInListException extends BusinessException {

    public DuplicatedPhoneInListException() {
        super("La lista de números de teléfono de cliente contiene duplicados");
    }
}
