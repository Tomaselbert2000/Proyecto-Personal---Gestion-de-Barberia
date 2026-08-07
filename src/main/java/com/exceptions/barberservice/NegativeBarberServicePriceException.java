package com.exceptions.barberservice;

import com.exceptions.BusinessException;

public class NegativeBarberServicePriceException extends BusinessException {

    public NegativeBarberServicePriceException() {

        super("El precio de servicio de barbería no puede ser un valor negativo y/o cero.");
    }
}
