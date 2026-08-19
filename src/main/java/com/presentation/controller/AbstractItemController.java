package com.presentation.controller;

import com.presentation.controller.item.ItemController;
import javafx.fxml.FXML;

import java.util.function.Consumer;

public abstract class AbstractItemController<T> implements ItemController<T> {

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    protected T infoDTOReference;

    protected abstract void configureButtonActions();

    protected final void fire(Consumer<T> callback) {

        if (callback != null) {

            callback.accept(infoDTOReference);
        }
    }
}
