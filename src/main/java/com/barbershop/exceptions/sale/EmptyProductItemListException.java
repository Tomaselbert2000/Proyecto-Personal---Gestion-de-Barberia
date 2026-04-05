package com.barbershop.exceptions.sale;

public class EmptyProductItemListException extends RuntimeException {

    @Override
    public String getMessage(){

        return "La lista de productos ingresada para la venta está vacía.";
    }
}
