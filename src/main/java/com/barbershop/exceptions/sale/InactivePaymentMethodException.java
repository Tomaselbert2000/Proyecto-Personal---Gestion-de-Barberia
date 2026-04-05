package com.barbershop.exceptions.sale;

public class InactivePaymentMethodException extends RuntimeException{

    @Override
    public String getMessage(){

        return "El medio de pago ingresado en el registro de venta se encuentra configurado como inactivo en el sistema.";
    }
}
