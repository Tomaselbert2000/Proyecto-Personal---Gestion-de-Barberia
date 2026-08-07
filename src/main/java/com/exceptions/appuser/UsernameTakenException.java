package com.exceptions.appuser;

import com.exceptions.BusinessException;

public class UsernameTakenException extends BusinessException {

    public UsernameTakenException() {

        super("El nombre de usuario especificado ya se encuentra registrado.");
    }
}
