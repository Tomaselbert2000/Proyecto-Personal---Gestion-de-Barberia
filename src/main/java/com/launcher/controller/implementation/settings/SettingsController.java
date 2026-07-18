package com.launcher.controller.implementation.settings;

import com.config.preferences.AppPreferences;
import com.dto.credentials_management.CredentialsUpdateDTO;
import com.dto.settings.SettingsUpdateDTO;
import com.enums.Theme;
import com.enums.ToastNotificationType;
import com.exceptions.credentials.PasswordMismatchException;
import com.launcher.controller.interfaces.Controller;
import com.utils.info.AppInformation;
import com.validation.credentials.CredentialsUpdateValidator;
import com.validation.settings.SettingsUpdateValidator;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

import static com.launcher.constants.ui.css_class.MaterialButton.MD_RADIO_BUTTON;
import static com.launcher.constants.ui.css_class.MaterialContainer.MD_HEADING_GROUP;
import static com.launcher.constants.ui.css_class.MaterialLabel.MD_LIST_ITEM_TITLE;
import static com.launcher.constants.ui.messages.ToastNotificationMessage.SETTINGS_UPDATE_NOTIFICATION_MESSAGE;
import static com.launcher.constants.ui.messages.ValidationErrorMessage.*;
import static com.launcher.constants.ui.prompt_text.SettingsPromptText.*;
import static com.launcher.controller.helper.ContainerManager.*;
import static com.launcher.controller.helper.HelperConstants.ACCEPT_BUTTON_TEXT;
import static com.launcher.controller.helper.HelperConstants.CLIPBOARD_BUTTON_TEXT;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.getConstraintViolationsList;
import static com.launcher.controller.helper.ValidationFormatter.parseNumberValueToText;
import static com.utils.password_generator.PasswordGenerator.generatePassword;

@Component
@RequiredArgsConstructor
public class SettingsController implements Controller {

    private static final boolean DEFAULT_CHECKBOX_STATE = true;
    private static final int THEME_CONTAINER_SPACING = 16;
    private static final int THEME_CARD_SPACING = 8;
    private static final Pos DEFAULT_THEME_CARD_POS = Pos.CENTER_LEFT;
    private static final int THEME_TRANSITION_DURATION_IN_MS = 400;
    private static final int MIN_PASSWORD_LENGTH = 10;
    private static final int MAX_PASSWORD_LENGTH = 32;
    private static final String PASSWORD_GENERATED_SUCCESFULLY = "Contraseña generada exitosamente";
    private static final String CREDENTIALS_UPDATE_SUCCESSFULLY = "Credenciales actualizadas exitosamente";
    private final AppInformation appInformation;
    private final AppPreferences appPreferences;
    private final SettingsUpdateValidator settingsUpdateValidator;
    private final CredentialsUpdateValidator credentialsUpdateValidator;
    private final ApplicationContext applicationContext;
    @FXML
    private final ToggleGroup themeGroup = new ToggleGroup();
    @FXML
    private AnchorPane anchor_pane;
    @FXML
    private HBox theme_container;
    @FXML
    private TextField
            name,
            phone,
            email,
            address,
            user_textfield,
            password_textfield,
            confirm_password_textfield;

    @FXML
    private ComboBox<LocalTime>
            opening_hours,
            closing_hours;

    @FXML
    private CheckBox
            new_appointment_checkbox,
            client_reminder_checkbox,
            low_stock_checkbox,
            workplace_changes_checkbox;

    @FXML
    private MFXButton
            update_password_button,
            suggest_strong_password,
            cancel_button,
            save_button;

    @FXML
    private Slider password_length_slider;

