package com.barbershop.exceptions.appuser;

public class UsernameTakenException extends RuntimeException {

    @Override
    public String getMessage() {
        return "El nombre de usuario especificado ya se encuentra registrado.";
    }
}
