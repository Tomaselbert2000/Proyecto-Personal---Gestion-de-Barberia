package com.barbershop.launcher.controller.helper;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.util.List;

public class ComboBoxHelper {

    private static final String LOCAL_TIME_FORMAT = "%02d";

    public static <T> void loadEnumsOnComboBox(ComboBox<T> comboBox, T[] values) {

        comboBox.getItems().clear();
        comboBox.getItems().addAll(values);
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
