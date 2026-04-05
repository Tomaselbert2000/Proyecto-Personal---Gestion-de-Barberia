package com.barbershop.exceptions.sale;

public class NullSaleInputDataException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Los campos de atributo de venta no pueden ser NULL.";
    }
}
