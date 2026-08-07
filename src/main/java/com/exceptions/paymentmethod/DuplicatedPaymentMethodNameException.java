package com.exceptions.paymentmethod;

import com.exceptions.BusinessException;

public class DuplicatedPaymentMethodNameException extends BusinessException {

    public DuplicatedPaymentMethodNameException() {

        super("El nombre de método de pago ingresado ya fue registrado anteriormente.");
    }
}
