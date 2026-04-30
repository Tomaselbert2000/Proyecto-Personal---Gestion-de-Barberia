package com.barbershop.launcher.controller.helper;

import com.barbershop.enums.ViewRedirection;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.loadViewOnPane;

@Component
@RequiredArgsConstructor
public class ViewRedirectionHelper {

    public static void redirectToView(ViewRedirection destination, Pane borderPane, ApplicationContext applicationContext) {

        switch (destination) {

            case DASHBOARD -> {
                borderPane.getChildren().clear();
                loadViewOnPane(DASHBOARD_VIEW_PATH, applicationContext, DASHBOARD_VIEW_LOADING_FAILED, borderPane);
            }

            case CLIENTS ->
                    loadViewOnPane(CLIENTS_VIEW_PATH, applicationContext, CLIENT_CREATION_VIEW_LOADING_FAILED, borderPane);

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

            case BARBER_SERVICES ->
                    loadViewOnPane(BARBER_SERVICE_VIEW_PATH, applicationContext, BARBER_SERVICE_VIEW_LOADING_FAILED, borderPane);

            case PRODUCTS ->
                    loadViewOnPane(PRODUCT_STOCK_VIEW_PATH, applicationContext, PRODUCTS_VIEW_LOADING_FAILED, borderPane);

            case PRODUCT_CREATION ->
                    loadViewOnPane(PRODUCT_CREATION_VIEW_PATH, applicationContext, PRODUCT_CREATION_VIEW_LOADING_FAILED, borderPane);

            case SETTINGS ->
                    loadViewOnPane(SETTINGS_VIEW_PATH, applicationContext, SETTINGS_VIEW_LOADING_FAILED, borderPane);

            case LOGOUT -> { //TODO: completar cuando esté lista la vista de logout}
            }
        }
    }
}
