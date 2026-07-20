package com.launcher.controller.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

import static com.launcher.constants.CssResourceFilePath.DESIGN_SYSTEM_TOKEN_FILE_PATH;
import static com.launcher.constants.StringResource.DisplayString.NEW_LINE;
import static com.utils.resource_helper.ResourceLocator.getResourceAsExternalForm;

public class PopUpWindowHelper {

    /**
     * Muestra una ventana de alerta con el título, mensaje de encabezado y contenido especificados.
     *
     * @param title          El título de la ventana de alerta.
     * @param headerMessage  El mensaje de encabezado de la ventana de alerta.
     * @param contentMessage El mensaje de contenido de la ventana de alerta.
     * @param alertType      El tipo de alerta a mostrar (INFORMATION, WARNING, ERROR, etc.).
     * @param buttonText     El texto del botón de acción en la ventana de alerta.
     * @param windowOwner    La ventana propietaria que abrirá la ventana de alerta.
     * @return true si el usuario hace clic en el botón de acción, false en caso contrario.
     */
    public static boolean showWindowAlert(
            String title,
            String headerMessage,
            String contentMessage,
            Alert.AlertType alertType,
            String buttonText,
            Window windowOwner
    ) {
        Alert alert = new Alert(alertType);
        setStyleSheetOnWindow(alert, getResourceAsExternalForm(PopUpWindowHelper.class, DESIGN_SYSTEM_TOKEN_FILE_PATH));
        alert.setTitle(title);
        alert.setHeaderText(headerMessage);
        alert.setContentText(NEW_LINE + contentMessage);
        alert.getButtonTypes().setAll(new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE));
        alert.initOwner(windowOwner);

        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        return optionalButtonType.isPresent() && optionalButtonType.get() == alert.getButtonTypes().getFirst();
    }

    /**
     * Establece una hoja de estilo en la ventana de alerta.
     *
     * @param alert  La ventana de alerta a la que se establecerá la hoja de estilo.
     * @param cssURL La URL de la hoja de estilo CSS a aplicar.
     */
    private static void setStyleSheetOnWindow(Alert alert, String cssURL) {
        alert.getDialogPane().getStylesheets().add(cssURL);
    }
}