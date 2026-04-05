package com.barbershop.exceptions.paymentmethod;

public class DuplicatedPaymentMethodNameException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El nombre de método de pago ingresado ya fue registrado anteriormente.";
    }
}
