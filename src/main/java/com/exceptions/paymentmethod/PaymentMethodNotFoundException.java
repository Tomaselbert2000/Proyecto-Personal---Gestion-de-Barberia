package com.exceptions.paymentmethod;

import com.exceptions.BusinessException;

public class PaymentMethodNotFoundException extends BusinessException {

    public PaymentMethodNotFoundException() {

        super("No se encontraron coincidencias de métodos de pago para el ID proporcionado.");
    }
}
