package com.exceptions.barberservice;

import com.exceptions.BusinessException;

public class BlankBarberServicePriceException extends BusinessException {

    public BlankBarberServicePriceException() {

        super("El precio de servicio no puede quedar en blanco.");
    }
}
