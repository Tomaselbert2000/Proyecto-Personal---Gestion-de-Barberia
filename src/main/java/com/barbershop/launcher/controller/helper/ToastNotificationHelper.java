package com.barbershop.launcher.controller.helper;

import com.barbershop.enums.ToastNotificationType;
import com.barbershop.launcher.controller.implementation.toast_notification.ToastNotificationController;
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

import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.TOAST_NOTIFICATION_VIEW_LOADING_FAILED;
import static com.barbershop.launcher.constants.view.ViewPath.TOAST_NOTIFICATION_VIEW_PATH;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.*;
import static com.barbershop.launcher.controller.helper.HelperConstants.*;

public class ToastNotificationHelper {

    public static void setToastNotificationOnAnchorPane(AnchorPane anchorPane, Parent toastNotificationView) {

        anchorPane.getChildren().add(toastNotificationView);
        configureToastNotificationMargins(toastNotificationView);
    }

    private static void configureToastNotificationMargins(Parent toastNotificationView) {

        AnchorPane.setTopAnchor(toastNotificationView, TOAST_NOTIFICATION_TOP_ANCHOR_MARGIN_VALUE);
        AnchorPane.setRightAnchor(toastNotificationView, TOAST_NOTIFICATION_RIGHT_ANCHOR_MARGIN_VALUE);
    }

    public static PauseTransition generatePauseTransition(Duration seconds) {

        return new PauseTransition(seconds);
    }

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

    public static void showToastNotification(AnchorPane anchor_pane, ApplicationContext applicationContext, String message, ToastNotificationType notificationType) {

        FXMLLoader loader = generateLoaderWithPath(TOAST_NOTIFICATION_VIEW_PATH);

        setControllerOnLoader(loader, applicationContext);

        Parent toastNotificationView = returnParentFromLoader(loader, TOAST_NOTIFICATION_VIEW_LOADING_FAILED);

        ToastNotificationController toastNotificationController = loader.getController();

        toastNotificationController.configureAndShowOnScreen(message, anchor_pane, toastNotificationView, notificationType);

        setToastNotificationOnAnchorPane(anchor_pane, toastNotificationView);
    }

    public static void showExceptionErrorMessage(RuntimeException exception, Label error_message_label, VBox error_message_container) {

        String errorMessage = exception.getMessage();

        error_message_label.setText(errorMessage);

        error_message_container.setVisible(true);

        setErrorMessageTransition(error_message_container);
    }

    public static void setErrorMessageTransition(VBox generalErrorContainer) {

        PauseTransition pauseTransition = generatePauseTransition(Duration.seconds(ERROR_MESSAGE_DURATION_IN_SECONDS));
        pauseTransition.setOnFinished(_ -> generalErrorContainer.setVisible(false));
        pauseTransition.play();
    }
}
