package com.exceptions.paymentmethod;

import com.exceptions.BusinessException;

public class InvalidPaymentMethodNameException extends BusinessException {

    public InvalidPaymentMethodNameException() {

        super("El campo de nombre de método de pago no puede contener caractéres numéricos y/o especiales.");
    }
}
