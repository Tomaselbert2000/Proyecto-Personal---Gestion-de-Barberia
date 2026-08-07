package com.exceptions.product;

import com.exceptions.BusinessException;

public class NegativeCurrentStockLevelException extends BusinessException {

    public NegativeCurrentStockLevelException() {

        super("No es posible ingresar un producto nuevo con valor de stock actual menor a cero.");
    }
}
