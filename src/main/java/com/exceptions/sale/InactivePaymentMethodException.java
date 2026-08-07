package com.exceptions.sale;

import com.exceptions.BusinessException;

public class InactivePaymentMethodException extends BusinessException {

    public InactivePaymentMethodException() {

        super("El medio de pago ingresado en el registro de venta se encuentra configurado como inactivo en el sistema.");
    }
}
