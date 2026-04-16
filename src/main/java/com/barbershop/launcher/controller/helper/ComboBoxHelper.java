package com.barbershop.launcher.controller.helper;

import javafx.scene.control.ComboBox;

import java.util.List;

public class ComboBoxHelper {

    public static <T> void loadEnumsOnComboBox(ComboBox<T> comboBox, T[] values) {

        comboBox.getItems().clear();
        comboBox.getItems().addAll(values);
    }

    public static void loadStringsOnComboBox(ComboBox<String> comboBox, List<String> stringList) {

        for (String string : stringList) {

            comboBox.getItems().add(string);
        }
    }

    public static <T> void removeFirstItemFromComboBox(ComboBox<T> comboBox) {

        comboBox.getItems().removeFirst();
    }

    public static <T> void cleanComboBox(ComboBox<T> comboBox) {

        comboBox.getSelectionModel().clearAndSelect(0);
    }

    public static void cleanComboBoxes(ComboBox<?>... comboBoxes) {

        for (ComboBox<?> comboBox : comboBoxes) {

            cleanComboBox(comboBox);
        }
    }

    public static <T> void loadDTOsOnComboBox(ComboBox<T> comboBox, List<T> list) {

        comboBox.getItems().addAll(list);

        comboBox.getItems().addFirst(null);
    }
}
