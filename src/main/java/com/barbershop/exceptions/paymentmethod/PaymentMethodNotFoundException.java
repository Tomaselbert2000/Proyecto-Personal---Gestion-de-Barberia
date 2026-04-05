package com.barbershop.exceptions.paymentmethod;

public class PaymentMethodNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {

        return "No se encontraron coincidencias de métodos de pago para el ID proporcionado.";
    }
}
