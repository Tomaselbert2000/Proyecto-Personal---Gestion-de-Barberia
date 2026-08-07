package com.exceptions.common;

import com.exceptions.BusinessException;

public class NullDTOException extends BusinessException {

    public NullDTOException() {
        super("El objeto de transferencia de datos enviado es NULL");
    }
}
