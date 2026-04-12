package com.barbershop.launcher.controller;

import com.barbershop.enums.*;
import com.barbershop.launcher.controller.dashboard.DashboardController;
import com.barbershop.launcher.controller.dialog.DeleteConfirmationDialogController;
import com.barbershop.launcher.controller.toast_notification.ToastNotificationController;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.barbershop.launcher.constants.ui.css_class.CssStylesStrings.EMPTY_LIST_STYLE_CLASS;
import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.DELETE_CONFIRMATION_DIALOG_VIEW_LOADING_FAILED;
import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.TOAST_NOTIFICATION_VIEW_LOADING_FAILED;
import static com.barbershop.launcher.constants.view.ViewPath.*;

public class UI_RenderingFunctions {

    private static final Double ANCHOR_PANE_TOP_ANCHOR_MARGIN_VALUE = 0.0;
    private static final Double ANCHOR_PANE_BOTTOM_ANCHOR_MARGIN_VALUE = 0.0;
    private static final Double ANCHOR_PANE_LEFT_ANCHOR_MARGIN_VALUE = 0.0;
    private static final Double ANCHOR_PANE_RIGHT_ANCHOR_MARGIN_VALUE = 0.0;
    private static final Double TOAST_NOTIFICATION_RIGHT_ANCHOR_MARGIN_VALUE = 20.0;
    private static final Double TOAST_NOTIFICATION_TOP_ANCHOR_MARGIN_VALUE = 20.0;
    private static final Integer ERROR_MESSAGE_DURATION_IN_SECONDS = 5;
    private static final Integer GAUSSIAN_BLUR_VALUE = 10;
    private static final String PRICE_FORMAT = "%.2f";
    private static final String PERCENTAGE_FORMAT = "%.1f";
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("*.png", "*.jpg");
    private static final String FILE_CHOOSER_IMAGE_DESCRIPTION = "Seleccionar una imagen";
    private static final String ERROR_MESSAGE_NEW_LINE = "\n• ";

    public static FXMLLoader generateLoaderWithPath(String path) {

        return new FXMLLoader(UI_RenderingFunctions.class.getResource(path));
    }

    public static void setTextOnLabel(Label label, String stringValue) {

        label.setText(stringValue);
    }

    public static void setBlankTextfield(TextField textField) {

        textField.setText("");
    }

    public static void setTextsOnLabelMap(Map<Label, String> labelTextsMap) {

        for (Label label : labelTextsMap.keySet()) {

            String text = labelTextsMap.get(label);

            setTextOnLabel(label, text);
        }
    }

    public static void setTextsOnTextfieldMap(Map<TextField, String> map) {

        for (TextField textField : map.keySet()) {

            String text = map.get(textField);

            textField.setText(text);
        }
    }

    public static void configureLabelStyle(Label label, String style) {

        label.getStyleClass().add(style);
    }

    public static void setMaxLabelWidth(Label label) {

        label.setMaxWidth(Double.MAX_VALUE);
    }

    public static void setControllerOnLoader(FXMLLoader loader, ApplicationContext applicationContext) {

        loader.setControllerFactory(applicationContext::getBean);
    }

    public static Parent returnParentFromLoader(FXMLLoader loader, String optionalErrorMessage) {

        try {
            return loader.load();

        } catch (IOException exception) {

            throw new RuntimeException(optionalErrorMessage, exception);
        }
    }

    public static void loadItemOnVBox(VBox vBox, Parent parent) {

        vBox.getChildren().add(parent);
    }

    public static void showEmptyListLabel(String message, VBox vBox) {

        Label emptyListLabel = new Label();

        setTextOnLabel(emptyListLabel, message);

        configureLabelStyle(emptyListLabel, EMPTY_LIST_STYLE_CLASS);

        setMaxLabelWidth(emptyListLabel);

        vBox.getChildren().add(emptyListLabel);
    }

    public static void setViewOnBorderPaneCenter(BorderPane borderPane, Parent view) {

        borderPane.setCenter(view);
    }

    public static void setViewOnAnchorPaneCenter(AnchorPane anchorPane, Parent parent) {

        anchorPane.getChildren().clear();
        configureAnchorMarginOnParent(parent);
        anchorPane.getChildren().add(parent);
    }

