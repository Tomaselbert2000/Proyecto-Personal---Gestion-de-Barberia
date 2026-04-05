package com.barbershop.exceptions.paymentmethod;

public class InvalidDecimalValueException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El valor decimal ingresado para el cálculo de porcentaje no puede ser menor a 0 o mayor a 1.";
    }
}
