package com.launcher.controller.helper;

import com.launcher.controller.implementation.dialog.ConfirmationDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static com.launcher.constants.HelperConstants.DialogHelperConstants.GAUSSIAN_BLUR_VALUE;
import static com.launcher.constants.StringResource.FxmlViewLoadingErrorMessage.CONFIRMATION_DIALOG_VIEW_LOADING_FAILED;
import static com.launcher.constants.ViewPath.DELETE_CONFIRMATION_VIEW_PATH;
import static com.launcher.controller.helper.ContainerManager.configureAnchorMarginOnParent;
import static com.launcher.controller.helper.FXMLViewLoader.*;

public class DialogHelper {

    /**
     * Muestra un diálogo de confirmación.
     *
     * @param anchorPane         El contenedor donde se mostrará el diálogo.
     * @param applicationContext El contexto de la aplicación Spring.
     * @param title              El título del diálogo.
     * @param message            El mensaje a mostrar en el diálogo.
     * @param cancelButtonText   El texto del botón de cancelación.
     * @param confirmButtonText  El texto del botón de confirmación.
     * @param iconStyleclass     La clase de estilo para el ícono del diálogo.
     * @param onConfirm          La acción a ejecutar cuando se confirme el diálogo.
     * @param onCancel           La acción a ejecutar cuando se cancele el diálogo.
     */
    public static void showConfirmationDialog(
            Pane anchorPane,
            ApplicationContext applicationContext,
            String title,
            String message,
            String cancelButtonText,
            String confirmButtonText,
            String iconStyleclass,
            Runnable onConfirm,
            Runnable onCancel) {

        List<Node> nodesToBlur = anchorPane.getChildren();
        setGaussianBlurOnNodes(nodesToBlur);

        FXMLLoader loader = generateLoaderWithPath(DELETE_CONFIRMATION_VIEW_PATH);
        setControllerOnLoader(loader, applicationContext);
        Parent dialogView = returnParentFromLoader(loader, CONFIRMATION_DIALOG_VIEW_LOADING_FAILED);

        ConfirmationDialogController controller = loader.getController();
        Runnable onCloseDialog = () -> {
            removeBlur(nodesToBlur);
            removeNodeFromAnchorPane(dialogView);
        };
        controller.configureDialog(title, message, cancelButtonText, confirmButtonText, iconStyleclass, onConfirm, onCancel, onCloseDialog);
        configureAnchorMarginOnParent(dialogView);

        if (anchorPane.getParent() instanceof Pane parentPane) parentPane.getChildren().add(dialogView);
        else anchorPane.getChildren().add(dialogView);
    }

    /**
     * Aplica un efecto de desenfoque a una lista de nodos.
     *
     * @param nodesToBlur La lista de nodos a los que se aplicará el efecto de desenfoque.
     */
    private static void setGaussianBlurOnNodes(List<Node> nodesToBlur) {
        for (Node node : nodesToBlur) node.setEffect(new GaussianBlur(GAUSSIAN_BLUR_VALUE));
    }

    /**
     * Elimina el efecto de desenfoque de una lista de nodos.
     *
     * @param nodesToUnblur La lista de nodos a los que se eliminará el efecto de desenfoque.
     */
    private static void removeBlur(List<Node> nodesToUnblur) {
        for (Node node : nodesToUnblur) node.setEffect(null);
    }

    /**
     * Elimina un nodo de un contenedor padre.
     *
     * @param view El nodo a eliminar.
     */
    private static void removeNodeFromAnchorPane(Parent view) {
        if (view.getParent() instanceof Pane parentPane) parentPane.getChildren().remove(view);
    }
}