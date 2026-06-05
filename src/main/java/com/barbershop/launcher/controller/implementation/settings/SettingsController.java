package com.barbershop.launcher.controller.implementation.settings;

import com.barbershop.config.AppPreferences;
import com.barbershop.enums.Theme;
import com.barbershop.launcher.controller.interfaces.Controller;
import com.barbershop.utils.info.AppInformation;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.css_class.MaterialButton.MD_RADIO_BUTTON;
import static com.barbershop.launcher.constants.ui.css_class.MaterialContainer.MD_HEADING_GROUP;
import static com.barbershop.launcher.constants.ui.css_class.MaterialLabel.MD_LIST_ITEM_TITLE;
import static com.barbershop.launcher.controller.helper.ContainerManager.*;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;

@Component
@RequiredArgsConstructor
public class SettingsController implements Controller {

    private final AppInformation appInformation;

    private static final int THEME_CONTAINER_SPACING = 16;
    private static final int THEME_CARD_SPACING = 8;
    private static final Pos DEFAULT_THEME_CARD_POS = Pos.CENTER_LEFT;
    private static final int THEME_TRANSITION_DURATION_IN_MS = 400;

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
            cancel_button,
            save_button;

    @FXML
    private Label
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

        loadSoftwareInformation();
    }

    @Override
    public void configureButtonActions() {

    }

    private void configureTimeSelectors() {

        configureServiceHoursSelectors(opening_hours, closing_hours);
    }

    private void configureThemeSelector() {

        ToggleGroup themeGroup = new ToggleGroup();

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

            if (theme == Theme.MD3_LIGHT) {

                radioButton.setSelected(true);

            }

            addAllChildrensToPane(themeCard, themeLabel, radioButton);
            addAllChildrensToPane(theme_container, themeCard);

            setTextOnLabel(themeLabel, theme.getDisplayName());
            addLabelStyle(themeLabel, MD_LIST_ITEM_TITLE);

            radioButton.setToggleGroup(themeGroup);
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

    //TODO: completar logica para persistir ajustes
}
