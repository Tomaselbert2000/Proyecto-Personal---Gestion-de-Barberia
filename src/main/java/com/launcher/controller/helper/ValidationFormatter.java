package com.launcher.controller.helper;

import com.enums.DescribableEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.launcher.constants.StringResource.DisplayString.NEW_LINE;
import static com.launcher.constants.StringResource.StringFormat.*;

public class ValidationFormatter {

    /**
     * Genera un filtro para un TextFormatter que solo permite texto que coincida con una expresión regular.
     *
     * @param regex La expresión regular que se utilizará para filtrar el texto.
     * @return El filtro de TextFormatter generado.
     */
    public static UnaryOperator<TextFormatter.Change> generateUnaryOperatorFilterForTextFormatterWith(String regex) {
        return change -> change.getControlNewText().matches(regex) ? change : null;
    }

    /**
     * Genera un TextFormatter con el filtro especificado.
     *
     * @param unaryOperatorFilter El filtro que se utilizará en el TextFormatter.
     * @return El TextFormatter generado.
     */
    public static TextFormatter<String> generateTextFormatterWithFilter(UnaryOperator<TextFormatter.Change> unaryOperatorFilter) {
        return new TextFormatter<>(unaryOperatorFilter);
    }

    /**
     * Formatea un valor de tipo Double como una cadena de precio.
     *
     * @param value El valor de tipo Double a formatear.
     * @return La cadena de texto formateada como precio.
     */
    public static String formatAsPrice(Double value) {
        return String.format(PRICE_FORMAT, value);
    }

    /**
     * Formatea un valor de tipo Double como una cadena con un solo decimal.
     *
     * @param percentage El valor de tipo Double a formatear.
     * @return La cadena de texto formateada con un solo decimal.
     */
    public static String formatAsDecimalValue(Double percentage) {
        return String.format(ONE_DECIMAL_FORMAT, percentage);
    }

    /**
     * Formatea un valor de tipo Double como una cadena de porcentaje.
     *
     * @param percentage El valor de tipo Double a formatear.
     * @return La cadena de texto formateada como porcentaje.
     */
    public static String formatAsPercentage(Double percentage) {
        return String.format(PERCENTAGE_FORMAT, percentage);
    }

    /**
     * Establece un conversor para un ComboBox que muestra el nombre descriptivo de los elementos.
     *
     * @param comboBox     El ComboBox al que se establecerá el conversor.
     * @param defaultValue El valor por defecto a mostrar si el elemento es null.
     * @param <T>          El tipo de elementos en el ComboBox.
     */
    public static <T extends DescribableEnum> void setStringConverter(ComboBox<T> comboBox, T defaultValue) {
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T item) {
                return Objects.requireNonNullElse(item, defaultValue).getDisplayName();
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        });
    }

    /**
     * Parsea un texto a un valor de tipo Integer.
     *
     * @param text El texto a parsear.
     * @return El valor de tipo Integer parseado, o null si el texto es nulo o vacío.
     */
    public static Integer parseTextToInteger(String text) {
        return text == null || text.trim().isEmpty() ? null : Integer.valueOf(text);
    }

    /**
     * Parsea un texto a un valor de tipo Double.
     *
     * @param text El texto a parsear.
     * @return El valor de tipo Double parseado, o null si el texto es nulo o vacío.
     */
    public static Double parseTextToDouble(String text) {
        return text == null || text.trim().isEmpty() ? null : Double.valueOf(text);
    }

    /**
     * Convierte un valor numérico a una cadena de texto.
     *
     * @param value El valor numérico a convertir.
     * @return La cadena de texto resultante.
     */
    public static String parseNumberValueToText(Integer value) {
        return String.valueOf(value);
    }

    /**
     * Convierte un valor numérico a una cadena de texto.
     *
     * @param value El valor numérico a convertir.
     * @return La cadena de texto resultante.
     */
    public static String parseNumberValueToText(Double value) {
        return String.valueOf(value);
    }

    /**
     * Convierte un valor numérico a una cadena de texto.
     *
     * @param value El valor numérico a convertir.
     * @return La cadena de texto resultante.
     */
    public static String parseNumberValueToText(Long value) {
        return String.valueOf(value);
    }

    /**
     * Obtiene una lista de mensajes de violación de restricciones a partir de una excepción ConstraintViolationException.
     *
     * @param exception La excepción ConstraintViolationException que contiene las violaciones de restricción.
     * @return La lista de mensajes de violación de restricción concatenados con saltos de línea.
     */
    public static String getConstraintViolationsList(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(NEW_LINE));
    }
}