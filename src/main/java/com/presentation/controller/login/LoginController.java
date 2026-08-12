package com.presentation.controller.login;

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
import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.constants.StringResource.ValidationErrorMessage.LOGIN_ERROR_TITLE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.LOGIN_FAILED;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class LoginController {

    private final ApplicationContext applicationContext;
    private final AppPreferences appPreferences;
    private final AppUserService appUserService;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField usernameTextField, passwordTextField;

    @FXML
    private CheckBox rememberSessionCheckBox;

    @FXML
    private MFXButton loginButton, registerButton;

    @FXML
    public void initialize() {
        configureButtonActions();
    }

    private void configureButtonActions() {

        Map<Button, Runnable> actionButtonsMap = Map.of(
                loginButton, this::handleLoginClick,
                registerButton, this::handleRegisterClick
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

        redirectToView(REGISTER, anchorPane, applicationContext);
    }

    private String extractUsername() {

        return usernameTextField.getText();
    }

    private String extractPassword() {

        return passwordTextField.getText();
    }

    private boolean validateInputFields(String username, String password) {

        return !username.isBlank() && !password.isBlank();
    }

    private void verifyCredentialsAndRedirect(String username, String password) {

        executeAsyncTask(
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
        appPreferences.setRememberCredentials(rememberSessionCheckBox.isSelected());

        redirectToView(DASHBOARD, anchorPane, applicationContext);
    }

    private void showLoginError() {

        showWindowAlert(
                LOGIN_ERROR_TITLE,
                "",
                LOGIN_FAILED,
                Alert.AlertType.ERROR,
                com.presentation.constants.ControllerConstants.LoginControllerConstants.RETRY_LOGIN_BUTTON_TEXT,
                getCurrentWindow(anchorPane));
    }
}
