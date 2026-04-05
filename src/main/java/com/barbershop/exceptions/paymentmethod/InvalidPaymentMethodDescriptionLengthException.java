package com.barbershop.exceptions.paymentmethod;

public class InvalidPaymentMethodDescriptionLengthException extends RuntimeException{

    @Override
    public String getMessage(){

        return "La longitud máxima admitida para descripciones de métodos de pago es de 256 caractéres.";
    }
}
