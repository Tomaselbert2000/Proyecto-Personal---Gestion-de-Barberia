package com.barbershop.launcher.controller.dashboard;

import com.barbershop.dto.dashboard.RecentActivityDTO;
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

import static com.barbershop.launcher.constants.ui.messages.EmptyListMessage.EMPTY_ACTIVITY_LOG_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.helper.ContainerManager.cleanVBox;
import static com.barbershop.launcher.controller.helper.ContainerManager.loadItemOnVBox;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.*;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.setTextOnLabel;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.showEmptyListLabel;

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
    private Button navbar_settings_button;

    @FXML
    private Button navbar_logout_button;

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

    @FXML
    public Button dashboard_view_register_client_button;

    private final ApplicationContext applicationContext;

    @FXML
    public void initialize() {

        dashboardReference = borderPane.getCenter();

        loadStatistics();
    }

    @FXML
    public void showDashboardView() {

        borderPane.setCenter(dashboardReference);

        loadStatistics();
    }

    @FXML
    public void showEmployeeView() {

        loadViewOnPane(EMPLOYEES_VIEW_PATH, applicationContext, EMPLOYEE_VIEW_LOADING_FAILED, borderPane);
    }

    @FXML
    public void showAppointmentView() {

        loadViewOnPane(APPOINTMENT_VIEW_PATH, applicationContext, APPOINTMENS_VIEW_LIST_LOADING_FAILED, borderPane);
    }

    @FXML
    public void showClientView() {

        loadViewOnPane(CLIENTS_VIEW_PATH, applicationContext, CLIENT_CREATION_VIEW_LOADING_FAILED, borderPane);
    }

    @FXML
    public void showBarberServiceView() {

        loadViewOnPane(BARBER_SERVICE_VIEW_PATH, applicationContext, BARBER_SERVICE_VIEW_LOADING_FAILED, borderPane);
    }

    @FXML
    public void showProductView() {

        loadViewOnPane(PRODUCT_STOCK_VIEW_PATH, applicationContext, PRODUCTS_VIEW_LOADING_FAILED, borderPane);
    }

    @FXML
    public void showSettingsView() {

        loadViewOnPane(SETTINGS_VIEW_PATH, applicationContext, SETTINGS_VIEW_LOADING_FAILED, borderPane);
    }

    @FXML
    public void showLogoutView() {

        //TODO: completar esto más adelante
    }

    @FXML
    public void showRegisterNewClientView() {

        loadViewOnPane(CLIENT_CREATION_VIEW_PATH, applicationContext, CLIENT_CREATION_VIEW_LOADING_FAILED, borderPane);
    }

    @FXML
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
