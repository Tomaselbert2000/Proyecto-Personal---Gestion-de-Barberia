package com.exceptions.sale;

import com.exceptions.BusinessException;

public class SaleNotFoundException extends BusinessException {

    public SaleNotFoundException() {

        super("No se encontraron coincidencias de ventas para el ID proporcionado.");
    }
}
