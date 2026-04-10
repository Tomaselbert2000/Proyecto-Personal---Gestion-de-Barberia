package com.barbershop.launcher.controller.toast_notification;

import com.barbershop.enums.ToastNotificationType;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.css_class.CssStylesStrings.*;
import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.TOAST_NOTIFICATION_TITLE_FAILED;
import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.TOAST_NOTIFICATION_TITLE_SUCCESSFUL;
import static com.barbershop.launcher.controller.UI_RenderingFunctions.*;

@Component
@RequiredArgsConstructor
public class ToastNotificationController {

    private static final Integer TOAST_PROGRESS_BAR_WIDTH_VALUE_ON_START = 400;
    private static final Double TOAST_PROGRESS_BAR_WIDTH_VALUE_ON_FINISH = 0.0;
    private static final Integer TOAST_NOTIFICATION_DURATION_IN_SECONDS = 5;

    @FXML
    private Region toast_notification_region_icon;

    @FXML
    private Circle toast_notification_icon_background_circle;

    @FXML
    private Label toast_title;

    @FXML
    private Label toast_message;

    @FXML
    private Button toast_close_button;

    @FXML
    private VBox toast_progress_container;

    @FXML
    private Region toast_progress_bar;

    public void configureAndShowOnScreen(String message, AnchorPane anchorPane, Node node, ToastNotificationType notificationType) {

        Timeline timeline = generateTimelineInstance(
                toast_progress_bar,
                TOAST_PROGRESS_BAR_WIDTH_VALUE_ON_START,
                TOAST_NOTIFICATION_DURATION_IN_SECONDS,
                TOAST_PROGRESS_BAR_WIDTH_VALUE_ON_FINISH
        );

        List<Node> nodes = List.of(toast_notification_region_icon, toast_notification_icon_background_circle, toast_progress_bar);
        List<String> stylesForSuccessfulOperation = List.of(TOAST_NOTIFICATION_SUCCESSFUL_ICON, TOAST_NOTIFICATION_SUCCESFUL_CIRCLE, TOAST_NOTIFICATION_PROGRESS_BAR_SUCCESSFUL);
        List<String> stylesForFailedOperation = List.of(TOAST_NOTIFICATION_FAILED_ICON, TOAST_NOTIFICATION_FAILED_CIRCLE, TOAST_NOTIFICATION_PROGRESS_BAR_FAILED);

        switch (notificationType){

            case SUCCESSFUL -> {

                setTextOnLabel(toast_title, TOAST_NOTIFICATION_TITLE_SUCCESSFUL);

                Map<Node, String> map = generateMap(nodes, stylesForSuccessfulOperation);

                setStylesOnNodeMap(map);
            }

            case FAILED -> {

                setTextOnLabel(toast_title, TOAST_NOTIFICATION_TITLE_FAILED);

                Map<Node, String> map = generateMap(nodes, stylesForFailedOperation);

                setStylesOnNodeMap(map);
            }
        }

        setTextOnLabel(toast_message, message);

        configureToastCloseButton(anchorPane, node, timeline);
        toast_progress_container.setVisible(true);
        timeline.setOnFinished(_ -> anchorPane.getChildren().remove(node));
        timeline.play();
    }

    private void configureToastCloseButton(AnchorPane anchorPane, Node node, Timeline timeline) {

        toast_close_button.setOnAction(_ -> {

                    anchorPane.getChildren().remove(node);
                    timeline.stop();
                }
        );
    }
}
