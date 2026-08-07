package com.exceptions.common;

import com.exceptions.BusinessException;

public class NullMapperInputException extends BusinessException {

    public NullMapperInputException() {

        super("Uno o más objetos requeridos para el mapeo de nueva entidad son NULL.");
    }
}
