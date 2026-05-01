package com.barbershop.launcher.controller.helper;

import com.barbershop.launcher.controller.implementation.dialog.DeleteConfirmationDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.DELETE_CONFIRMATION_DIALOG_VIEW_LOADING_FAILED;
import static com.barbershop.launcher.constants.view.ViewPath.DELETE_CONFIRMATION_VIEW_PATH;
import static com.barbershop.launcher.controller.helper.ContainerManager.configureAnchorMarginOnParent;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.*;
import static com.barbershop.launcher.controller.helper.HelperConstants.GAUSSIAN_BLUR_VALUE;

public class DialogHelper {

    public static void showConfirmationDialog(AnchorPane anchorPane, ApplicationContext applicationContext, String title, String message, Runnable onConfirm, Runnable onCancel) {

        List<Node> nodesToBlur = anchorPane.getChildren();

        setGaussianBlurOnNodes(nodesToBlur);

        FXMLLoader loader = generateLoaderWithPath(DELETE_CONFIRMATION_VIEW_PATH);

        setControllerOnLoader(loader, applicationContext);

        Parent dialogView = returnParentFromLoader(loader, DELETE_CONFIRMATION_DIALOG_VIEW_LOADING_FAILED);

        DeleteConfirmationDialogController deleteConfirmationDialogController = loader.getController();

        Runnable onCloseDialog = () -> {

            removeBlur(nodesToBlur);
            removeNodeFromAnchorPane(anchorPane, dialogView);
        };

        deleteConfirmationDialogController.configureDialog(title, message, onConfirm, onCancel, onCloseDialog);

        configureAnchorMarginOnParent(dialogView);

        anchorPane.getChildren().add(dialogView);
    }

    private static void setGaussianBlurOnNodes(List<Node> nodesToBlur) {

        for (Node node : nodesToBlur) {

            node.setEffect(new GaussianBlur(GAUSSIAN_BLUR_VALUE));
        }
    }

    private static void removeBlur(List<Node> nodesToUnblur) {

        for (Node node : nodesToUnblur) {

            node.setEffect(null);
        }
    }

    private static void removeNodeFromAnchorPane(AnchorPane anchorPane, Parent view) {

        anchorPane.getChildren().remove(view);
    }
}
