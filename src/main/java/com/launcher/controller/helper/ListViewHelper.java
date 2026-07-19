package com.launcher.controller.helper;

import io.github.palexdev.materialfx.controls.MFXListView;

import java.util.List;

public class ListViewHelper {

    /**
     * Carga una lista de elementos en un MFXListView.
     *
     * @param listView El MFXListView al que se cargarán los elementos.
     * @param items    La lista de elementos a cargar en el MFXListView.
     */
    public static <T> void loadItemsOnListView(MFXListView<T> listView, List<T> items) {
        listView.getItems().setAll(items);
    }

    /**
     * Limpia todos los elementos de un MFXListView.
     *
     * @param listView El MFXListView que se limpiará.
     */
    public static <T> void cleanListView(MFXListView<T> listView) {
        listView.getItems().clear();
    }
}