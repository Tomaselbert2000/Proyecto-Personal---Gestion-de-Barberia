package com.exceptions.client;

import com.exceptions.BusinessException;

public class BlankClientNameException extends BusinessException {

    public BlankClientNameException() {

        super("Los campos de nombre y/o apellido de cliente no pueden quedar en blanco.");
    }
}
