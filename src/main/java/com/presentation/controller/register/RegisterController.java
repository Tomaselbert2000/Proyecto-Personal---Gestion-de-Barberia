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
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.getConstraintViolationsList;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class RegisterController {

    private final ApplicationContext applicationContext;
    private final AppUserService appUserService;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton back_button, create_user_button;

    @FXML
    private TextField username_field, password_field, confirm_password_field;

    @FXML
    public void initialize() {

        configurePromptTexts();
        configureButtonActions();
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.LOGIN, anchor_pane, applicationContext),
                create_user_button, this::createNewUser
        );

        configureRunnableMaps(map);
    }

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                username_field, USERNAME,
                password_field, PASSWORD,
                confirm_password_field, CONFIRM_PASSWORD
        );

        setPromptTextOnMap(map);
    }

    private void createNewUser() {

        String username = username_field.getText();
        String password = password_field.getText();
        String confirmPassword = confirm_password_field.getText();

        try {

            if (password.equals(confirmPassword)) {

                AppUserCreationDTO creationDTO = AppUserCreationDTO.builder()
                        .username(username)
                        .password(password)
                        .hasAdminRights(false) // placeholder value, this will be refactored in the future
                        .build();

                appUserService.createAppUser(creationDTO);

                redirectToView(ViewRedirection.DASHBOARD, anchor_pane, applicationContext);

            } else {

                showWindowAlert(REGISTER_ERROR_TITLE, REGISTER_VALIDATION_FAILED, PASSWORDS_DOES_NOT_MATCH, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchor_pane));
            }
        } catch (ConstraintViolationException | UsernameTakenException exception) {

            String errorMessage;

            if (exception instanceof ConstraintViolationException) {

                errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);

            } else {

                errorMessage = exception.getMessage();
            }

            showWindowAlert(REGISTER_ERROR_TITLE, REGISTER_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchor_pane));
        }
    }
}
