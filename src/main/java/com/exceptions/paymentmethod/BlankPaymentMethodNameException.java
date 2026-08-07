package com.exceptions.paymentmethod;

import com.exceptions.BusinessException;

public class BlankPaymentMethodNameException extends BusinessException {

    public BlankPaymentMethodNameException() {
        super("El nombre de método de pago no puede quedar en blanco.");
    }
}
