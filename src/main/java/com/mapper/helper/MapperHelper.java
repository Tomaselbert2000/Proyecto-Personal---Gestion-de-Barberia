package com.mapper.helper;

import com.exceptions.common.NullMapperInputException;

public final class MapperHelper {

    private MapperHelper() {
    }

    public static void checkIfMapperInputIsNull(Object... objects) {

        for (Object object : objects) {

            if (object == null) {

                throw new NullMapperInputException();
            }
        }
    }
}
