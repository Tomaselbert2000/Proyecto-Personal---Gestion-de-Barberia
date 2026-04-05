package com.barbershop.exceptions.sale;

public class InsufficientProductStockException extends RuntimeException{

    @Override
    public String getMessage(){

        return "Uno o más productos ingresados en el registro de compra no cuentan con stock suficiente para continuar con la misma.";
    }
}
