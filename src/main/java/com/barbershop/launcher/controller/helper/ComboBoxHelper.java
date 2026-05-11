package com.barbershop.launcher.controller.helper;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.util.StringConverter;
import org.jetbrains.annotations.UnknownNullability;

import java.time.LocalTime;
import java.util.List;

public class ComboBoxHelper {

    private static final String LOCAL_TIME_FORMAT = "%02d";

    public static <T> void loadEnumsOnComboBox(MFXComboBox<T> comboBox, T[] values) {

        comboBox.getItems().clear();
        comboBox.getItems().addAll(values);
    }

    public static <T> void removeFirstItemFromComboBox(MFXComboBox<T> comboBox) {

        comboBox.getItems().removeFirst();
    }

    public static void cleanComboBox(@UnknownNullability MFXComboBox<?> comboBox) {

        comboBox.getSelectionModel().clearSelection();
    }

    public static void cleanComboBoxes(MFXComboBox<?>... comboBoxes) {

        for (MFXComboBox<?> comboBox : comboBoxes) {

            cleanComboBox(comboBox);
        }
    }

    public static <T> void loadDTOsOnComboBox(MFXComboBox<T> comboBox, List<T> list) {

        comboBox.getItems().addAll(list);

        comboBox.getItems().addFirst(null);
    }

    public static void setLocalTimeHourConverter(MFXComboBox<LocalTime> comboBox) {

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

    public static void setLocalTimeMinuteConverter(MFXComboBox<LocalTime> comboBox) {

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
