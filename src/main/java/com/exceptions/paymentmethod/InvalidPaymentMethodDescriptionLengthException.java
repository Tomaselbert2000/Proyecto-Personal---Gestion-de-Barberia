package com.exceptions.paymentmethod;

import com.exceptions.BusinessException;

public class InvalidPaymentMethodDescriptionLengthException extends BusinessException {

    public InvalidPaymentMethodDescriptionLengthException() {

        super("La longitud máxima admitida para descripciones de métodos de pago es de 256 caractéres.");
    }
}
