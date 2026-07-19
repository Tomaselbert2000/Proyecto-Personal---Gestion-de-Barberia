package com.launcher.controller.helper;

import com.enums.AppointmentStatus;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.launcher.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialLabel.MD_PAGE_SUBTITLE;
import static com.launcher.controller.helper.ComboBoxHelper.*;
import static com.launcher.controller.helper.ValidationFormatter.generateTextFormatterWithFilter;
import static com.launcher.controller.helper.ValidationFormatter.generateUnaryOperatorFilterForTextFormatterWith;
import static com.utils.strings.RegexPatterns.DECIMAL_REGEX;

/**
 * Clase que contiene métodos auxiliares para manipular componentes de interfaz gráfica en JavaFX.
 * Ofrece funciones para crear layouts, manipular etiquetas, campos de texto, botones y otros componentes comunes.
 */

public class UIBasicComponents {

    /**
     * Crea un HBox con el espaciado y alineación especificados.
     *
     * @param spacing   El espaciado entre los elementos del HBox.
     * @param alignment La alineación de los elementos dentro del HBox.
     * @return El HBox creado.
     */
    public static HBox createPhoneHBox(int spacing, Pos alignment) {
        HBox hbox = new HBox();
        hbox.setAlignment(alignment);
        hbox.setSpacing(spacing);
        return hbox;
    }

    /**
     * Establece el texto de una etiqueta.
     *
     * @param label       La etiqueta a la que se establecerá el texto.
     * @param stringValue El texto a establecer en la etiqueta.
     */
    public static void setTextOnLabel(Label label, String stringValue) {
        label.setText(stringValue);
    }

    /**
     * Establece el texto de varias etiquetas a partir de un mapa de etiquetas y valores.
     *
     * @param labelTextsMap El mapa que contiene las etiquetas y sus respectivos valores de texto.
     */
    public static void setTextsOnLabelMap(Map<Label, String> labelTextsMap) {
        for (Label label : labelTextsMap.keySet()) {
            setTextOnLabel(label, labelTextsMap.get(label));
        }
    }

    /**
     * Muestra una etiqueta vacía en un VBox con el mensaje especificado.
     *
     * @param message El mensaje a mostrar en la etiqueta vacía.
     * @param vBox    El VBox donde se añadirá la etiqueta vacía.
     */
    public static void showEmptyListLabel(String message, VBox vBox) {
        Label emptyListLabel = new Label();
        setTextOnLabel(emptyListLabel, message);
        addLabelStyle(emptyListLabel, MD_PAGE_SUBTITLE);
        setMaxLabelWidth(emptyListLabel);
        vBox.getChildren().add(emptyListLabel);
    }

    /**
     * Establece el ancho máximo de una etiqueta.
     *
     * @param label La etiqueta a la que se establecerá el ancho máximo.
     */
    public static void setMaxLabelWidth(Label label) {
        label.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Añade un estilo a una etiqueta.
     *
     * @param label La etiqueta a la que se añadirá el estilo.
     * @param style El estilo a añadir a la etiqueta.
     */
    public static void addLabelStyle(Label label, String style) {
        label.getStyleClass().add(style);
    }

    // TextField manipulation

    /**
     * Establece el texto de varios campos de texto en blanco.
     *
     * @param textfields Los campos de texto a establecer en blanco.
     */
    public static void setBlankTextfield(TextField... textfields) {
        for (TextField textfield : textfields) {
            textfield.setText("");
        }
    }

    /**
     * Establece el texto de un campo de texto.
     *
     * @param textfield El campo de texto al que se establecerá el texto.
     * @param text      El texto a establecer en el campo de texto.
     */
    public static void setTextOnTextfield(TextField textfield, String text) {
        textfield.setText(text);
    }

    /**
     * Establece el texto de varios campos de texto a partir de un mapa de campos de texto y valores.
     *
     * @param map El mapa que contiene los campos de texto y sus respectivos valores de texto.
     */
    public static void setTextsOnTextfieldMap(Map<TextField, String> map) {
        for (TextField textField : map.keySet()) {
            setTextOnTextfield(textField, map.get(textField));
        }
    }

    // Button manipulation

    /**
     * Establece el texto de un botón.
     *
     * @param button El botón al que se establecerá el texto.
     * @param text   El texto a establecer en el botón.
     */
    public static void setTextOnButton(Button button, String text) {
        button.setText(text);
    }

    /**
     * Añade una clase de estilo a un nodo.
     *
     * @param node       El nodo al que se añadirá la clase de estilo.
     * @param styleClass La clase de estilo a añadir al nodo.
     */
    public static void addNodeStyleClass(Node node, String styleClass) {
        if (!node.getStyleClass().contains(styleClass)) {
            node.getStyleClass().add(styleClass);
        }
    }

    /**
     * Añade clases de estilo a varios nodos a partir de un mapa de nodos y clases de estilo.
     *
     * @param map El mapa que contiene los nodos y sus respectivas clases de estilo.
     */
    public static void setStylesOnNodeMap(Map<Node, String> map) {
        for (Node node : map.keySet()) {
            addNodeStyleClass(node, map.get(node));
        }
    }

    // General utilities

    /**
     * Limpia los textos de una lista de campos de texto.
     *
     * @param textfields La lista de campos de texto a limpiar.
     */
    public static void cleanTextfields(List<TextField> textfields) {
        for (TextField textField : textfields) {
            textField.setText("");
        }
    }

    /**
     * Genera un mapa a partir de dos listas: una con claves y otra con valores.
     *
     * @param keys   La lista de claves para el mapa.
     * @param values La lista de valores para el mapa.
     * @param <T>    El tipo de los elementos en la lista de claves.
     * @return El mapa generado a partir de las listas de claves y valores.
     */
    public static <T> Map<T, String> generateMap(List<T> keys, List<String> values) {
        Map<T, String> map = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }

    /**
     * Establece el texto de sugerencia en un mapa de campos de texto.
     *
     * @param textfieldMap El mapa que contiene los campos de texto y sus respectivos textos de sugerencia.
     */
    public static void setPromptTextOnMap(Map<TextField, String> textfieldMap) {
        for (TextField textfield : textfieldMap.keySet()) {
            textfield.setPromptText(textfieldMap.get(textfield));
        }
    }

    /**
     * Establece el texto de sugerencia en una lista de campos de texto.
     *
     * @param textfields La lista de campos de texto a los que se establecerá el texto de sugerencia.
     * @param value      El texto de sugerencia a establecer en todos los campos de texto.
     */
    public static void setPromptTextOnList(List<TextField> textfields, String value) {
        for (TextField textField : textfields) {
            textField.setPromptText(value);
        }
    }

    /**
     * Limpia el valor seleccionado en un DatePicker.
     *
     * @param dateSelector El DatePicker a limpiar.
     */
    public static void cleanDatePicker(DatePicker dateSelector) {
        dateSelector.valueProperty().setValue(null);
    }

    /**
     * Deshabilita varios botones.
     *
     * @param buttons Los botones a deshabilitar.
     */
    public static void disableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }

