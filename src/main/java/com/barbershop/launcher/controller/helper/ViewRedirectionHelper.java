package com.barbershop.launcher.controller.helper;

import com.barbershop.enums.ViewRedirection;
import com.barbershop.launcher.controller.implementation.dashboard.DashboardController;
import javafx.scene.layout.Pane;
import org.springframework.context.ApplicationContext;

import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.loadViewOnPane;

public class ViewRedirectionHelper {

    public static void redirectToView(ViewRedirection destination, Pane borderPane, ApplicationContext applicationContext) {

        switch (destination) {

            case DASHBOARD -> {

                DashboardController dashboardController = applicationContext.getBean(DashboardController.class);

                dashboardController.reloadDashboard();
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
}
