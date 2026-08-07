package com.exceptions.sale;

import com.exceptions.BusinessException;

public class OrphanBarberServiceException extends BusinessException {

    public OrphanBarberServiceException() {

        super("No es posible registrar el servicio de barbería, el ID de empleado ingresado es NULL.");
    }
}
