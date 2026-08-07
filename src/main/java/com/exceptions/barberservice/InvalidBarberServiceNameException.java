package com.exceptions.barberservice;

import com.exceptions.BusinessException;

public class InvalidBarberServiceNameException extends BusinessException {

    public InvalidBarberServiceNameException() {

        super("El nombre de servicio de barbería no puede contener caractéres especiales y/o numéricos");
    }
}
