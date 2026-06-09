package com.barbershop.launcher.controller.helper;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import static com.barbershop.launcher.constants.ui.CssFilePath.*;
import static com.barbershop.launcher.constants.ui.themes.ThemeFilePath.LA_TERCERA_DARK_THEME_FILE_PATH;
import static com.barbershop.launcher.constants.ui.themes.ThemeFilePath.LA_TERCERA_LIGHT_THEME_FILE_PATH;
import static com.barbershop.launcher.controller.helper.HelperConstants.*;
import static com.barbershop.utils.resource_helper.ResourceLocator.getResourceAsExternalForm;

public class ContainerManager {

    private static final double THEME_TRANSITION_STARTING_VALUE = 1.0;
    private static final double THEME_TRANSITION_ENDING_VALUE = 0.0;

    public static void loadItemOnVBox(VBox vBox, Parent parent) {

        vBox.getChildren().add(parent);
    }

    public static void setViewOnAnchorPaneCenter(AnchorPane anchorPane, Parent parent) {

        anchorPane.getChildren().clear();
        configureAnchorMarginOnParent(parent);
        anchorPane.getChildren().add(parent);
    }

    public static void configureAnchorMarginOnParent(Parent parent) {

        AnchorPane.setTopAnchor(parent, ANCHOR_PANE_TOP_ANCHOR_MARGIN_VALUE);
        AnchorPane.setBottomAnchor(parent, ANCHOR_PANE_BOTTOM_ANCHOR_MARGIN_VALUE);
        AnchorPane.setLeftAnchor(parent, ANCHOR_PANE_LEFT_ANCHOR_MARGIN_VALUE);
        AnchorPane.setRightAnchor(parent, ANCHOR_PANE_RIGHT_ANCHOR_MARGIN_VALUE);
    }

    public static void cleanContainer(Pane container) {

        container.getChildren().clear();
    }

    public static void addAllChildrensToPane(Pane pane, Node... nodes) {

        pane.getChildren().addAll(nodes);
    }

    public static void changeSceneTheme(Scene currentScene, String selectedThemeFilePath) {

        currentScene.getStylesheets().clear();

        String designSystemTokenFilePathToExternalForm = getResourceAsExternalForm(ContainerManager.class, DESIGN_SYSTEM_TOKEN_FILE_PATH);
        String themeFilePathToExternalForm = getResourceAsExternalForm(ContainerManager.class, selectedThemeFilePath);

        if (!designSystemTokenFilePathToExternalForm.isEmpty() && !themeFilePathToExternalForm.isEmpty())
            currentScene.getStylesheets().addAll(designSystemTokenFilePathToExternalForm, themeFilePathToExternalForm);

        loadMaterialIconsCSS(currentScene);

        loadFixedColorsCSSIfNeeded(currentScene, selectedThemeFilePath);
    }

    private static void loadFixedColorsCSSIfNeeded(Scene currentScene, String selectedThemeFilePath) {

        if (selectedThemeFilePath.equals(LA_TERCERA_LIGHT_THEME_FILE_PATH) || selectedThemeFilePath.equals(LA_TERCERA_DARK_THEME_FILE_PATH)) {

            String fixedColorsFilePathToExternalForm = getResourceAsExternalForm(ContainerManager.class, LA_TERCERA_FIXED_COLORS_FILE_PATH);

            currentScene.getStylesheets().add(fixedColorsFilePathToExternalForm);
        }
    }

    public static void loadMaterialIconsCSS(Scene newScene) {

        String materialIconsFilePathToExternalForm = getResourceAsExternalForm(ContainerManager.class, MATERIAL_ICONS_FILE_PATH);

        if (!materialIconsFilePathToExternalForm.isEmpty())
            newScene.getStylesheets().add(materialIconsFilePathToExternalForm);
    }

    public static void playThemeTransitionAnimation(ImageView snapshotView, int themeTransitionDurationInMs) {

        FadeTransition transition = new FadeTransition(Duration.millis(themeTransitionDurationInMs), snapshotView);

        transition.setFromValue(THEME_TRANSITION_STARTING_VALUE);
        transition.setToValue(THEME_TRANSITION_ENDING_VALUE);

        transition.play();

        transition.setOnFinished(_ -> {

                    Node parentNode = snapshotView.getParent();

                    ((Pane) parentNode).getChildren().remove(snapshotView);
                }
        );
    }
}
