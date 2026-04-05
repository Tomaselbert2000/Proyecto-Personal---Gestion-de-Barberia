package com.barbershop.exceptions.paymentmethod;

public class NullPaymentMethodInputDataException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Los campos de atributo de método de pago no pueden ser NULL.";
    }
}
