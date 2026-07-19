package com.launcher.controller.implementation.dialog;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import static com.launcher.constants.ui.css_class.CssStylesStrings.CONFIRM_DELETE_DIALOG_WARNING_ICON;
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

    public void configureDialog(String title,
                                String message,
                                String cancelButtonText,
                                String confirmButtonText,
                                Runnable onConfirm,
                                Runnable onCancel,
                                Runnable onCloseDialog) {

        setTextOnLabel(confirmation_title, title);
        setTextOnLabel(confirmation_message, message);
        setTextOnButton(cancel_button, cancelButtonText);
        setTextOnButton(confirm_button, confirmButtonText);

        addNodeStyleClass(dialog_icon, CONFIRM_DELETE_DIALOG_WARNING_ICON);

        if (onCancel != null && onCloseDialog != null) {

            cancel_button.setOnAction(_ -> {

                        onCancel.run();
                        onCloseDialog.run();
                    }
            );
        }

        if (onConfirm != null && onCloseDialog != null) {

            confirm_button.setOnAction(_ -> {

                        onConfirm.run();
                        onCloseDialog.run();
                    }
            );
        }
    }
}
