package com.barbershop.launcher.controller.dashboard;

import com.barbershop.dto.dashboard.RecentActivityDTO;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.launcher.controller.helper.ViewRedirectionHelper;
import com.barbershop.service.interfaces.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.messages.EmptyListMessage.EMPTY_ACTIVITY_LOG_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.helper.ContainerManager.cleanVBox;
import static com.barbershop.launcher.controller.helper.ContainerManager.loadItemOnVBox;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.*;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final AppointmentService appointmentService;
    private final ProductService productService;

    @FXML
    private Node dashboardReference;

    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox activity_log_vbox;

    @FXML
    private Button navbar_dashboard_button;

    @FXML
    private Button navbar_client_button;

    @FXML
    private Button navbar_employee_button;

    @FXML
    private Button navbar_appointment_button;

    @FXML
    private Button navbar_product_button;

    @FXML
    private Button navbar_barber_service_button;

    @FXML
    private Button navbar_settings_button;

    @FXML
    private Button navbar_logout_button;

    @FXML
    private Button clients_view_button;

    @FXML
    private Button create_client_button;

    @FXML
    private Button employee_view_button;

    @FXML
    private Button create_employee_button;

    @FXML
    private Button appointments_view_button;

    @FXML
    private Button create_appointment_button;

    @FXML
    private Button products_view_button;

    @FXML
    private Button create_product_button;

    @FXML
    private Label clients_registered_count;

    @FXML
    private Label clients_registered_percentage_vs_last_month;

    @FXML
    private Label active_employees_count;

    @FXML
    private Label active_employees_this_month_count;

    @FXML
    private Label appointments_today_count;

    @FXML
    private Label finished_appointments_today_count;

    @FXML
    private Label products_on_stock_count;

    @FXML
    private Label low_stock_products_count;

    private final ApplicationContext applicationContext;

    @FXML
    public void initialize() {

        dashboardReference = borderPane.getCenter();

        loadStatistics();

        configureButtonActions();
    }

    private void configureButtonActions() {

        Map<Button, Runnable> navBarButtonsMap = Map.of(
                navbar_dashboard_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.DASHBOARD, borderPane, applicationContext),
                navbar_client_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.CLIENTS, borderPane, applicationContext),
                navbar_employee_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.EMPLOYEES, borderPane, applicationContext),
                navbar_appointment_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.APPOINTMENTS, borderPane, applicationContext),
                navbar_barber_service_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.BARBER_SERVICES, borderPane, applicationContext),
                navbar_product_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.PRODUCTS, borderPane, applicationContext),
                navbar_settings_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.SETTINGS, borderPane, applicationContext),
                navbar_logout_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.LOGOUT, borderPane, applicationContext)
        );

        Map<Button, Runnable> quickAccessButtonsMap = Map.of(
                clients_view_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.CLIENTS, borderPane, applicationContext),
                employee_view_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.EMPLOYEES, borderPane, applicationContext),
                appointments_view_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.APPOINTMENTS, borderPane, applicationContext),
                products_view_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.PRODUCTS, borderPane, applicationContext)
        );

        Map<Button, Runnable> quickCreationButtonsMap = Map.of(
                create_client_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.CLIENT_CREATION, borderPane, applicationContext),
                create_employee_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.EMPLOYEE_CREATION, borderPane, applicationContext),
                create_appointment_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.APPOINTMENT_CREATION, borderPane, applicationContext),
                create_product_button, () -> ViewRedirectionHelper.redirectToView(ViewRedirection.PRODUCT_CREATION, borderPane, applicationContext)
        );

        configureRunnableMaps(navBarButtonsMap, quickAccessButtonsMap, quickCreationButtonsMap);
    }

    private void loadEventLog() {

        List<RecentActivityDTO> recentActivity = dashboardService.getRecentActivityLog();

        loadRecentActivitiesOnDashboard(recentActivity);
    }

    private void loadStatistics() {

        loadDashboardStats();

        loadEventLog();
    }

    private void loadRecentActivitiesOnDashboard(List<RecentActivityDTO> recentActivity) {

        cleanVBox(activity_log_vbox);

        if (recentActivity.isEmpty()) {

            showEmptyListLabel(EMPTY_ACTIVITY_LOG_MESSAGE, activity_log_vbox);

        } else {

            for (RecentActivityDTO activityDTO : recentActivity) {

                FXMLLoader loader = generateLoaderWithPath(ACTIVITY_LOG_ITEM_VIEW_PATH);

                Parent activityLog = returnParentFromLoader(loader, RECENT_ACTIVITY_VIEW_LOADING_FAILED);

                ActivityItemController activityItemController = loader.getController();

                activityItemController.setDataOnItem(activityDTO);

                loadItemOnVBox(activity_log_vbox, activityLog);
            }
        }
    }

    private void loadDashboardStats() {

        loadClientsStats();

        loadEmployeesStats();

        loadAppointmentsStats();

        loadProductsStats();
    }

    private void loadClientsStats() {

        Long clientsRegistered = clientService.getClientsRegisteredQuantity();

        setTextOnLabel(clients_registered_count, clientsRegistered.toString());

        Long clientsRegisteredPercentageVsPreviousMonth = clientService.calculatePercentageOfClientsVsLastMonth();

        setTextOnLabel(clients_registered_percentage_vs_last_month, clientsRegisteredPercentageVsPreviousMonth.toString() + "%");
    }

    private void loadEmployeesStats() {

        Long activeEmployees = employeeService.getActiveEmployees();

        setTextOnLabel(active_employees_count, activeEmployees.toString());

        Long activeEmployeesThisMonthVsLastMonth = employeeService.calculateActiveEmployeesVsLastMonth();

        setTextOnLabel(active_employees_this_month_count, activeEmployeesThisMonthVsLastMonth.toString());
    }

    private void loadAppointmentsStats() {

        Long appointmentsTodayCount = appointmentService.appointmentsToday();

        setTextOnLabel(appointments_today_count, appointmentsTodayCount.toString());

        Long finishedAppointmentsToday = appointmentService.completedAppointmentsToday();

        setTextOnLabel(finished_appointments_today_count, finishedAppointmentsToday.toString());
    }

    private void loadProductsStats() {

        Long productsOnStockCount = productService.getProductsRegisteredCount();

        setTextOnLabel(products_on_stock_count, productsOnStockCount.toString());

        Long lowStockProductCount = productService.getProductsOnLowStock();

        setTextOnLabel(low_stock_products_count, lowStockProductCount.toString());
    }
}
