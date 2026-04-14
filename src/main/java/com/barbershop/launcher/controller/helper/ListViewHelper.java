package com.barbershop.launcher.controller.helper;

import javafx.scene.control.ListView;

import java.util.List;

public class ListViewHelper {

    public static <T> void loadItemsOnListView(ListView<T> listView, List<T> items) {

        listView.getItems().setAll(items);
    }

    public static <T> void cleanListView(ListView<T> listView) {

        listView.getItems().clear();
    }
}
