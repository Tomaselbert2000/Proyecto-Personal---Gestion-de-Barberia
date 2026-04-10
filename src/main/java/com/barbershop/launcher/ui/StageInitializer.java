package com.barbershop.launcher.ui;

import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.barbershop.launcher.constants.ui.messages.GenericStrings.APP_TITLE;
import static com.barbershop.launcher.constants.view.ViewPath.DASHBOARD_VIEW_PATH;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final SceneManager sceneManager;

    public StageInitializer(SceneManager sceneManager) {

        this.sceneManager = sceneManager;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {

        Stage stage = event.getStage();

        stage.setTitle(APP_TITLE);

        sceneManager.setPrimaryStage(stage);

        sceneManager.switchScene(DASHBOARD_VIEW_PATH);
    }

    @Override
    public boolean supportsAsyncExecution() {

        return ApplicationListener.super.supportsAsyncExecution();
    }
}