    /**
     * Deshabilita un ComboBox.
     *
     * @param comboBox El ComboBox a deshabilitar.
     */
    public static void disableComboBox(ComboBox<AppointmentStatus> comboBox) {
        comboBox.disableProperty();
    }

    // Time and date selectors

    /**
     * Configura los selectores de hora y minutos con valores predeterminados.
     *
     * @param hourSelector   El selector de horas a configurar.
     * @param minuteSelector El selector de minutos a configurar.
     */
    public static void setHourAndMinuteSelectors(ComboBox<LocalTime> hourSelector, ComboBox<LocalTime> minuteSelector) {
        List<LocalTime> hours = new ArrayList<>();
        List<LocalTime> minutes = new ArrayList<>();
        for (int i = 8; i <= 20; i++) {
            hours.add(LocalTime.of(i, 0));
        }
        for (int i = 0; i < 60; i += 15) {
            minutes.add(LocalTime.of(0, i));
        }
        loadGenericTypeListOnComboBox(hourSelector, hours);
        loadGenericTypeListOnComboBox(minuteSelector, minutes);
        setLocalTimeHourConverter(hourSelector);
        setLocalTimeMinuteConverter(minuteSelector);
    }

    /**
     * Configura los selectores de horas con valores predeterminados para servicios.
     *
     * @param selectors Los selectores de horas a configurar.
     */
    @SafeVarargs
    public static void configureServiceHoursSelectors(ComboBox<LocalTime>... selectors) {
        List<LocalTime> hours = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            hours.add(LocalTime.of(i, 0));
        }
        for (ComboBox<LocalTime> comboBox : selectors) {
            loadGenericTypeListOnComboBox(comboBox, hours);
            setLocalTimeHourConverter(comboBox);
        }
    }

    // Textfield restrictions

    /**
     * Configura las restricciones de texto decimal en un campo de texto.
     *
     * @param textfield El campo de texto al que se aplicarán las restricciones de texto decimal.
     */
    public static void configureDecimalTextfieldRestrictions(TextField textfield) {
        UnaryOperator<TextFormatter.Change> unaryOperatorFilter = generateUnaryOperatorFilterForTextFormatterWith(DECIMAL_REGEX);
        TextFormatter<String> stringTextFormatter = generateTextFormatterWithFilter(unaryOperatorFilter);
        textfield.setTextFormatter(stringTextFormatter);
    }

    /**
     * Configura las acciones de los mapas de botones y acciones.
     *
     * @param maps Los mapas que contienen los botones y sus respectivas acciones.
     */
    @SafeVarargs
    public static void configureRunnableMaps(Map<Button, Runnable>... maps) {
        for (Map<Button, Runnable> runnableMap : maps) {
            for (Button button : runnableMap.keySet()) {
                Runnable action = runnableMap.get(button);
                button.setOnAction(_ -> action.run());
            }
        }
    }

    // Checkbox manipulation

    /**
     * Establece el estado de varios checkboxes a partir de un mapa de checkboxes y estados.
     *
     * @param checkboxMap El mapa que contiene los checkboxes y sus respectivos estados.
     */
    public static void setStatusOnCheckBoxMap(Map<CheckBox, Boolean> checkboxMap) {
        for (CheckBox checkBox : checkboxMap.keySet()) {
            checkBox.setSelected(checkboxMap.get(checkBox));
        }
    }

    /**
     * Establece el estado de varios checkboxes.
     *
     * @param state      El estado a establecer en todos los checkboxes.
     * @param checkBoxes Los checkboxes a establecer.
     */
    public static void setCheckboxState(Boolean state, CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setSelected(state);
        }
    }
}