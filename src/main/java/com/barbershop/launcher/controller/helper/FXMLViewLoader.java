package com.barbershop.launcher.controller.helper;

import com.barbershop.enums.ViewRedirection;
import com.barbershop.launcher.controller.dashboard.DashboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

import static com.barbershop.launcher.controller.helper.ContainerManager.setViewOnAnchorPaneCenter;
import static com.barbershop.launcher.controller.helper.ContainerManager.setViewOnBorderPaneCenter;

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

        Parent parent = returnParentFromLoader(loader, optionalErrorMessage);

        if (pane instanceof BorderPane) {

            setViewOnBorderPaneCenter((BorderPane) pane, parent);

        } else {

            setViewOnAnchorPaneCenter((AnchorPane) pane, parent);
        }
    }

    public static void redirectToView(ApplicationContext applicationContext, ViewRedirection viewRedirection) {

        DashboardController dashboardController = applicationContext.getBean(DashboardController.class);

        switch (viewRedirection) {

            case DASHBOARD -> dashboardController.showDashboardView();
            case CLIENTS -> dashboardController.showClientView();
            case CLIENT_CREATION -> dashboardController.showRegisterNewClientView();
            case EMPLOYEES -> dashboardController.showEmployeeView();
            case APPOINTMENTS -> dashboardController.showAppointmentView();
            case BARBER_SERVICES -> dashboardController.showBarberServiceView();
            case PRODUCTS -> dashboardController.showProductView();
            case SETTINGS -> dashboardController.showSettingsView();
            case LOGOUT -> dashboardController.showLogoutView();
        }
    }
}
