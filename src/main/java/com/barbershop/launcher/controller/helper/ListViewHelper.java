package com.barbershop.launcher.controller.helper;

import io.github.palexdev.materialfx.controls.MFXListView;

import java.util.List;

public class ListViewHelper {

    public static <T> void loadItemsOnListView(MFXListView<T> listView, List<T> items) {

        listView.getItems().setAll(items);
    }

    public static <T> void cleanListView(MFXListView<T> listView) {

        listView.getItems().clear();
    }
}