    private static void configureAnchorMarginOnParent(Parent parent) {

        AnchorPane.setTopAnchor(parent, ANCHOR_PANE_TOP_ANCHOR_MARGIN_VALUE);
        AnchorPane.setBottomAnchor(parent, ANCHOR_PANE_BOTTOM_ANCHOR_MARGIN_VALUE);
        AnchorPane.setLeftAnchor(parent, ANCHOR_PANE_LEFT_ANCHOR_MARGIN_VALUE);
        AnchorPane.setRightAnchor(parent, ANCHOR_PANE_RIGHT_ANCHOR_MARGIN_VALUE);
    }

    public static void setNodeStyleClass(Node node, String styleClass) {

        node.getStyleClass().add(styleClass);
    }

    public static void setToastNotificationOnAnchorPane(AnchorPane anchorPane, Parent toastNotificationView) {

        anchorPane.getChildren().add(toastNotificationView);
        configureToastNotificationMargins(toastNotificationView);
    }

    private static void configureToastNotificationMargins(Parent toastNotificationView) {

        AnchorPane.setTopAnchor(toastNotificationView, TOAST_NOTIFICATION_TOP_ANCHOR_MARGIN_VALUE);
        AnchorPane.setRightAnchor(toastNotificationView, TOAST_NOTIFICATION_RIGHT_ANCHOR_MARGIN_VALUE);
    }

    public static PauseTransition generatePauseTransition(Duration seconds) {

        return new PauseTransition(seconds);
    }

