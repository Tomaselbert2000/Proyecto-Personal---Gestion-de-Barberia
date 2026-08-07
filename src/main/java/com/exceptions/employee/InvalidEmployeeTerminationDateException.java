package com.exceptions.employee;

import com.exceptions.BusinessException;

public class InvalidEmployeeTerminationDateException extends BusinessException {

    public InvalidEmployeeTerminationDateException() {

        super("La fecha de fin de relación laboral especificada es anterior a la fecha de contratación del empleado");
    }
}
