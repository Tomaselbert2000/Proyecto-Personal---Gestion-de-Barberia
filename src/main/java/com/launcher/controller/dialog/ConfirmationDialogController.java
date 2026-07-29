package com.launcher.controller.dialog;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import static com.launcher.controller.helper.UIBasicComponents.*;

@Component
public class ConfirmationDialogController {

    @FXML
    private Node dialog_icon;

    @FXML
    private Label
            confirmation_title,
            confirmation_message;

    @FXML
    private MFXButton
            cancel_button,
            confirm_button;

    /**
     * Configura el cuadro de diálogo con los parámetros proporcionados.
     *
     * @param title             Título del cuadro de diálogo.
     * @param message           Mensaje del cuadro de diálogo.
     * @param cancelButtonText  Texto del botón de cancelación.
     * @param confirmButtonText Texto del botón de confirmación.
     * @param iconStyleClass    Clase de estilo para el icono.
     * @param onConfirm         Acción a ejecutar cuando se confirme el cuadro de diálogo.
     * @param onCancel          Acción a ejecutar cuando se cancele el cuadro de diálogo.
     * @param onCloseDialog     Acción a ejecutar cuando se cierre el cuadro de diálogo.
     */
    public void configureDialog(String title, String message, String cancelButtonText, String confirmButtonText, String iconStyleClass, Runnable onConfirm, Runnable onCancel, Runnable onCloseDialog) {
        setTextOnLabel(confirmation_title, title);
        setTextOnLabel(confirmation_message, message);
        setTextOnButton(cancel_button, cancelButtonText);
        setTextOnButton(confirm_button, confirmButtonText);
        addNodeStyleClass(dialog_icon, iconStyleClass);

        if (onCancel != null && onCloseDialog != null) {
            cancel_button.setOnAction(_ -> {
                onCancel.run();
                onCloseDialog.run();
            });
        }

        if (onConfirm != null && onCloseDialog != null) {
            confirm_button.setOnAction(_ -> {
                onConfirm.run();
                onCloseDialog.run();
            });
        }
    }
}