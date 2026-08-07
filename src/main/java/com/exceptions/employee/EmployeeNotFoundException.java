package com.exceptions.employee;

import com.exceptions.BusinessException;

public class EmployeeNotFoundException extends BusinessException {

    public EmployeeNotFoundException() {

        super("No se encontraron coincidencias de empleados para el ID proporcionado.");
    }
}
