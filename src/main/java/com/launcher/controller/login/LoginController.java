package com.launcher.controller.login;

import com.config.preferences.AppPreferences;
import com.service.interfaces.AppUserService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.enums.ViewRedirection.DASHBOARD;
import static com.enums.ViewRedirection.REGISTER;
import static com.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.launcher.constants.StringResource.ValidationErrorMessage.LOGIN_ERROR_TITLE;
import static com.launcher.constants.StringResource.ValidationErrorMessage.LOGIN_FAILED;
import static com.launcher.controller.helper.ContainerManager.getCurrentWindow;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.UIBasicComponents.configureRunnableMaps;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class LoginController {

    private final ApplicationContext applicationContext;
    private final AppPreferences appPreferences;
    private final AppUserService appUserService;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private TextField username_field, password_field;

    @FXML
    private CheckBox remember_session_checkbox;

    @FXML
    private MFXButton login_button, register_button;

    @FXML
    public void initialize() {
        configureButtonActions();
    }

    private void configureButtonActions() {
        Map<Button, Runnable> actionButtonsMap = Map.of(
                login_button, this::handleLoginClick,
                register_button, this::handleRegisterClick
        );

        configureRunnableMaps(actionButtonsMap);
    }

    private void handleLoginClick() {
        String username = extractUsername();
        String password = extractPassword();

        if (validateInputFields(username, password)) {
            verifyCredentialsAndRedirect(username, password);
        } else {
            showLoginError();
        }
    }

    private void handleRegisterClick() {
        redirectToView(REGISTER, anchor_pane, applicationContext);
    }

    private String extractUsername() {
        return username_field.getText();
    }

    private String extractPassword() {
        return password_field.getText();
    }

    private boolean validateInputFields(String username, String password) {
        return !username.isBlank() && !password.isBlank();
    }

    private void verifyCredentialsAndRedirect(String username, String password) {
        executeUITask(
                () -> appUserService.signIn(username, password),
                uiActionValue -> {
                    if (uiActionValue) {
                        handleSuccessfulLogin(username);
                    } else {
                        showLoginError();
                    }
                }
        );
    }

    private void handleSuccessfulLogin(String username) {
        appPreferences.setCurrentUser(username);
        appPreferences.setRememberCredentials(remember_session_checkbox.isSelected());
        redirectToView(DASHBOARD, anchor_pane, applicationContext);
    }

    private void showLoginError() {
        showWindowAlert(LOGIN_ERROR_TITLE, "", LOGIN_FAILED, Alert.AlertType.ERROR, com.launcher.constants.ControllerConstants.LoginControllerConstants.RETRY_LOGIN_BUTTON_TEXT, getCurrentWindow(anchor_pane));
    }
}
