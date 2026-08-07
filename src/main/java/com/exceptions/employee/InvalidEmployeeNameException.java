package com.exceptions.employee;

import com.exceptions.BusinessException;

public class InvalidEmployeeNameException extends BusinessException {

    public InvalidEmployeeNameException() {

        super("Los campos de nombre y/o apellido de empleado no pueden contener caractéres númericos o especiales.");
    }
}
