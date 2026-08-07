package com.exceptions.common;

import com.exceptions.BusinessException;

public class EmployeeNotAvailableException extends BusinessException {

    public EmployeeNotAvailableException() {

        super("El empleado seleccionado para el nuevo turno ya posee otro asignado dentro del mismo rango horario.");
    }
}
