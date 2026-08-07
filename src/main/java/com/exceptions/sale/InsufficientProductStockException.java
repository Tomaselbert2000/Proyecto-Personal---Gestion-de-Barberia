package com.exceptions.sale;

import com.exceptions.BusinessException;

public class InsufficientProductStockException extends BusinessException {

    public InsufficientProductStockException() {

        super("Uno o más productos ingresados en el registro de compra no cuentan con stock suficiente para continuar con la misma.");
    }
}
