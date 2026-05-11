package com.barbershop.launcher.controller.helper;

import com.barbershop.enums.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.barbershop.launcher.controller.helper.HelperConstants.*;

public class ValidationFormatter {

    public static UnaryOperator<TextFormatter.Change> generateUnaryOperatorFilterForTextFormatterWith(String regex) {

        return change -> {

            String text = change.getControlNewText();

            if (text.matches(regex)) {

                return change;

            } else {

                return null;
            }
        };
    }

    public static TextFormatter<String> generateTextFormatterWithFilter(UnaryOperator<TextFormatter.Change> unaryOperatorFilter) {

        return new TextFormatter<>(unaryOperatorFilter);
    }

    public static String formatAsPrice(Double value) {

        return String.format(PRICE_FORMAT, value);
    }

    public static String formatAsDecimalValue(Double percentage) {

        return String.format(ONE_DECIMAL_FORMAT, percentage);
    }

    public static String formatAsPercentage(Double percentage) {

        return String.format(PERCENTAGE_FORMAT, percentage);
    }

    public static <T extends DescribableEnum> void setStringConverter(MFXComboBox<T> comboBox, T defaultValue) {

        comboBox.setConverter(

                new StringConverter<>() {
                    @Override
                    public String toString(T item) {

                        return Objects.requireNonNullElse(item, defaultValue).getDisplayName();
                    }

                    @Override
                    public T fromString(String string) {

                        return null;
                    }
                }
        );
    }

    public static Integer parseTextToInteger(String text) {

        if (text == null || text.trim().isEmpty()) {

            return null;
        }

        try {

            return Integer.valueOf(text);

        } catch (NumberFormatException e) {

            return null;
        }
    }

    public static Double parseTextToDouble(String text) {

        if (text == null || text.trim().isEmpty()) {

            return null;
        }

        try {

            return Double.valueOf(text);

        } catch (NumberFormatException e) {

            return null;
        }
    }

    public static String parseNumberValueToText(Integer value) {

        return String.valueOf(value);
    }

    public static String parseNumberValueToText(Double value) {

        return String.valueOf(value);
    }

    public static String parseNumberValueToText(Long value) {

        return String.valueOf(value);
    }

    public static String getConstraintViolationsList(ConstraintViolationException exception) {

        return exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(ERROR_MESSAGE_NEW_LINE));
    }

    public static void showErrorAlert(String title, String headerMessage, String errorMessages) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(headerMessage);
        alert.setContentText(ERROR_MESSAGE_NEW_LINE + errorMessages);
        alert.showAndWait();
    }
}
