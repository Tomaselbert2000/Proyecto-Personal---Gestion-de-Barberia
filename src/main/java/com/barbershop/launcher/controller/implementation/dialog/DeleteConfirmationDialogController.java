package com.barbershop.launcher.controller.implementation.dialog;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import static com.barbershop.launcher.constants.ui.css_class.CssStylesStrings.CONFIRM_DELETE_DIALOG_WARNING_ICON;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.setNodeStyleClass;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.setTextOnLabel;

@Component
public class DeleteConfirmationDialogController {

    @FXML
    private Node dialog_icon;

    @FXML
    private Label confirmation_title;

    @FXML
    private Label confirmation_message;

    @FXML
    private Button cancel_button;

    @FXML
    private Button confirm_button;

    public void configureDialog(String title, String message, Runnable onConfirm, Runnable onCancel, Runnable onCloseDialog) {

        setTextOnLabel(confirmation_title, title);
        setTextOnLabel(confirmation_message, message);

        setNodeStyleClass(dialog_icon, CONFIRM_DELETE_DIALOG_WARNING_ICON);

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
