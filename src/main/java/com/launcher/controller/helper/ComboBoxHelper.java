package com.launcher.controller.helper;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.jetbrains.annotations.UnknownNullability;

import java.time.LocalTime;
import java.util.List;

import static com.launcher.constants.HelperConstants.ComboBoxHelperConstants.LOCAL_TIME_FORMAT;

public class ComboBoxHelper {

    /**
     * Carga los valores de un enum en un ComboBox.
     *
     * @param comboBox El ComboBox al que se cargarán los valores del enum.
     * @param values   Los valores del enum a cargar en el ComboBox.
     */
    public static <T> void loadEnumsOnComboBox(ComboBox<T> comboBox, T[] values) {
        comboBox.getItems().clear();
        comboBox.getItems().addAll(values);
    }

    /**
     * Elimina el primer elemento de un ComboBox.
     *
     * @param comboBox El ComboBox del que se eliminará el primer elemento.
     */
    public static <T> void removeFirstItemFromComboBox(ComboBox<T> comboBox) {
        comboBox.getItems().removeFirst();
    }

    /**
     * Limpia la selección en un ComboBox.
     *
     * @param comboBox El ComboBox a limpiar.
     */
    public static void cleanComboBox(@UnknownNullability ComboBox<?> comboBox) {
        comboBox.getSelectionModel().clearSelection();
    }

    /**
     * Limpia las selecciones en varios ComboBox.
     *
     * @param comboBoxes Los ComboBox a limpiar.
     */
    public static void cleanComboBoxes(ComboBox<?>... comboBoxes) {
        for (ComboBox<?> comboBox : comboBoxes) {
            cleanComboBox(comboBox);
        }
    }

    /**
     * Carga una lista genérica en un ComboBox y añade un elemento nulo al principio.
     *
     * @param comboBox El ComboBox al que se cargará la lista.
     * @param list     La lista genérica a cargar en el ComboBox.
     */
    public static <T> void loadGenericTypeListOnComboBox(ComboBox<T> comboBox, List<T> list) {
        comboBox.getItems().addAll(list);
        comboBox.getItems().addFirst(null);
    }

    /**
     * Establece un conversor para un ComboBox de LocalTime que solo muestra la hora.
     *
     * @param comboBox El ComboBox de LocalTime al que se establecerá el conversor.
     */
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

    /**
     * Establece un conversor para un ComboBox de LocalTime que solo muestra los minutos.
     *
     * @param comboBox El ComboBox de LocalTime al que se establecerá el conversor.
     */
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
        });
    }
}