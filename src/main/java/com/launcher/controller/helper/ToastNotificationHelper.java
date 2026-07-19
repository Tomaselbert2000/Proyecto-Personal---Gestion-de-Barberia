package com.launcher.controller.helper;

import com.enums.ToastNotificationType;
import com.launcher.controller.implementation.toast_notification.ToastNotificationController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.springframework.context.ApplicationContext;

import static com.launcher.constants.HelperConstants.ToastNotificationHelperConstant.*;
import static com.launcher.constants.StringResource.FxmlViewLoadingErrorMessage.TOAST_NOTIFICATION_VIEW_LOADING_FAILED;
import static com.launcher.constants.ViewPath.TOAST_NOTIFICATION_VIEW_PATH;
import static com.launcher.controller.helper.FXMLViewLoader.*;

public class ToastNotificationHelper {

    /**
     * Configura una notificación de toast en un AnchorPane.
     *
     * @param anchorPane            El AnchorPane donde se añadirá la notificación de toast.
     * @param toastNotificationView La vista de la notificación de toast a añadir.
     */
    public static void setToastNotificationOnAnchorPane(AnchorPane anchorPane, Parent toastNotificationView) {
        anchorPane.getChildren().add(toastNotificationView);
        configureToastNotificationMargins(toastNotificationView);
    }

    /**
     * Configura los márgenes de una notificación de toast en un AnchorPane.
     *
     * @param toastNotificationView La vista de la notificación de toast a configurar.
     */
    private static void configureToastNotificationMargins(Parent toastNotificationView) {
        AnchorPane.setTopAnchor(toastNotificationView, TOAST_NOTIFICATION_TOP_ANCHOR_MARGIN_VALUE);
        AnchorPane.setRightAnchor(toastNotificationView, TOAST_NOTIFICATION_RIGHT_ANCHOR_MARGIN_VALUE);
    }

    /**
     * Genera una transición de pausa con el tiempo especificado.
     *
     * @param seconds El tiempo de la transición en segundos.
     * @return La transición de pausa generada.
     */
    public static PauseTransition generatePauseTransition(Duration seconds) {
        return new PauseTransition(seconds);
    }

    /**
     * Genera una instancia de Timeline con los parámetros especificados.
     *
     * @param region                             El componente que se animará.
     * @param toastProgressBarWidthValue         El valor inicial de la anchura del ProgressBar.
     * @param toastNotificationDurationInSeconds La duración de la notificación en segundos.
     * @param toastProgressBarWidthValueOnFinish El valor final de la anchura del ProgressBar.
     * @return La instancia de Timeline generada.
     */
    public static Timeline generateTimelineInstance(
            Region region,
            Integer toastProgressBarWidthValue,
            Integer toastNotificationDurationInSeconds,
            Double toastProgressBarWidthValueOnFinish
    ) {
        return new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(region.prefWidthProperty(), toastProgressBarWidthValue)),
                new KeyFrame(Duration.seconds(toastNotificationDurationInSeconds), new KeyValue(region.prefWidthProperty(), toastProgressBarWidthValueOnFinish))
        );
    }

    /**
     * Muestra una notificación de toast en un AnchorPane.
     *
     * @param anchor_pane        El AnchorPane donde se mostrará la notificación de toast.
     * @param applicationContext El contexto de la aplicación Spring para cargar el controlador.
     * @param message            El mensaje de la notificación de toast.
     * @param notificationType   El tipo de notificación de toast (INFORMATION, WARNING, ERROR, etc.).
     */
    public static void showToastNotification(AnchorPane anchor_pane, ApplicationContext applicationContext, String message, ToastNotificationType notificationType) {
        FXMLLoader loader = generateLoaderWithPath(TOAST_NOTIFICATION_VIEW_PATH);
        setControllerOnLoader(loader, applicationContext);
        Parent toastNotificationView = returnParentFromLoader(loader, TOAST_NOTIFICATION_VIEW_LOADING_FAILED);
        ToastNotificationController toastNotificationController = loader.getController();
        toastNotificationController.configureAndShowOnScreen(message, anchor_pane, toastNotificationView, notificationType);
        setToastNotificationOnAnchorPane(anchor_pane, toastNotificationView);
    }

    /**
     * Muestra un mensaje de error en una etiqueta y un contenedor de errores.
     *
     * @param exception               La excepción que generó el error.
     * @param error_message_label     La etiqueta donde se mostrará el mensaje de error.
     * @param error_message_container El contenedor donde se mostrará el mensaje de error.
     */
    public static void showExceptionErrorMessage(RuntimeException exception, Label error_message_label, VBox error_message_container) {
        String errorMessage = exception.getMessage();
        error_message_label.setText(errorMessage);
        error_message_container.setVisible(true);
        setErrorMessageTransition(error_message_container);
    }

    /**
     * Establece una transición de error en un contenedor de errores.
     *
     * @param generalErrorContainer El contenedor de errores donde se establecerá la transición.
     */
    public static void setErrorMessageTransition(VBox generalErrorContainer) {
        PauseTransition pauseTransition = generatePauseTransition(Duration.seconds(TOAST_NOTIFICATION_DURATION_IN_SECONDS));
        pauseTransition.setOnFinished(_ -> generalErrorContainer.setVisible(false));
        pauseTransition.play();
    }
}