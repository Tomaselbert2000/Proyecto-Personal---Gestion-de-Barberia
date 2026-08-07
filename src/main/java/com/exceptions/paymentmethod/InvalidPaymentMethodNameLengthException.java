package com.exceptions.paymentmethod;

import com.exceptions.BusinessException;

public class InvalidPaymentMethodNameLengthException extends BusinessException {

    public InvalidPaymentMethodNameLengthException() {

        super("La longitud permitida para nombres de métodos de pago es de 4 a 100 caractéres.");
    }
}
