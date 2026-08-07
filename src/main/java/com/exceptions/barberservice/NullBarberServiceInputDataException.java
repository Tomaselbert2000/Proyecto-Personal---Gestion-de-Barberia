package com.exceptions.barberservice;

import com.exceptions.BusinessException;

public class NullBarberServiceInputDataException extends BusinessException {

    public NullBarberServiceInputDataException() {

        super("Los atributos de servicio de barbería no pueden ser NULL");
    }
}
