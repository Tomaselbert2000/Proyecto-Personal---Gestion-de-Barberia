package com.exceptions.sale;

import com.exceptions.BusinessException;

public class EmptyProductItemListException extends BusinessException {

    public EmptyProductItemListException() {

        super("La lista de productos ingresada para la venta está vacía.");
    }
}
