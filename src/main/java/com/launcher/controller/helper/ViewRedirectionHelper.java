package com.launcher.controller.helper;

import com.config.preferences.AppPreferences;
import com.enums.ViewRedirection;
import com.launcher.ui.SceneManager;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.launcher.constants.StringResource.FxmlViewLoadingErrorMessage.*;
import static com.launcher.constants.ViewPath.*;
import static com.launcher.controller.helper.FXMLViewLoader.loadViewOnPane;

@Component
@RequiredArgsConstructor
public class ViewRedirectionHelper {

    private final AppPreferences appPreferences;

    /**
     * Redirige al usuario a la vista especificada.
     *
     * @param destination        La vista a la que se desea redirigir el usuario.
     * @param borderPane         El Pane donde se cargará la vista.
     * @param applicationContext El contexto de la aplicación Spring.
     */
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
            case REGISTER ->
                    loadViewOnPane(REGISTER_VIEW_PATH, applicationContext, REGISTER_VIEW_LOADING_FAILED, borderPane);
            default -> throw new IllegalArgumentException("Vista de redirección desconocida: " + destination);
        }
    }

    /**
     * Redirige al usuario a la vista especificada con una acción adicional en el caso de la vista del panel.
     *
     * @param destination        La vista a la que se desea redirigir el usuario.
     * @param borderPane         El Pane donde se cargará la vista.
     * @param applicationContext El contexto de la aplicación Spring.
     * @param dashboardCallback  La acción adicional a ejecutar cuando se redirija a la vista del panel.
     */
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

    /**
     * Determina la vista inicial basándose en las preferencias del usuario.
     *
     * @return La vista inicial a mostrar.
     */
    public ViewRedirection determineInitialView() {
        if (appPreferences.isRememberCredentialsEnabled() && appPreferences.getCurrentUser() != null && !appPreferences.getCurrentUser().isBlank()) {
            return ViewRedirection.DASHBOARD;
        } else {
            return ViewRedirection.LOGIN;
        }
    }
}