package com.launcher.ui;

import com.enums.ViewRedirection;
import com.launcher.controller.helper.ViewRedirectionHelper;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.launcher.constants.ui.messages.GenericStrings.APP_TITLE;
import static com.launcher.constants.view.ViewPath.DASHBOARD_VIEW_PATH;
import static com.launcher.constants.view.ViewPath.LOGIN_VIEW_PATH;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final SceneManager sceneManager;
    private final ViewRedirectionHelper viewRedirectionHelper;

    public StageInitializer(SceneManager sceneManager, ViewRedirectionHelper viewRedirectionHelper) {

        this.sceneManager = sceneManager;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {

        Stage stage = event.getStage();

        stage.setTitle(APP_TITLE);

        sceneManager.setPrimaryStage(stage);

        ViewRedirection viewToShow = viewRedirectionHelper.determineInitialView();

        switch (viewToShow) {

            case LOGIN -> sceneManager.switchScene(LOGIN_VIEW_PATH);

            case DASHBOARD -> sceneManager.switchScene(DASHBOARD_VIEW_PATH);
        }
    }

    @Override
    public boolean supportsAsyncExecution() {

        return ApplicationListener.super.supportsAsyncExecution();
    }
}
