package com.launcher.controller.implementation.login;

import com.config.preferences.AppPreferences;
import com.enums.ViewRedirection;
import com.launcher.controller.interfaces.Controller;
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

import static com.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.launcher.constants.ui.messages.ValidationErrorMessage.*;
import static com.launcher.controller.helper.ContainerManager.getCurrentWindow;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.UIBasicComponents.configureRunnableMaps;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class LoginController implements Controller {

    private static final String RETRY_LOGIN_BUTTON_TEXT = "Reintentar";
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

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> actionButtonsMap = Map.of(
                login_button, this::manageLoginProcess,
                register_button, this::manageRegisterProcess
        );

        configureRunnableMaps(actionButtonsMap);
    }

    private void manageLoginProcess() {

        String username = username_field.getText();
        String password = password_field.getText();

        if (usernameAndPasswordAreBlank(username, password)) {

            showWindowAlert(LOGIN_ERROR_TITLE, "", LOGIN_FIELDS_BLANK, Alert.AlertType.ERROR, RETRY_LOGIN_BUTTON_TEXT, getCurrentWindow(anchor_pane));

        } else {

            verifyCredentials(username, password);
        }
    }

    private void verifyCredentials(String username, String password) {

        executeUITask(
                () -> appUserService.signIn(username, password),
                uiActionValue -> {
                    if (uiActionValue) {

                        appPreferences.setCurrentUser(username);
                        appPreferences.setRememberCredentials(remember_session_checkbox.isSelected());

                        redirectToView(ViewRedirection.DASHBOARD, anchor_pane, applicationContext);

                    } else {

                        showWindowAlert(LOGIN_ERROR_TITLE, "", LOGIN_FAILED, Alert.AlertType.ERROR, RETRY_LOGIN_BUTTON_TEXT, getCurrentWindow(anchor_pane));

                    }
                }
        );
    }

    private boolean usernameAndPasswordAreBlank(String username, String password) {

        return username.isBlank() || password.isBlank();
    }

    private void manageRegisterProcess() {

    }
}
