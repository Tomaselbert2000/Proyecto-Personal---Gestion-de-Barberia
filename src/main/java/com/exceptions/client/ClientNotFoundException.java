package com.exceptions.client;

public class ClientNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "No se encontraron coincidencias de cliente para el ID proporcionado.";
    }
}
