package com.barbershop.exceptions.paymentmethod;

public class BlankPaymentMethodNameException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El nombre de método de pago no puede quedar en blanco.";
    }
}
