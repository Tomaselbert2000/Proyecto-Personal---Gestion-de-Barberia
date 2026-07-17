package com.exceptions.appuser;

public class AppUserNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "No se encontraron usuarios con el ID proporcionado.";
    }
}