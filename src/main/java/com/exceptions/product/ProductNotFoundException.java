package com.exceptions.product;

import com.exceptions.BusinessException;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() {
        super("No se encontraron productos con el ID proporcionado.");
    }
}
