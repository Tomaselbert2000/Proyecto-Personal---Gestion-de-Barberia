package com.exceptions.product;

import com.exceptions.BusinessException;

public class ConcurrentStockModificationException extends BusinessException {

    public ConcurrentStockModificationException() {
        super("El stock de uno o más productos fue modificado por otra transacción. Por favor, recargue e intente nuevamente.");
    }
}
