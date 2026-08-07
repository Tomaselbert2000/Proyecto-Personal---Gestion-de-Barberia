package com.exceptions.client;

import com.exceptions.BusinessException;

public class ClientNotFoundException extends BusinessException {

    public ClientNotFoundException() {
        super("No se encontraron coincidencias de cliente para el ID proporcionado.");
    }
}
