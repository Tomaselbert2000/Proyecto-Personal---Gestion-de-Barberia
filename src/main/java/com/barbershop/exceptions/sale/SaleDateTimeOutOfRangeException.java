package com.barbershop.exceptions.sale;

public class SaleDateTimeOutOfRangeException extends RuntimeException{

    @Override
    public String getMessage(){

        return "La fecha de venta ingresada supera el márgen de 24 horas para el registro de ventas.";
    }
}
