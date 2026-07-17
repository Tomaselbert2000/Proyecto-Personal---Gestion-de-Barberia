package com.launcher.ui;

import com.config.preferences.AppPreferences;
import com.enums.Theme;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.launcher.controller.helper.ContainerManager.changeSceneTheme;

@Component
public class SceneManager {

    private static final String PATH_ERROR_MESSAGE = "Hubo un error al cargar la siguiente vista: ";
    private final AppPreferences appPreferences;
    private final ApplicationContext applicationContext;
    @Setter
    private Stage primaryStage;

    public SceneManager(ApplicationContext applicationContext, AppPreferences appPreferences) {

        this.applicationContext = applicationContext;
        this.appPreferences = appPreferences;
    }

    public void switchScene(String fxmlPath) {

        try {
            FXMLLoader loader = generateLoaderWithPath(fxmlPath);

            setController(loader);

            Parent root = loader.load();

            Scene newScene = new Scene(root);

            String currentThemePreference = appPreferences.getTheme();

            Theme currentThemeEnum = Theme.valueOf(currentThemePreference);

            changeSceneTheme(newScene, currentThemeEnum.getThemeFilePath());

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
