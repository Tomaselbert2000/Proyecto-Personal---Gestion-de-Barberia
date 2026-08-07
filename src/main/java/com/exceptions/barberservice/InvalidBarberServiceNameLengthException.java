package com.exceptions.barberservice;

import com.exceptions.BusinessException;

public class InvalidBarberServiceNameLengthException extends BusinessException {

    public InvalidBarberServiceNameLengthException() {

        super("La longitud en caractéres permitida para nombres de servicio es 4-100");
    }
}
