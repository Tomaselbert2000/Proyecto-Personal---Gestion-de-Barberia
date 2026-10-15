package com.exceptions.product;

import com.exceptions.BusinessException;

public class InvalidProductImageException extends BusinessException {

    public InvalidProductImageException() {

        super("La imagen del producto no es válida. Se aceptan archivos JPG o PNG.");
    }
}
