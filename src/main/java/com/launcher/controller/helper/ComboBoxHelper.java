package com.launcher.controller.helper;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.jetbrains.annotations.UnknownNullability;

import java.time.LocalTime;
import java.util.List;

/**
 * Clase auxiliar para gestionar componentes ComboBox en la aplicación BarberShop.
 * Proporciona métodos para poblar, limpiar y administrar datos dentro de los ComboBoxes.
 */

public class ComboBoxHelper {

    private static final String LOCAL_TIME_FORMAT = "%02d";

    public static <T> void loadEnumsOnComboBox(ComboBox<T> comboBox, T[] values) {

        comboBox.getItems().clear();
        comboBox.getItems().addAll(values);
    }

    public static <T> void removeFirstItemFromComboBox(ComboBox<T> comboBox) {

        comboBox.getItems().removeFirst();
    }

    public static void cleanComboBox(@UnknownNullability ComboBox<?> comboBox) {

        comboBox.getSelectionModel().clearSelection();
    }

    public static void cleanComboBoxes(ComboBox<?>... comboBoxes) {

        for (ComboBox<?> comboBox : comboBoxes) {

            cleanComboBox(comboBox);
        }
    }

    public static <T> void loadGenericTypeListOnComboBox(ComboBox<T> comboBox, List<T> list) {

        comboBox.getItems().addAll(list);

        comboBox.getItems().addFirst(null);
    }

    public static void setLocalTimeHourConverter(ComboBox<LocalTime> comboBox) {

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalTime object) {

                if (object == null) return "";

                return String.format(LOCAL_TIME_FORMAT, object.getHour());
            }

            @Override
            public LocalTime fromString(String string) {
                return null;
            }
        });
    }

    public static void setLocalTimeMinuteConverter(ComboBox<LocalTime> comboBox) {

        comboBox.setConverter(new StringConverter<>() {
                                  @Override
                                  public String toString(LocalTime object) {

                                      if (object == null) return "";

                                      return String.format(LOCAL_TIME_FORMAT, object.getMinute());
                                  }

                                  @Override
                                  public LocalTime fromString(String string) {
                                      return null;
                                  }
                              }
        );
    }
}
