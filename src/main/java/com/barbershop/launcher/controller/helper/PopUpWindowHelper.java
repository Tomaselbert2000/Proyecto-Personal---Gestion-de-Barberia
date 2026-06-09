package com.barbershop.launcher.controller.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import static com.barbershop.launcher.constants.ui.CssFilePath.DESIGN_SYSTEM_TOKEN_FILE_PATH;
import static com.barbershop.launcher.controller.helper.HelperConstants.NEW_LINE;
import static com.barbershop.utils.resource_helper.ResourceLocator.getResourceAsExternalForm;

public class PopUpWindowHelper {

    public static boolean showWindowAlert(
            String title,
            String headerMessage,
            String contentMessage,
            Alert.AlertType alertType,
            String buttonText
    ) {

        Alert alert = new Alert(alertType);

        String cssURL = getResourceAsExternalForm(PopUpWindowHelper.class, DESIGN_SYSTEM_TOKEN_FILE_PATH);

        setStyleSheetOnWindow(alert, cssURL);

        alert.setTitle(title);
        alert.setHeaderText(headerMessage);
        alert.setContentText(NEW_LINE + contentMessage);

        ButtonType customButton = new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE);

        alert.getButtonTypes().setAll(customButton);

        var result = alert.showAndWait();

        return result.isPresent() && result.get() == customButton;
    }

    private static void setStyleSheetOnWindow(Alert alert, String cssURL) {

        alert.getDialogPane().getStylesheets().add(cssURL);
    }
}
