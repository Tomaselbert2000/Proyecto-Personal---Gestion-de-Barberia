package com.exceptions.barberservice;

import com.exceptions.BusinessException;

public class BarberServiceNotFoundException extends BusinessException {

    public BarberServiceNotFoundException() {

        super("No se encontraron coincidencias de servicios para el ID proporcionado.");
    }
}
