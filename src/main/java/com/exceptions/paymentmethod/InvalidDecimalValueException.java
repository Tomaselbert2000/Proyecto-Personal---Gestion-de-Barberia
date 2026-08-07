package com.exceptions.paymentmethod;

import com.exceptions.BusinessException;

public class InvalidDecimalValueException extends BusinessException {

    public InvalidDecimalValueException() {

        super("El valor decimal ingresado para el cálculo de porcentaje no puede ser menor a 0 o mayor a 1.");
    }
}
