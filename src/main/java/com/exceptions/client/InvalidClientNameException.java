package com.exceptions.client;

import com.exceptions.BusinessException;

public class InvalidClientNameException extends BusinessException {

    public InvalidClientNameException() {

        super("Los campos de nombre y apellido de cliente no pueden contener caractéres especiales y/o numéricos.");
    }
}
