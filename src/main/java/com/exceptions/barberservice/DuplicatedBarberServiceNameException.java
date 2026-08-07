package com.exceptions.barberservice;

import com.exceptions.BusinessException;

public class DuplicatedBarberServiceNameException extends BusinessException {

    public DuplicatedBarberServiceNameException() {

        super("El nombre de servicio de barbería ya fue registrado anteriormente.");
    }
}
