package com.exceptions.credentials;

import com.exceptions.BusinessException;

public class PasswordMismatchException extends BusinessException {

    public PasswordMismatchException() {

        super("Las contraseñas ingresadas no coinciden.");
    }
}