    public static Timeline generateTimelineInstance(
            Region region,
            Integer toastProgressBarWidthValue,
            Integer toastNotificationDurationInSeconds,
            Double toastProgressBarWidthValueOnFinish
    ) {

        return new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(region.prefWidthProperty(), toastProgressBarWidthValue)),
                new KeyFrame(Duration.seconds(toastNotificationDurationInSeconds), new KeyValue(region.prefWidthProperty(), toastProgressBarWidthValueOnFinish))
        );
    }

    public static void loadViewOnPane(
            String viewPath,
            ApplicationContext applicationContext,
            String optionalErrorMessage,
            Pane pane
    ) {

        FXMLLoader loader = generateLoaderWithPath(viewPath);

        setControllerOnLoader(loader, applicationContext);

        Parent parent = returnParentFromLoader(loader, optionalErrorMessage);

        if (pane instanceof BorderPane) {

            setViewOnBorderPaneCenter((BorderPane) pane, parent);

        } else {

            setViewOnAnchorPaneCenter((AnchorPane) pane, parent);
        }
    }

    public static void redirectToView(ApplicationContext applicationContext, ViewRedirection viewRedirection) {

        DashboardController dashboardController = applicationContext.getBean(DashboardController.class);

        switch (viewRedirection) {

            case DASHBOARD -> dashboardController.showDashboardView();
            case CLIENTS -> dashboardController.showClientView();
            case EMPLOYEES -> dashboardController.showEmployeeView();
            case APPOINTMENTS -> dashboardController.showAppointmentView();
            case BARBER_SERVICES -> dashboardController.showBarberServiceView();
            case PRODUCTS -> dashboardController.showProductView();
            case SETTINGS -> dashboardController.showSettingsView();
            case LOGOUT -> dashboardController.showLogoutView();
        }
    }

    public static void cleanTextfields(List<TextField> textfields) {

        for (TextField textField : textfields) {

            textField.setText("");
        }
    }

    public static void showToastNotification(AnchorPane anchor_pane, ApplicationContext applicationContext, String message, ToastNotificationType notificationType) {

        FXMLLoader loader = generateLoaderWithPath(TOAST_NOTIFICATION_VIEW_PATH);

        setControllerOnLoader(loader, applicationContext);

        Parent toastNotificationView = returnParentFromLoader(loader, TOAST_NOTIFICATION_VIEW_LOADING_FAILED);

        ToastNotificationController toastNotificationController = loader.getController();

        toastNotificationController.configureAndShowOnScreen(message, anchor_pane, toastNotificationView, notificationType);

        setToastNotificationOnAnchorPane(anchor_pane, toastNotificationView);
    }

    public static void showExceptionErrorMessage(RuntimeException exception, Label error_message_label, VBox error_message_container) {

        String errorMessage = exception.getMessage();

        error_message_label.setText(errorMessage);

        error_message_container.setVisible(true);

        setErrorMessageTransition(error_message_container);
    }

    public static void setErrorMessageTransition(VBox generalErrorContainer) {

        PauseTransition pauseTransition = generatePauseTransition(Duration.seconds(ERROR_MESSAGE_DURATION_IN_SECONDS));
        pauseTransition.setOnFinished(_ -> generalErrorContainer.setVisible(false));
        pauseTransition.play();
    }

    public static <T> void loadEnumsOnComboBox(ComboBox<T> categoryComboBox, T[] values) {

        categoryComboBox.getItems().clear();
        categoryComboBox.getItems().addAll(values);
    }

    public static void loadStringsOnComboBox(ComboBox<String> comboBox, List<String> stringList) {

        for (String string : stringList) {

            comboBox.getItems().add(string);
        }
    }

    public static <T> void cleanComboBox(ComboBox<T> comboBox) {

        comboBox.getSelectionModel().clearAndSelect(0);
    }

    public static void cleanComboBoxes(ComboBox<?>... comboBoxes) {

        for (ComboBox<?> comboBox : comboBoxes) {

            cleanComboBox(comboBox);
        }
    }

    public static void cleanVBox(VBox vBox) {

        vBox.getChildren().clear();
    }

    public static <T> void removeFirstEnumFromComboBox(ComboBox<T> comboBox) {

        comboBox.getItems().removeFirst();
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

    public static void showConfirmationDialog(AnchorPane anchorPane, ApplicationContext applicationContext, String title, String message, Runnable onConfirm, Runnable onCancel) {

        List<Node> nodesToBlur = anchorPane.getChildren();

        setGaussianBlurOnNodes(nodesToBlur);

        FXMLLoader loader = generateLoaderWithPath(DELETE_CONFIRMATION_VIEW_PATH);

        setControllerOnLoader(loader, applicationContext);

        Parent dialogView = returnParentFromLoader(loader, DELETE_CONFIRMATION_DIALOG_VIEW_LOADING_FAILED);

        DeleteConfirmationDialogController deleteConfirmationDialogController = loader.getController();

        Runnable onCloseDialog = () -> {

            removeBlur(nodesToBlur);
            removeNodeFromAnchorPane(anchorPane, dialogView);
        };

        deleteConfirmationDialogController.configureDialog(title, message, onConfirm, onCancel, onCloseDialog);

        configureAnchorMarginOnParent(dialogView);

        anchorPane.getChildren().add(dialogView);
    }

    private static void setGaussianBlurOnNodes(List<Node> nodesToBlur) {

        for (Node node : nodesToBlur) {

            node.setEffect(new GaussianBlur(GAUSSIAN_BLUR_VALUE));
        }
    }

    private static void removeBlur(List<Node> nodesToUnblur) {

        for (Node node : nodesToUnblur) {

            node.setEffect(null);
        }
    }

    private static void removeNodeFromAnchorPane(AnchorPane anchorPane, Parent view) {

        anchorPane.getChildren().remove(view);
    }

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

    public static void setStylesOnNodeMap(Map<Node, String> map) {

        for (Node node : map.keySet()) {

            String styleClass = map.get(node);

            setNodeStyleClass(node, styleClass);
        }
    }

    public static String formatAsPrice(Double value) {

        return String.format(PRICE_FORMAT, value);
    }

    public static String formatAsPercentage(Double percentage) {

        return String.format(PERCENTAGE_FORMAT, percentage);
    }

    public static <T extends DescribableEnum> void setStringConverter(ComboBox<T> comboBox, T defaultValue) {

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

    public static File getFileFromFileChooser(AnchorPane currentView) {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = generateExtensionFilter();

        fileChooser.getExtensionFilters().add(extensionFilter);

        return new FileChooser().showOpenDialog(currentView.getScene().getWindow());
    }

    private static FileChooser.ExtensionFilter generateExtensionFilter() {

        return new FileChooser.ExtensionFilter(FILE_CHOOSER_IMAGE_DESCRIPTION, ALLOWED_IMAGE_EXTENSIONS);
    }

    public static void loadFileOnImageView(File file, ImageView imageView) {

        URI uriLocation = URI.create(file.toURI().toString());

        imageView.setImage(new Image(String.valueOf(uriLocation)));
    }

    public static void cleanImageView(ImageView imageView) {

        imageView.setImage(null);
    }

    public static void cleanDatePicker(DatePicker dateSelector) {

        dateSelector.valueProperty().setValue(null);
    }

    public static void disableButtons(Button ... buttons) {

        for (Button button : buttons){

            button.setDisable(true);
        }
    }
}
