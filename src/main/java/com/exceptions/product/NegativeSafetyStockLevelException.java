package com.exceptions.product;

import com.exceptions.BusinessException;

public class NegativeSafetyStockLevelException extends BusinessException {

    public NegativeSafetyStockLevelException() {

        super("No es posible ingresar un producto nuevo con un valor de stock de seguridad menor a cero.");
    }
}
