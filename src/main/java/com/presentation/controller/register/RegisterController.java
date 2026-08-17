package com.presentation.controller.register;

import com.dto.appuser.AppUserCreationDTO;
import com.enums.ViewRedirection;
import com.exceptions.appuser.UsernameTakenException;
import com.service.interfaces.AppUserService;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.presentation.constants.PromptTexts.AppUserPromptText.*;
import static com.presentation.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ValidationErrorMessage.*;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setPromptTextOnMap;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.notification.ExceptionNotificationHandler.notifyValidationFailure;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import com.presentation.support.view.ViewRedirectionHelper;

@Component
@RequiredArgsConstructor
public class RegisterController {

    private final ApplicationContext applicationContext;
    private final ViewRedirectionHelper viewRedirectionHelper;
    private final AppUserService appUserService;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton backButton, createUserButton;

    @FXML
    private TextField usernameField, passwordField, confirmPasswordField;

    @FXML
    public void initialize() {

        configurePromptTexts();
        configureButtonActions();
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                backButton, () -> viewRedirectionHelper.redirectToView(ViewRedirection.LOGIN, anchorPane, applicationContext),
                createUserButton, this::createNewUser
        );

        configureRunnableMaps(map);
    }

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                usernameField, USERNAME,
                passwordField, PASSWORD,
                confirmPasswordField, CONFIRM_PASSWORD
        );

        setPromptTextOnMap(map);
    }

    private void createNewUser() {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        try {

            if (password.equals(confirmPassword)) {

                AppUserCreationDTO creationDTO = AppUserCreationDTO.builder()
                        .username(username)
                        .password(password)
                        .hasAdminRights(false) // placeholder value, this will be refactored in the future
                        .build();

                appUserService.createAppUser(creationDTO);

                viewRedirectionHelper.redirectToView(ViewRedirection.DASHBOARD, anchorPane, applicationContext);

            } else {

                showWindowAlert(REGISTER_ERROR_TITLE, REGISTER_VALIDATION_FAILED, PASSWORDS_DOES_NOT_MATCH, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchorPane));
            }
        } catch (ConstraintViolationException | UsernameTakenException exception) {

            notifyValidationFailure(anchorPane, exception, REGISTER_ERROR_TITLE, REGISTER_VALIDATION_FAILED);
        }
    }
}
