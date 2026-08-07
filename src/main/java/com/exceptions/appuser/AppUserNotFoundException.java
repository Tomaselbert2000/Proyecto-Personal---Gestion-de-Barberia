package com.exceptions.appuser;

import com.exceptions.BusinessException;

public class AppUserNotFoundException extends BusinessException {

    public AppUserNotFoundException() {
        super("No se encontraron usuarios con el ID proporcionado.");
    }
}