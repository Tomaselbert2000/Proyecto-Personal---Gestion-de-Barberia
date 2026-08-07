package com.presentation.controller.settings;

import com.config.preferences.AppPreferences;
import com.dto.credentials.CredentialsUpdateDTO;
import com.dto.settings.SettingsUpdateDTO;
import com.enums.Theme;
import com.enums.ToastNotificationType;
import com.exceptions.credentials.PasswordMismatchException;
import com.utils.info.AppInformation;
import com.validation.credentials.CredentialsUpdateValidator;
import com.validation.settings.SettingsUpdateValidator;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Map;

import static com.presentation.constants.ControllerConstants.SettingsControllerConstants.*;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialButton.MD_RADIO_BUTTON;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialContainer.MD_HEADING_GROUP;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialLabel.MD_LIST_ITEM_TITLE;
import static com.presentation.constants.PromptTexts.SettingsPromptText.*;
import static com.presentation.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.presentation.constants.StringResource.DisplayString.ACCEPT_BUTTON_TEXT;
import static com.presentation.constants.StringResource.DisplayString.CLIPBOARD_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.SETTINGS_UPDATE_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.*;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.getConstraintViolationsList;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ContainerManager.*;
import static com.utils.password_generator.PasswordGenerator.generatePassword;

@Component
@RequiredArgsConstructor
public class SettingsController {

    private final AppInformation appInformation;
    private final AppPreferences appPreferences;
    private final SettingsUpdateValidator settingsUpdateValidator;
    private final CredentialsUpdateValidator credentialsUpdateValidator;
    private final ApplicationContext applicationContext;

    @FXML
    private final ToggleGroup themeGroup = new ToggleGroup();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private HBox themeContainer;
    @FXML
    private TextField
            nameField,
            phoneField,
            emailField,
            addressField,
            adminUsernameField,
            adminPasswordField,
            adminConfirmPasswordField;

    @FXML
    private ComboBox<LocalTime>
            openingHours,
            closingHours;

    @FXML
    private CheckBox
            newAppointmentCheckbox,
            clientReminderCheckbox,
            lowStockCheckbox,
            workplaceChangesCheckbox;

    @FXML
    private MFXButton
            updatePasswordButton,
            suggestStrongPasswordButton,
            cancelButton,
            saveButton;

    @FXML
    private Slider passwordLengthSlider;

    @FXML
    private Label
            passwordLengthLabel,
            versionNumber,
            frameworkName,
            databaseName,
            compilationTimestamp,
            developerName;

    @FXML
    public void initialize() {

        configureButtonActions();

        configureTimeSelectors();

        configureThemeSelector();

        configurePromptTexts();

        configureSlider();

        loadPreferences();

        loadSoftwareInformation();
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(updatePasswordButton, this::updateCredentials),
                Map.entry(suggestStrongPasswordButton, this::suggestStrongPassword),
                Map.entry(cancelButton, this::resetValues),
                Map.entry(saveButton, this::saveChanges)
        );

