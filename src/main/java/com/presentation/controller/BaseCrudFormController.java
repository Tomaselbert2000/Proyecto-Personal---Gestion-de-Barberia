package com.presentation.controller;

import com.enums.ToastNotificationType;
import jakarta.validation.ConstraintViolationException;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;

import static com.presentation.support.notification.ExceptionNotificationHandler.notifyValidationFailure;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;

public abstract class BaseCrudFormController<InputDTO, InfoDTO> {

    private final ApplicationContext applicationContext;

    protected InfoDTO internalInfoDTOReference;

    protected BaseCrudFormController(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    protected ApplicationContext getApplicationContext() {

        return applicationContext;
    }

    protected abstract AnchorPane getAnchorPane();

    protected abstract void persistEntity(InputDTO dto);

    protected abstract String getSuccessMessage();

    protected abstract String getErrorMessage();

    protected final void saveEntity() {

        try {

            InputDTO dto = buildDTO();

            persistEntity(dto);

            notifySuccess(getSuccessMessage());

            resetForm();

        } catch (ConstraintViolationException exception) {

            handleConstraintViolation(exception, getErrorMessage());
        }
    }

    protected abstract InputDTO buildDTO();

    protected abstract void resetForm();

    protected final void notifySuccess(String message) {

        showToastNotification(getAnchorPane(), applicationContext, message, ToastNotificationType.SUCCESSFUL);
    }

    protected final void handleConstraintViolation(ConstraintViolationException exception, String failMessage) {

        notifyValidationFailure(
                getAnchorPane(),
                exception,
                com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE,
                failMessage
        );
    }
}
