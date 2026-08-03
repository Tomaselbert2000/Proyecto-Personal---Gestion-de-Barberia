package com.presentation.support.notification;

import jakarta.validation.ConstraintViolationException;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import static com.presentation.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.presentation.support.control.ValidationFormatter.getConstraintViolationsList;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;

public class ExceptionNotificationHandler {

    public static void notifyValidationFailure(
            AnchorPane anchorPane,
            Exception exception,
            String validationTitle,
            String validationSubject
    ) {

        String errorMessage;

        if (exception instanceof ConstraintViolationException) {

            errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);

        } else {

            errorMessage = exception.getMessage();
        }

        showWindowAlert(validationTitle, validationSubject, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchorPane));
    }
}
