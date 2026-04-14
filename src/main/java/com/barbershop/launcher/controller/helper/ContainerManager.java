package com.barbershop.launcher.controller.helper;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import static com.barbershop.launcher.controller.helper.HelperConstants.*;
import static com.barbershop.launcher.controller.helper.HelperConstants.ANCHOR_PANE_RIGHT_ANCHOR_MARGIN_VALUE;

public class ContainerManager {

    public static void loadItemOnVBox(VBox vBox, Parent parent) {

        vBox.getChildren().add(parent);
    }

    public static void setViewOnBorderPaneCenter(BorderPane borderPane, Parent view) {

        borderPane.setCenter(view);
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

    public static void cleanVBox(VBox vBox) {

        vBox.getChildren().clear();
    }
}
