package com.launcher.controller.helper;

import com.config.preferences.AppPreferences;
import com.enums.ViewRedirection;
import com.launcher.ui.SceneManager;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.launcher.constants.view.ViewPath.*;
import static com.launcher.controller.helper.FXMLViewLoader.loadViewOnPane;

@Component
@RequiredArgsConstructor
public class ViewRedirectionHelper {

    private final AppPreferences appPreferences;

    public static void redirectToView(ViewRedirection destination, Pane borderPane, ApplicationContext applicationContext) {

        switch (destination) {

            case DASHBOARD -> {

                SceneManager sceneManager = applicationContext.getBean(SceneManager.class);

                sceneManager.switchScene(DASHBOARD_VIEW_PATH);
            }

            case CLIENTS ->
                    loadViewOnPane(CLIENTS_VIEW_PATH, applicationContext, CLIENTS_VIEW_LOADING_FAILED, borderPane);

            case CLIENT_CREATION ->
                    loadViewOnPane(CLIENT_CREATION_VIEW_PATH, applicationContext, CLIENT_CREATION_VIEW_LOADING_FAILED, borderPane);

            case EMPLOYEES ->
                    loadViewOnPane(EMPLOYEES_VIEW_PATH, applicationContext, EMPLOYEE_VIEW_LOADING_FAILED, borderPane);

            case EMPLOYEE_CREATION ->
                    loadViewOnPane(EMPLOYEE_CREATION_VIEW_PATH, applicationContext, EMPLOYEE_CREATION_VIEW_LOADING_FAILED, borderPane);

            case APPOINTMENTS ->
                    loadViewOnPane(APPOINTMENT_VIEW_PATH, applicationContext, APPOINTMENTS_VIEW_LOADING_FAILED, borderPane);

            case APPOINTMENT_CREATION ->
                    loadViewOnPane(APPOINTMENT_CREATION_VIEW_PATH, applicationContext, APPOINTMENT_CREATION_VIEW_LOADING_FAILED, borderPane);

            case BARBER_SERVICE_CREATION ->
                    loadViewOnPane(BARBER_SERVICE_CREATION_VIEW_PATH, applicationContext, BARBER_SERVICE_CREATION_VIEW_LOADING_FAILED, borderPane);

            case BARBER_SERVICE_EDITION ->
                    loadViewOnPane(BARBER_SERVICE_EDITION_VIEW_PATH, applicationContext, BARBER_SERVICE_EDITION_VIEW_LOADING_FAILED, borderPane);

            case BARBER_SERVICES ->
                    loadViewOnPane(BARBER_SERVICE_VIEW_PATH, applicationContext, BARBER_SERVICE_VIEW_LOADING_FAILED, borderPane);

            case PRODUCTS ->
                    loadViewOnPane(PRODUCT_STOCK_VIEW_PATH, applicationContext, PRODUCTS_VIEW_LOADING_FAILED, borderPane);

            case PRODUCT_CREATION ->
                    loadViewOnPane(PRODUCT_CREATION_VIEW_PATH, applicationContext, PRODUCT_CREATION_VIEW_LOADING_FAILED, borderPane);

            case PRODUCT_EDITION ->
                    loadViewOnPane(PRODUCT_EDITION_VIEW_PATH, applicationContext, PRODUCT_EDITION_VIEW_LOADING_FAILED, borderPane);

            case SETTINGS ->
                    loadViewOnPane(SETTINGS_VIEW_PATH, applicationContext, SETTINGS_VIEW_LOADING_FAILED, borderPane);

            case LOGIN -> loadViewOnPane(LOGIN_VIEW_PATH, applicationContext, LOGIN_VIEW_LOADING_FAILED, borderPane);

            case LOGOUT -> {
            }

            default -> throw new IllegalArgumentException("Vista de redirección desconocida: " + destination);
        }
    }

    public static void redirectToView(ViewRedirection destination, Pane borderPane, ApplicationContext applicationContext, Runnable dashboardCallback) {

        if (destination == ViewRedirection.DASHBOARD) {

            if (dashboardCallback != null) {

                dashboardCallback.run();
            } else {

                redirectToView(destination, borderPane, applicationContext);
            }

            return;
        }

        redirectToView(destination, borderPane, applicationContext);
    }

    public ViewRedirection determineInitialView() {

        if (appPreferences.isRememberCredentialsEnabled() && appPreferences.getCurrentUser() != null && !appPreferences.getCurrentUser().isBlank()) {

            return ViewRedirection.DASHBOARD;

        } else {

            return ViewRedirection.LOGIN;
        }
    }
}
