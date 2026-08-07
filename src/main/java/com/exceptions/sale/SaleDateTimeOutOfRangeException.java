package com.exceptions.sale;

import com.exceptions.BusinessException;

public class SaleDateTimeOutOfRangeException extends BusinessException {

    public SaleDateTimeOutOfRangeException() {

        super("La fecha de venta ingresada supera el márgen de 24 horas para el registro de ventas.");
    }
}
