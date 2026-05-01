package com.barbershop.launcher.controller.interfaces;

public interface EditionController<T> extends Controller {

    void configureButtonActions(T infoDTO);

    void resetForm(T infoDTO);
}