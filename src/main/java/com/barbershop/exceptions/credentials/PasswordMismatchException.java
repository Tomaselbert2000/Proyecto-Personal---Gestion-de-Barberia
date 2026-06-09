package com.barbershop.exceptions.credentials;

public class PasswordMismatchException extends RuntimeException{

    @Override
    public String getMessage() {

        return "Las contraseñas ingresadas no coinciden.";
    }
}