        configureRunnableMaps(map);
    }

    private void configureTimeSelectors() {

        configureServiceHoursSelectors(openingHours, closingHours);
    }

    private void configureThemeSelector() {

        cleanContainer(themeContainer);
        themeContainer.setSpacing(THEME_CONTAINER_SPACING);

        for (Theme theme : Theme.values()) {

            VBox themeCard = new VBox(THEME_CARD_SPACING);
            themeCard.setAlignment(DEFAULT_THEME_CARD_POS);
            addNodeStyleClass(themeCard, MD_HEADING_GROUP);

            Label themeLabel = new Label();
            RadioButton radioButton = new RadioButton();
            radioButton.setUserData(theme);
            addNodeStyleClass(radioButton, MD_RADIO_BUTTON);

            addAllChildrensToPane(themeCard, themeLabel, radioButton);
            addAllChildrensToPane(themeContainer, themeCard);

            setTextOnLabel(themeLabel, theme.getDisplayName());
            addLabelStyle(themeLabel, MD_LIST_ITEM_TITLE);

            radioButton.setToggleGroup(themeGroup);

            if (theme.name().equals(appPreferences.getTheme())) {

                radioButton.setSelected(true);
            }
        }

        themeGroup.selectedToggleProperty().addListener((_, _, newValue) -> {

                    if (newValue != null) {

                        Theme selectedTheme = (Theme) newValue.getUserData();
                        String cssFilePath = selectedTheme.getThemeFilePath();

                        Scene currentScene = themeContainer.getScene();

                        if (currentScene != null) {

                            Parent parentNode = currentScene.getRoot();

                            WritableImage snapshot = parentNode.snapshot(new SnapshotParameters(), null);

                            ImageView snapshotView = new ImageView(snapshot);

                            ((Pane) parentNode).getChildren().add(snapshotView);

                            changeSceneTheme(currentScene, cssFilePath);

                            playThemeTransitionAnimation(snapshotView, THEME_TRANSITION_DURATION_IN_MS);
                        }
                    }
                }
        );
    }

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.ofEntries(
                Map.entry(nameField, BARBER_SHOP_NAME),
                Map.entry(phoneField, BARBER_SHOP_PHONE_NUMBER),
                Map.entry(emailField, BARBER_SHOP_EMAIL),
                Map.entry(addressField, BARBER_SHOP_ADDRESS),
                Map.entry(adminUsernameField, USER_ADMIN_NAME),
                Map.entry(adminPasswordField, USER_ADMIN_PASSWORD),
                Map.entry(adminConfirmPasswordField, USER_ADMIN_CONFIRMATION_PASSWORD)
        );

        setPromptTextOnMap(map);
    }

    private void configureSlider() {

        setTextOnLabel(passwordLengthLabel, "Longitud: " + MIN_PASSWORD_LENGTH + " caracteres");

        passwordLengthSlider.setMin(MIN_PASSWORD_LENGTH);
        passwordLengthSlider.setMax(MAX_PASSWORD_LENGTH);
        passwordLengthSlider.setValue(MIN_PASSWORD_LENGTH);

        passwordLengthSlider.valueChangingProperty().addListener((_, _, _) -> {

                    int value = (int) passwordLengthSlider.getValue();
                    setTextOnLabel(passwordLengthLabel, "Longitud: " + parseNumberValueToText(value) + " caracteres");
                }
        );
    }

    private void loadPreferences() {

        Map<TextField, String> preferencesMap = Map.ofEntries(
                Map.entry(nameField, appPreferences.getBarberShopName()),
                Map.entry(phoneField, appPreferences.getBarberShopPhoneNumber()),
                Map.entry(emailField, appPreferences.getBarberShopEmail()),
                Map.entry(addressField, appPreferences.getBarberShopAddress())
        );

        setTextsOnTextfieldMap(preferencesMap);

        Map<CheckBox, Boolean> notificationsPreferencesMap = Map.ofEntries(
                Map.entry(newAppointmentCheckbox, appPreferences.isNewAppointmentNotificationEnabled()),
                Map.entry(clientReminderCheckbox, appPreferences.isClientReminderNotificationEnabled()),
                Map.entry(lowStockCheckbox, appPreferences.isLowStockNotificationEnabled()),
                Map.entry(workplaceChangesCheckbox, appPreferences.isWorkplaceChangesNotificationEnabled())
        );

        setStatusOnCheckBoxMap(notificationsPreferencesMap);

        LocalTime parsedOpeningTime = LocalTime.parse(appPreferences.getBarberShopOpeningTime());
        LocalTime parsedClosingTime = LocalTime.parse(appPreferences.getBarberShopClosingTime());

        openingHours.setValue(parsedOpeningTime);
        closingHours.setValue(parsedClosingTime);
    }

    private void loadSoftwareInformation() {

        String versionNumberValue = appInformation.getVersionNumber();
        Instant compilationDate = appInformation.getBuildTimestamp();
        String databaseNameValue = appInformation.getDatabaseName();
        String developerNameValue = appInformation.getDeveloperName();

        Map<Label, String> map = Map.ofEntries(
                Map.entry(versionNumber, versionNumberValue),
                Map.entry(frameworkName, "Java " + appInformation.getJavaVersion() + ", " + "SpringBoot " + appInformation.getFrameworkVersion()),
                Map.entry(compilationTimestamp, compilationDate.toString()),
                Map.entry(databaseName, databaseNameValue),
                Map.entry(developerName, developerNameValue)
        );

        setTextsOnLabelMap(map);
    }

    private void suggestStrongPassword() {

        int length = (int) passwordLengthSlider.getValue();

        String password = generatePassword(length);

        boolean isClicked = showWindowAlert("", PASSWORD_GENERATED_SUCCESFULLY, password, Alert.AlertType.INFORMATION, CLIPBOARD_BUTTON_TEXT, getCurrentWindow(anchorPane));

        if (isClicked) {

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();

            content.putString(password);
            clipboard.setContent(content);
        }
    }

    private void updateCredentials() {

        try {

            String username = adminUsernameField.getText();
            String password = adminPasswordField.getText();
            String confirmPassword = adminConfirmPasswordField.getText();

            CredentialsUpdateDTO credentialsUpdateDTO = buildDTOFromCredentialsFields(username, password, confirmPassword);

            credentialsUpdateValidator.validateDTO(credentialsUpdateDTO);

            showToastNotification(anchorPane, applicationContext, CREDENTIALS_UPDATE_SUCCESSFULLY, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException | PasswordMismatchException exception) {

            if (exception instanceof ConstraintViolationException) {

                String errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);

                showWindowAlert(VALIDATION_ERROR_TITLE, CREDENTIALS_UPDATE_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchorPane));

            } else {

                showWindowAlert(VALIDATION_ERROR_TITLE, "", exception.getMessage(), Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchorPane));
            }
        }
    }

    private CredentialsUpdateDTO buildDTOFromCredentialsFields(String username, String password, String confirmPassword) {

        return CredentialsUpdateDTO.builder()
                .username(username)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
    }

    private void saveChanges() {

        try {

            Theme selectedTheme = (Theme) themeGroup.getSelectedToggle().getUserData();

            String themeName = selectedTheme.name();

            SettingsUpdateDTO settingsUpdateDTO = buildDTOFromSettingsFields(
                    themeName,
                    nameField.getText().isBlank() ? null : nameField.getText(),
                    phoneField.getText().isBlank() ? null : phoneField.getText(),
                    emailField.getText().isBlank() ? null : emailField.getText(),
                    addressField.getText().isBlank() ? null : addressField.getText(),
                    openingHours.getValue(),
                    closingHours.getValue(),
                    newAppointmentCheckbox.isSelected(),
                    clientReminderCheckbox.isSelected(),
                    lowStockCheckbox.isSelected(),
                    workplaceChangesCheckbox.isSelected()
            );

            settingsUpdateValidator.validateDTO(settingsUpdateDTO);

            appPreferences.saveSettings(settingsUpdateDTO);

            showToastNotification(anchorPane, applicationContext, SETTINGS_UPDATE_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException exception) {

            String errorMessage = getConstraintViolationsList(exception);

            showWindowAlert(VALIDATION_ERROR_TITLE, SETTINGS_UPDATE_VALIDATION_FAILED, errorMessage, Alert.AlertType.INFORMATION, ACCEPT_BUTTON_TEXT, getCurrentWindow(anchorPane));
        }
    }

    private SettingsUpdateDTO buildDTOFromSettingsFields(
            String theme,
            String name,
            String phone,
            String email,
            String address,
            LocalTime openingTime,
            LocalTime closingTime,
            Boolean newAppointmentCheckboxSelectedValue,
            Boolean clientReminderCheckBoxSelectedValue,
            Boolean lowStockCheckBoxSelectedValue,
            Boolean workplaceChangesCheckBoxSelectedValue) {

        return SettingsUpdateDTO.builder()
                .themeSelected(theme)
                .name(name)
                .phone(phone)
                .email(email)
                .address(address)
                .openingHour(openingTime)
                .closingHour(closingTime)
                .newAppointmentNotificationEnabled(newAppointmentCheckboxSelectedValue)
                .clientReminderNotificationEnabled(clientReminderCheckBoxSelectedValue)
                .lowStockNotificationEnabled(lowStockCheckBoxSelectedValue)
                .workplaceChangesNotificationEnabled(workplaceChangesCheckBoxSelectedValue)
                .build();
    }

    private void resetValues() {

        setBlankTextfield(nameField, phoneField, emailField, addressField);

        openingHours.setValue(LocalTime.MIN);
        closingHours.setValue(LocalTime.MAX);

        resetCheckBoxStatus();
    }

    private void resetCheckBoxStatus() {

        setCheckboxState(
                DEFAULT_CHECKBOX_STATE,
                newAppointmentCheckbox,
                clientReminderCheckbox,
                lowStockCheckbox,
                workplaceChangesCheckbox
        );
    }
}
