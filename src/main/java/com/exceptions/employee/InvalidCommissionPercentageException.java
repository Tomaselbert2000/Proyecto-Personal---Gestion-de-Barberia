package com.exceptions.employee;

import com.exceptions.BusinessException;

public class InvalidCommissionPercentageException extends BusinessException {

    public InvalidCommissionPercentageException() {

        super("El valor de comisión de empleado no puede ser un número negativo y/o cero");
    }
}
