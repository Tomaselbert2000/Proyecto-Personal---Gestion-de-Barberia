package com.barbershop.exceptions.paymentmethod;

public class InvalidPaymentMethodNameLengthException extends RuntimeException{

    @Override
    public String getMessage(){

        return "La longitud permitida para nombres de métodos de pago es de 4 a 100 caractéres.";
    }
}
