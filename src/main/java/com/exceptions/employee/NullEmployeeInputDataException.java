package com.exceptions.employee;

import com.exceptions.BusinessException;

public class NullEmployeeInputDataException extends BusinessException {

    public NullEmployeeInputDataException() {

        super("Los campos de atributo de empleado no pueden ser NULL.");
    }
}
