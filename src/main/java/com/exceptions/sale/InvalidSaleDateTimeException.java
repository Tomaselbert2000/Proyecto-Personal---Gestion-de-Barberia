package com.exceptions.sale;

import com.exceptions.BusinessException;

public class InvalidSaleDateTimeException extends BusinessException {

    public InvalidSaleDateTimeException() {

        super("La fecha de venta ingresada no es válida.");
    }
}
