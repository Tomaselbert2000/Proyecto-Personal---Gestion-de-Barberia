package com.exceptions.paymentmethod;

import com.exceptions.BusinessException;

public class NullPaymentMethodInputDataException extends BusinessException {

    public NullPaymentMethodInputDataException() {

        super("Los campos de atributo de método de pago no pueden ser NULL.");
    }
}
