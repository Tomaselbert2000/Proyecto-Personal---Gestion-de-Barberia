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

import static com.launcher.constants.ui.css_class.MaterialLabel.MD_PAGE_SUBTITLE;
import static com.launcher.controller.helper.ComboBoxHelper.*;
import static com.launcher.controller.helper.ValidationFormatter.generateTextFormatterWithFilter;
import static com.launcher.controller.helper.ValidationFormatter.generateUnaryOperatorFilterForTextFormatterWith;
import static com.utils.strings.RegexPatterns.DECIMAL_REGEX;

public class UIBasicComponents {

    public static HBox createPhoneHBox(int spacing, Pos aligment) {

        HBox hbox = new HBox();
        hbox.setAlignment(aligment);
        hbox.setSpacing(spacing);

        return hbox;
    }

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

        addLabelStyle(emptyListLabel, MD_PAGE_SUBTITLE);

        setMaxLabelWidth(emptyListLabel);

        vBox.getChildren().add(emptyListLabel);
    }

    public static void setMaxLabelWidth(Label label) {

        label.setMaxWidth(Double.MAX_VALUE);
    }

    public static void addLabelStyle(Label label, String style) {

        label.getStyleClass().add(style);
    }

    public static void setBlankTextfield(TextField... textfields) {

        for (TextField textfield : textfields) {

            textfield.setText("");
        }
    }

    public static void setTextOnTextfield(TextField textfield, String text) {

        textfield.setText(text);
    }

    public static void setTextOnButton(Button button, String text) {

        button.setText(text);
    }

    public static void setTextsOnTextfieldMap(Map<TextField, String> map) {

        for (TextField textField : map.keySet()) {

            String text = map.get(textField);

            setTextOnTextfield(textField, text);
        }
    }

    public static void addNodeStyleClass(Node node, String styleClass) {

        if (!node.getStyleClass().contains(styleClass)) {
            node.getStyleClass().add(styleClass);
        }
    }

    public static void setStylesOnNodeMap(Map<Node, String> map) {

        for (Node node : map.keySet()) {

            String styleClass = map.get(node);

            addNodeStyleClass(node, styleClass);
        }
    }

    public static void cleanTextfields(List<TextField> textfields) {

        for (TextField textField : textfields) {

            textField.setText("");
        }
    }

    public static <T> Map<T, String> generateMap(List<T> keys, List<String> values) {

        Map<T, String> map = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {

            map.put(keys.get(i), values.get(i));
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

    public static void setHourAndMinuteSelectors(ComboBox<LocalTime> hourSelector, ComboBox<LocalTime> minuteSelector) {

        List<LocalTime> hours = new ArrayList<>();
        List<LocalTime> minutes = new ArrayList<>();

        for (int i = 8; i <= 20; i++) {

            hours.add(LocalTime.of(i, 0));
        }

        for (int i = 0; i < 60; i += 15) {

            minutes.add(LocalTime.of(0, i));
        }

        loadGenericTypeListOnComboBox(hourSelector, hours); // I use this method because takes a generic type argument
        loadGenericTypeListOnComboBox(minuteSelector, minutes);

        setLocalTimeHourConverter(hourSelector);
        setLocalTimeMinuteConverter(minuteSelector);
    }

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

    public static void configureDecimalTextfieldRestrictions(TextField textfield) {

        UnaryOperator<TextFormatter.Change> unaryOperatorFilter = generateUnaryOperatorFilterForTextFormatterWith(DECIMAL_REGEX);

        TextFormatter<String> stringTextFormatter = generateTextFormatterWithFilter(unaryOperatorFilter);

        textfield.setTextFormatter(stringTextFormatter);
    }

    @SafeVarargs
    public static void configureRunnableMaps(Map<Button, Runnable>... maps) {

        for (Map<Button, Runnable> runnableMap : maps) {

            for (Button button : runnableMap.keySet()) {

                Runnable action = runnableMap.get(button);

                button.setOnAction(_ -> action.run());
            }
        }
    }

    public static void setStatusOnCheckBoxMap(Map<CheckBox, Boolean> checkboxMap) {

        for (CheckBox checkBox : checkboxMap.keySet()) {

            Boolean status = checkboxMap.get(checkBox);

            checkBox.setSelected(status);
        }
    }

    public static void setCheckboxState(Boolean state, CheckBox... checkBoxes) {

        for (CheckBox checkBox : checkBoxes) {

            checkBox.setSelected(state);
        }
    }
}
