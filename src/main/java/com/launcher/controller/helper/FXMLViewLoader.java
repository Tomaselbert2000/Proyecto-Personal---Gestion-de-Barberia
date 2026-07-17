package com.launcher.controller.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

import static com.launcher.animation.AnimationEngine.slideAndFadeNodeIn;
import static com.launcher.animation.AnimationEngine.slideAndFadeNodeOut;
import static com.launcher.controller.helper.ContainerManager.setViewOnAnchorPaneCenter;

public class FXMLViewLoader {

    public static FXMLLoader generateLoaderWithPath(String path) {

        return new FXMLLoader(ValidationFormatter.class.getResource(path));
    }

    public static void setControllerOnLoader(FXMLLoader loader, ApplicationContext applicationContext) {

        loader.setControllerFactory(applicationContext::getBean);
    }

    public static Parent returnParentFromLoader(FXMLLoader loader, String optionalErrorMessage) {

        try {
            return loader.load();

        } catch (IOException exception) {

            throw new RuntimeException(optionalErrorMessage, exception);
        }
    }

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

            setViewOnAnchorPaneCenter((AnchorPane) pane, (Parent) newView);
        }
    }
}
