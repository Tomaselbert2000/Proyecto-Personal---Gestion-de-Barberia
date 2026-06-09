package com.barbershop.utils.resource_helper;

import java.util.Objects;

public final class ResourceLocator {

    public static <T> String getResourceAsExternalForm(Class<T> referencedClass, String resource) {

        if (referencedClass != null && resource != null)
            return Objects.requireNonNull(referencedClass.getResource(resource)).toExternalForm();

        return "";
    }
}
