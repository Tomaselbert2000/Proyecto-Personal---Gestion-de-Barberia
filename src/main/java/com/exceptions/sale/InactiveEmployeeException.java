package com.exceptions.sale;

import com.exceptions.BusinessException;

public class InactiveEmployeeException extends BusinessException {

    public InactiveEmployeeException() {

        super("El empleado ingresado en el registro de venta no es encuentra activo actualmente.");
    }
}
