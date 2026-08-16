package com.presentation.controller.item;

import java.util.function.Consumer;

public abstract class AbstractItemController<T> implements ItemController<T> {

    protected T infoDTOReference;

    protected abstract void configureButtonActions();

    protected final void fire(Consumer<T> callback) {

        if (callback != null) {

            callback.accept(infoDTOReference);
        }
    }
}
