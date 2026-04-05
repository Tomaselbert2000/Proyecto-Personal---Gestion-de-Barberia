package com.barbershop.launcher.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SceneManager {

    private static final String PATH_ERROR_MESSAGE = "Hubo un error al cargar la siguiente vista: ";

    @Setter
    private Stage primaryStage;
    private final ApplicationContext applicationContext;

    public SceneManager(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    public void switchScene(String fxmlPath) {

        try {
            FXMLLoader loader = generateLoaderWithPath(fxmlPath);

            setController(loader);

            Parent root = loader.load();

            Scene newScene = new Scene(root);

            setSceneOnStage(newScene);

            primaryStage.show();

        } catch (IOException exception) {

            throw new RuntimeException(PATH_ERROR_MESSAGE + fxmlPath, exception);
        }
    }

    private FXMLLoader generateLoaderWithPath(String fxmlPath) {

        return new FXMLLoader(getClass().getResource(fxmlPath));
    }

    private void setController(FXMLLoader loader) {

        loader.setControllerFactory(applicationContext::getBean);
    }

    private void setSceneOnStage(Scene newScene) {

        primaryStage.setScene(newScene);
    }
}
