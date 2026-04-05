package com.barbershop.exceptions.paymentmethod;

public class InvalidPaymentMethodNameException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El campo de nombre de método de pago no puede contener caractéres numéricos y/o especiales.";
    }
}
