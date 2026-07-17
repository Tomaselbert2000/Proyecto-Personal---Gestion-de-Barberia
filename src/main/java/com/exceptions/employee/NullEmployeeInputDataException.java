package com.exceptions.employee;

public class NullEmployeeInputDataException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Los campos de atributo de empleado no pueden ser NULL.";
    }
}
