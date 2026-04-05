package com.barbershop.exceptions.sale;

public class SaleNotFoundException extends RuntimeException{

    @Override
    public String getMessage(){

        return "No se encontraron coincidencias de ventas para el ID proporcionado.";
    }
}
