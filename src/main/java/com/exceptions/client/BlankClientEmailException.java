package com.exceptions.client;

import com.exceptions.BusinessException;

public class BlankClientEmailException extends BusinessException {

    public BlankClientEmailException() {

        super("El campo de email de cliente no puede quedar en blanco.");
    }
}
