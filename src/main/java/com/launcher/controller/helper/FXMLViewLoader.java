package com.launcher.controller.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

import static com.launcher.animation.AnimationEngine.slideAndFadeNodeIn;
import static com.launcher.animation.AnimationEngine.slideAndFadeNodeOut;
import static com.launcher.controller.helper.ContainerManager.setViewOnPaneCenter;

public class FXMLViewLoader {

    /**
     * Genera un FXMLLoader con la ruta especificada.
     *
     * @param path La ruta del archivo FXML a cargar.
     * @return Un FXMLLoader configurado con la ruta especificada.
     */
    public static FXMLLoader generateLoaderWithPath(String path) {
        return new FXMLLoader(ValidationFormatter.class.getResource(path));
    }

    /**
     * Establece el controlador para el FXMLLoader utilizando el ApplicationContext proporcionado.
     *
     * @param loader             El FXMLLoader al que se establecerá el controlador.
     * @param applicationContext El contexto de la aplicación Spring que se utilizará para obtener el controlador.
     */
    public static void setControllerOnLoader(FXMLLoader loader, ApplicationContext applicationContext) {
        loader.setControllerFactory(applicationContext::getBean);
    }

    /**
     * Carga un archivo FXML y devuelve su representación como un Parent.
     *
     * @param loader               El FXMLLoader que se utilizará para cargar el archivo FXML.
     * @param optionalErrorMessage Un mensaje de error opcional que se mostrará si ocurre una excepción.
     * @return El Parent cargado desde el archivo FXML.
     */
    public static Parent returnParentFromLoader(FXMLLoader loader, String optionalErrorMessage) {
        try {
            return loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(optionalErrorMessage, exception);
        }
    }

    /**
     * Carga una vista en un Pane utilizando el archivo FXML especificado.
     *
     * @param viewPath             La ruta del archivo FXML que contiene la vista a cargar.
     * @param applicationContext   El contexto de la aplicación Spring que se utilizará para obtener el controlador.
     * @param optionalErrorMessage Un mensaje de error opcional que se mostrará si ocurre una excepción.
     * @param pane                 El Pane en el que se cargará la vista.
     */
    public static void loadViewOnPane(
            String viewPath,
            ApplicationContext applicationContext,
            String optionalErrorMessage,
            Pane pane
    ) {
        FXMLLoader loader = generateLoaderWithPath(viewPath);
        setControllerOnLoader(loader, applicationContext);
        Parent newView = returnParentFromLoader(loader, optionalErrorMessage);

        animateViewChange(newView, pane);
    }

    /**
     * Realiza una transición animada entre dos vistas en un Pane.
     *
     * @param newView La nueva vista que se cargará.
     * @param pane    El Pane en el que se realizará la transición.
     */
    public static void animateViewChange(Node newView, Pane pane) {
        if (pane instanceof BorderPane borderPane) {
            Node currentCenter = borderPane.getCenter();
            StackPane animationContainer;

            if (currentCenter instanceof StackPane) {
                animationContainer = (StackPane) currentCenter;
            } else {
                animationContainer = new StackPane();
                if (currentCenter != null) {
                    animationContainer.getChildren().add(currentCenter);
                }
                borderPane.setCenter(animationContainer);
            }

            Node oldView = animationContainer.getChildren().isEmpty() ? null : animationContainer.getChildren().getLast();

            if (oldView != null) {
                animationContainer.getChildren().removeIf(node -> node != oldView);
            } else {
                animationContainer.getChildren().clear();
            }

            animationContainer.getChildren().add(newView);

            if (oldView != null) slideAndFadeNodeOut(oldView, 0.0);
            if (newView != null) slideAndFadeNodeIn(newView, 0.0);
        } else {
            setViewOnPaneCenter(pane, (Parent) newView);
        }
    }
}