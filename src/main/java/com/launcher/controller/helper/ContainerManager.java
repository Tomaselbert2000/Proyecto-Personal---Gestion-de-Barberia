package com.launcher.controller.helper;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Duration;

import static com.launcher.constants.CssResourceFilePath.LA_TERCERA_FIXED_COLORS_FILE_PATH;
import static com.launcher.constants.CssResourceFilePath.MATERIAL_ICONS_FILE_PATH;
import static com.launcher.constants.HelperConstants.ContainerManagerConstants.*;
import static com.launcher.constants.ThemeFilePath.LA_TERCERA_DARK_THEME_FILE_PATH;
import static com.launcher.constants.ThemeFilePath.LA_TERCERA_LIGHT_THEME_FILE_PATH;
import static com.utils.resource_helper.ResourceLocator.getResourceAsExternalForm;

/**
 * Clase encargada de gestionar los contenedores en la aplicación BarberShop.
 * Proporciona métodos para crear, eliminar y manipular diferentes tipos de contenedores.
 */

public class ContainerManager {

    /**
     * Carga un elemento en un contenedor VBox.
     *
     * @param vBox   El contenedor VBox donde se cargará el elemento.
     * @param parent El elemento a cargar en el contenedor.
     */
    public static void loadItemOnVBox(VBox vBox, Parent parent) {
        vBox.getChildren().add(parent);
    }

    /**
     * Establece una vista en el centro de un contenedor Pane.
     *
     * @param pane   El contenedor Pane donde se establecerá la vista.
     * @param parent La vista a establecer en el centro del contenedor.
     */
    public static void setViewOnPaneCenter(Pane pane, Parent parent) {
        pane.getChildren().clear();
        configureAnchorMarginOnParent(parent);
        pane.getChildren().add(parent);
    }

    /**
     * Configura los márgenes de anclaje para un elemento en un contenedor.
     *
     * @param parent El elemento a configurar con márgenes de anclaje.
     */
    public static void configureAnchorMarginOnParent(Parent parent) {
        AnchorPane.setTopAnchor(parent, ANCHOR_PANE_TOP_ANCHOR_MARGIN_VALUE);
        AnchorPane.setBottomAnchor(parent, ANCHOR_PANE_BOTTOM_ANCHOR_MARGIN_VALUE);
        AnchorPane.setLeftAnchor(parent, ANCHOR_PANE_LEFT_ANCHOR_MARGIN_VALUE);
        AnchorPane.setRightAnchor(parent, ANCHOR_PANE_RIGHT_ANCHOR_MARGIN_VALUE);
    }

    /**
     * Limpia un contenedor, eliminando todos sus elementos.
     *
     * @param container El contenedor a limpiar.
     */
    public static void cleanContainer(Pane container) {
        container.getChildren().clear();
    }

    /**
     * Añade todos los nodos especificados a un contenedor Pane.
     *
     * @param pane  El contenedor Pane donde se añadirán los nodos.
     * @param nodes Los nodos a añadir al contenedor.
     */
    public static void addAllChildrensToPane(Pane pane, Node... nodes) {
        pane.getChildren().addAll(nodes);
    }

    /**
     * Cambia el tema de una escena actualizando sus hojas de estilo.
     *
     * @param currentScene          La escena cuyo tema se cambiará.
     * @param selectedThemeFilePath La ruta del archivo de hoja de estilo del nuevo tema seleccionado.
     */
    public static void changeSceneTheme(Scene currentScene, String selectedThemeFilePath) {
        currentScene.getStylesheets().clear();
        addStylesheets(currentScene, selectedThemeFilePath);
        loadMaterialIconsCSS(currentScene);
        loadFixedColorsCSSIfNeeded(currentScene, selectedThemeFilePath);
    }

    /**
     * Añade las hojas de estilo especificadas a una escena.
     *
     * @param currentScene  La escena a la que se añadirán las hojas de estilo.
     * @param themeFilePath La ruta del archivo de hoja de estilo a añadir.
     */
    private static void addStylesheets(Scene currentScene, String themeFilePath) {
        String designSystemTokenFilePathToExternalForm = getResourceAsExternalForm(ContainerManager.class, com.launcher.constants.CssResourceFilePath.DESIGN_SYSTEM_TOKEN_FILE_PATH);
        String themeFilePathToExternalForm = getResourceAsExternalForm(ContainerManager.class, themeFilePath);

        if (!designSystemTokenFilePathToExternalForm.isEmpty() && !themeFilePathToExternalForm.isEmpty())
            currentScene.getStylesheets().addAll(designSystemTokenFilePathToExternalForm, themeFilePathToExternalForm);
    }

    /**
     * Añade la hoja de estilo de colores fijos si el tema seleccionado lo requiere.
     *
     * @param currentScene          La escena a la que se añadirá la hoja de estilo de colores fijos si es necesario.
     * @param selectedThemeFilePath La ruta del archivo de hoja de estilo del tema seleccionado.
     */
    private static void loadFixedColorsCSSIfNeeded(Scene currentScene, String selectedThemeFilePath) {
        if (selectedThemeFilePath.equals(LA_TERCERA_LIGHT_THEME_FILE_PATH) || selectedThemeFilePath.equals(LA_TERCERA_DARK_THEME_FILE_PATH)) {
            String fixedColorsFilePathToExternalForm = getResourceAsExternalForm(ContainerManager.class, LA_TERCERA_FIXED_COLORS_FILE_PATH);
            currentScene.getStylesheets().add(fixedColorsFilePathToExternalForm);
        }
    }

    /**
     * Carga la hoja de estilo de iconos de Material en una nueva escena.
     *
     * @param newScene La nueva escena a la que se añadirá la hoja de estilo de iconos de Material.
     */
    public static void loadMaterialIconsCSS(Scene newScene) {
        String materialIconsFilePathToExternalForm = getResourceAsExternalForm(ContainerManager.class, MATERIAL_ICONS_FILE_PATH);
        if (!materialIconsFilePathToExternalForm.isEmpty())
            newScene.getStylesheets().add(materialIconsFilePathToExternalForm);
    }

    /**
     * Realiza una transición de tema utilizando una animación de desvanecimiento.
     *
     * @param snapshotView                La vista que se utilizará para la transición.
     * @param themeTransitionDurationInMs La duración de la transición en milisegundos.
     */
    public static void playThemeTransitionAnimation(ImageView snapshotView, int themeTransitionDurationInMs) {
        FadeTransition transition = new FadeTransition(Duration.millis(themeTransitionDurationInMs), snapshotView);
        transition.setFromValue(THEME_TRANSITION_STARTING_VALUE);
        transition.setToValue(THEME_TRANSITION_ENDING_VALUE);
        transition.play();
        transition.setOnFinished(_ -> ((Pane) snapshotView.getParent()).getChildren().remove(snapshotView));
    }

    /**
     * Obtiene la ventana actual asociada con un contenedor.
     *
     * @param pane El contenedor para el que se desea obtener la ventana.
     * @return La ventana asociada con el contenedor.
     */
    public static Window getCurrentWindow(Pane pane) {
        return pane.getScene().getWindow();
    }
}