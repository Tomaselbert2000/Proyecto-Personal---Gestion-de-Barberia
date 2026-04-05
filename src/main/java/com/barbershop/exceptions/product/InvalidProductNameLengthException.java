package com.barbershop.exceptions.product;

public class InvalidProductNameLengthException extends RuntimeException{

    @Override
    public String getMessage(){
        return "La longitud permitida de nombres de producto es desde 4 a 100 caracteres";
    }
}
