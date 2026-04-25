package com.barbershop.launcher.controller.helper;

import com.barbershop.enums.AppointmentStatus;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.css_class.CssStylesStrings.EMPTY_LIST_STYLE_CLASS;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.*;

public class UIBasicComponents {

    public static void setTextOnLabel(Label label, String stringValue) {

        label.setText(stringValue);
    }

    public static void setTextsOnLabelMap(Map<Label, String> labelTextsMap) {

        for (Label label : labelTextsMap.keySet()) {

            String text = labelTextsMap.get(label);

            setTextOnLabel(label, text);
        }
    }

    public static void showEmptyListLabel(String message, VBox vBox) {

        Label emptyListLabel = new Label();

        setTextOnLabel(emptyListLabel, message);

        configureLabelStyle(emptyListLabel, EMPTY_LIST_STYLE_CLASS);

        setMaxLabelWidth(emptyListLabel);

        vBox.getChildren().add(emptyListLabel);
    }

    public static void setMaxLabelWidth(Label label) {

        label.setMaxWidth(Double.MAX_VALUE);
    }

    public static void configureLabelStyle(Label label, String style) {

        label.getStyleClass().add(style);
    }

    public static void setBlankTextfield(TextField textField) {

        textField.setText("");
    }

    public static void setTextOnTextfield(TextField textfield, String text) {

        textfield.setText(text);
    }

    public static void setTextsOnTextfieldMap(Map<TextField, String> map) {

        for (TextField textField : map.keySet()) {

            String text = map.get(textField);

            setTextOnTextfield(textField, text);
        }
    }

    public static void setNodeStyleClass(Node node, String styleClass) {

        node.getStyleClass().add(styleClass);
    }

    public static void setStylesOnNodeMap(Map<Node, String> map) {

        for (Node node : map.keySet()) {

            String styleClass = map.get(node);

            setNodeStyleClass(node, styleClass);
        }
    }

    public static void cleanTextfields(List<TextField> textfields) {

        for (TextField textField : textfields) {

            textField.setText("");
        }
    }

    public static <T> Map<T, String> generateMap(List<T> keys, List<String> promptTexts) {

        Map<T, String> map = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {

            map.put(keys.get(i), promptTexts.get(i));
        }

        return map;
    }

    public static void setPromptTextOnMap(Map<TextField, String> textfieldMap) {

        for (TextField textfield : textfieldMap.keySet()) {

            String promtptext = textfieldMap.get(textfield);

            textfield.setPromptText(promtptext);
        }
    }

    public static void setPromptTextOnList(List<TextField> textfields, String value) {

        for (TextField textField : textfields) {

            textField.setPromptText(value);
        }
    }

    public static void cleanDatePicker(DatePicker dateSelector) {

        dateSelector.valueProperty().setValue(null);
    }

    public static void disableButtons(Button... buttons) {

        for (Button button : buttons) {

            button.setDisable(true);
        }
    }

    public static void disableComboBox(ComboBox<AppointmentStatus> comboBox) {

        comboBox.disableProperty();
    }

    public static void setTimeSelectors(ComboBox<LocalTime> hourSelector, ComboBox<LocalTime> minuteSelector) {

        List<LocalTime> hours = new ArrayList<>();
        List<LocalTime> minutes = new ArrayList<>();

        for (int i = 8; i <= 20; i++) {

            hours.add(LocalTime.of(i, 0));
        }

        for (int i = 0; i < 60; i += 15) {

            minutes.add(LocalTime.of(0, i));
        }

        loadDTOsOnComboBox(hourSelector, hours); // I use this method because takes a generic type argument
        loadDTOsOnComboBox(minuteSelector, minutes);

        setLocalTimeHourConverter(hourSelector);
        setLocalTimeMinuteConverter(minuteSelector);
    }
}