    @FXML
    private Label
            password_length_label,
            version_number,
            framework_name,
            database_name,
            compilation_timestamp,
            developer_name;

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

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(update_password_button, this::updateCredentials),
                Map.entry(suggest_strong_password, this::suggestStrongPassword),
                Map.entry(cancel_button, this::resetValues),
                Map.entry(save_button, this::saveChanges)
        );

        configureRunnableMaps(map);
    }

    private void configureTimeSelectors() {

        configureServiceHoursSelectors(opening_hours, closing_hours);
    }

    private void configureThemeSelector() {

        cleanContainer(theme_container);
        theme_container.setSpacing(THEME_CONTAINER_SPACING);

        for (Theme theme : Theme.values()) {

            VBox themeCard = new VBox(THEME_CARD_SPACING);
            themeCard.setAlignment(DEFAULT_THEME_CARD_POS);
            addNodeStyleClass(themeCard, MD_HEADING_GROUP);

            Label themeLabel = new Label();
            RadioButton radioButton = new RadioButton();
            radioButton.setUserData(theme);
            addNodeStyleClass(radioButton, MD_RADIO_BUTTON);

            addAllChildrensToPane(themeCard, themeLabel, radioButton);
            addAllChildrensToPane(theme_container, themeCard);

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

                        Scene currentScene = theme_container.getScene();

                        Parent parentNode = currentScene.getRoot();

                        WritableImage snapshot = parentNode.snapshot(new SnapshotParameters(), null);

                        ImageView snapshotView = new ImageView(snapshot);

                        ((Pane) parentNode).getChildren().add(snapshotView);

                        changeSceneTheme(currentScene, cssFilePath);

                        playThemeTransitionAnimation(snapshotView, THEME_TRANSITION_DURATION_IN_MS);
                    }
                }
        );
    }

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.ofEntries(
                Map.entry(name, BARBER_SHOP_NAME),
                Map.entry(phone, BARBER_SHOP_PHONE_NUMBER),
                Map.entry(email, BARBER_SHOP_EMAIL),
                Map.entry(address, BARBER_SHOP_ADDRESS),
                Map.entry(user_textfield, USER_ADMIN_NAME),
                Map.entry(password_textfield, USER_ADMIN_PASSWORD),
                Map.entry(confirm_password_textfield, USER_ADMIN_CONFIRMATION_PASSWORD)
        );

        setPromptTextOnMap(map);
    }

    private void configureSlider() {

        setTextOnLabel(password_length_label, "Longitud: " + MIN_PASSWORD_LENGTH + " caracteres");

        password_length_slider.setMin(MIN_PASSWORD_LENGTH);
        password_length_slider.setMax(MAX_PASSWORD_LENGTH);
        password_length_slider.setValue(MIN_PASSWORD_LENGTH);

        password_length_slider.valueChangingProperty().addListener((_, _, _) -> {

                    int value = (int) password_length_slider.getValue();
                    setTextOnLabel(password_length_label, "Longitud: " + parseNumberValueToText(value) + " caracteres");
                }
        );
    }

    private void loadPreferences() {

        Map<TextField, String> preferencesMap = Map.ofEntries(
                Map.entry(name, appPreferences.getBarberShopName()),
                Map.entry(phone, appPreferences.getBarberShopPhoneNumber()),
                Map.entry(email, appPreferences.getBarberShopEmail()),
                Map.entry(address, appPreferences.getBarberShopAddress())
        );

        setTextsOnTextfieldMap(preferencesMap);

        Map<CheckBox, Boolean> notificationsPreferencesMap = Map.ofEntries(
                Map.entry(new_appointment_checkbox, appPreferences.isNewAppointmentNotificationEnabled()),
                Map.entry(client_reminder_checkbox, appPreferences.isClientReminderNotificationEnabled()),
                Map.entry(low_stock_checkbox, appPreferences.isLowStockNotificationEnabled()),
                Map.entry(workplace_changes_checkbox, appPreferences.isWorkplaceChangesNotificationEnabled())
        );

        setStatusOnCheckBoxMap(notificationsPreferencesMap);

        LocalTime parsedOpeningTime = LocalTime.parse(appPreferences.getBarberShopOpeningTime());
        LocalTime parsedClosingTime = LocalTime.parse(appPreferences.getBarberShopClosingTime());

        opening_hours.setValue(parsedOpeningTime);
        closing_hours.setValue(parsedClosingTime);
    }

    private void loadSoftwareInformation() {

        String versionNumber = appInformation.getVersionNumber();
        Instant compilationDate = appInformation.getBuildTimestamp();
        String databaseName = appInformation.getDatabaseName();
        String developerName = appInformation.getDeveloperName();

        Map<Label, String> map = Map.ofEntries(
                Map.entry(version_number, versionNumber),
                Map.entry(framework_name, "Java " + appInformation.getJavaVersion() + ", " + "SpringBoot " + appInformation.getFrameworkVersion()),
                Map.entry(compilation_timestamp, compilationDate.toString()),
                Map.entry(database_name, databaseName),
                Map.entry(developer_name, developerName)
        );

        setTextsOnLabelMap(map);
    }

    private void suggestStrongPassword() {

        int length = (int) password_length_slider.getValue();

        String password = generatePassword(length);

        boolean isClicked = showWindowAlert("", PASSWORD_GENERATED_SUCCESFULLY, password, Alert.AlertType.INFORMATION, CLIPBOARD_BUTTON_TEXT, getCurrentWindow(anchor_pane));

        if (isClicked) {

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();

            content.putString(password);
            clipboard.setContent(content);
        }
    }

    private void updateCredentials() {

        try {

            String username = user_textfield.getText();
            String password = password_textfield.getText();
            String confirmPassword = confirm_password_textfield.getText();

            CredentialsUpdateDTO credentialsUpdateDTO = buildDTOFromCredentialsFields(username, password, confirmPassword);

            credentialsUpdateValidator.validateDTO(credentialsUpdateDTO);

            showToastNotification(anchor_pane, applicationContext, CREDENTIALS_UPDATE_SUCCESSFULLY, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException | PasswordMismatchException exception) {

            if (exception instanceof ConstraintViolationException) {

                String errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);

                showWindowAlert(VALIDATION_ERROR_TITLE, CREDENTIALS_UPDATE_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, ACCEPT_BUTTON_TEXT, getCurrentWindow(anchor_pane));

            } else {

                showWindowAlert(VALIDATION_ERROR_TITLE, "", exception.getMessage(), Alert.AlertType.ERROR, ACCEPT_BUTTON_TEXT, getCurrentWindow(anchor_pane));
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
                    name.getText().isBlank() ? null : name.getText(),
                    phone.getText().isBlank() ? null : phone.getText(),
                    email.getText().isBlank() ? null : email.getText(),
                    address.getText().isBlank() ? null : address.getText(),
                    opening_hours.getValue(),
                    closing_hours.getValue(),
                    new_appointment_checkbox.isSelected(),
                    client_reminder_checkbox.isSelected(),
                    low_stock_checkbox.isSelected(),
                    workplace_changes_checkbox.isSelected()
            );

            settingsUpdateValidator.validateDTO(settingsUpdateDTO);

            appPreferences.saveSettings(settingsUpdateDTO);

            showToastNotification(anchor_pane, applicationContext, SETTINGS_UPDATE_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException exception) {

            String errorMessage = getConstraintViolationsList(exception);

            showWindowAlert(VALIDATION_ERROR_TITLE, SETTINGS_UPDATE_VALIDATION_FAILED, errorMessage, Alert.AlertType.INFORMATION, ACCEPT_BUTTON_TEXT, getCurrentWindow(anchor_pane));
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

        setBlankTextfield(name, phone, email, address);

        opening_hours.setValue(LocalTime.MIN);
        closing_hours.setValue(LocalTime.MAX);

        resetCheckBoxStatus();
    }

    private void resetCheckBoxStatus() {

        setCheckboxState(
                DEFAULT_CHECKBOX_STATE,
                new_appointment_checkbox,
                client_reminder_checkbox,
                low_stock_checkbox,
                workplace_changes_checkbox
        );
    }
}
