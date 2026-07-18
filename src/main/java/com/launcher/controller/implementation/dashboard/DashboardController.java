package com.launcher.controller.implementation.dashboard;

import com.dto.dashboard.RecentActivityDTO;
import com.enums.ViewRedirection;
import com.launcher.controller.interfaces.Controller;
import com.service.interfaces.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.launcher.animation.AnimationEngine.fadeNodeIn;
import static com.launcher.animation.AnimationEngineConstant.ANIMATION_DELAY_IN_MS;
import static com.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.launcher.constants.ui.messages.EmptyListMessage.EMPTY_ACTIVITY_LOG_MESSAGE;
import static com.launcher.constants.ui.messages.ViewLoadingErrorMessage.RECENT_ACTIVITY_VIEW_LOADING_FAILED;
import static com.launcher.constants.view.ViewPath.ACTIVITY_LOG_ITEM_VIEW_PATH;
import static com.launcher.controller.helper.ContainerManager.cleanContainer;
import static com.launcher.controller.helper.ContainerManager.loadItemOnVBox;
import static com.launcher.controller.helper.FXMLViewLoader.*;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.formatAsPercentage;
import static com.launcher.controller.helper.ValidationFormatter.parseNumberValueToText;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class DashboardController implements Controller {

    private final DashboardService dashboardService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final AppointmentService appointmentService;
    private final ProductService productService;

    private final ApplicationContext applicationContext;

    @FXML
    private Node dashboardReference;

    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox activity_log_vbox;

    @FXML
    private MFXButton
            navbar_dashboard_button,
            navbar_client_button,
            navbar_employee_button,
            navbar_appointment_button,
            navbar_product_button,
            navbar_barber_service_button,
            navbar_settings_button,
            navbar_logout_button,
            clients_view_button,
            create_client_button,
            employee_view_button,
            create_employee_button,
            appointments_view_button,
            create_appointment_button,
            products_view_button,
            create_product_button;

    @FXML
    private Label
            clients_registered_count,
            clients_registered_percentage_vs_last_month,
            active_employees_count,
            active_employees_this_month_count,
            appointments_today_count,
            finished_appointments_today_count,
            products_on_stock_count,
            low_stock_products_count;

    @FXML
    public void initialize() {

        dashboardReference = borderPane.getCenter();

        loadStatistics();

        configureButtonActions();
    }

    public void reloadDashboard() {

        animateChangeAndLoadStats();
    }

    private void animateChangeAndLoadStats() {

        animateViewChange(dashboardReference, borderPane);
        loadStatistics();
    }

    private void loadEventLog() {

        executeUITask(
                dashboardService::getRecentActivityLog,
                this::loadRecentActivitiesOnDashboard
        );
    }

    private void loadStatistics() {

        loadDashboardStats();

        loadEventLog();
    }

    private void loadRecentActivitiesOnDashboard(List<RecentActivityDTO> recentActivity) {

        cleanContainer(activity_log_vbox);

        if (recentActivity.isEmpty()) {

            showEmptyListLabel(EMPTY_ACTIVITY_LOG_MESSAGE, activity_log_vbox);

        } else {

            for (int i = 0; i < recentActivity.size(); i++) {

                RecentActivityDTO activityDTO = recentActivity.get(i);

                FXMLLoader loader = generateLoaderWithPath(ACTIVITY_LOG_ITEM_VIEW_PATH);

                Parent activityLog = returnParentFromLoader(loader, RECENT_ACTIVITY_VIEW_LOADING_FAILED);

                ActivityItemController activityItemController = loader.getController();

                activityItemController.setDataOnItem(activityDTO);

                loadItemOnVBox(activity_log_vbox, activityLog);

                fadeNodeIn(activityLog, i * ANIMATION_DELAY_IN_MS);
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

        executeUITask(
                () -> {
                    Long clientsRegistered = clientService.getClientsRegisteredQuantity();
                    Long clientsRegisteredPercentageVsPreviousMonth = clientService.calculatePercentageOfClientsVsLastMonth();

                    return List.of(clientsRegistered, clientsRegisteredPercentageVsPreviousMonth);
                },

                longList -> {

                    setTextOnLabel(clients_registered_count, parseNumberValueToText(longList.getFirst()));
                    setTextOnLabel(clients_registered_percentage_vs_last_month, formatAsPercentage(Double.valueOf(longList.getLast())));
                }
        );
    }

    private void loadEmployeesStats() {

        executeUITask(
                () -> {
                    Long activeEmployees = employeeService.getActiveEmployees();
                    Long activeEmployeesThisMonthVsLastMonth = employeeService.calculateActiveEmployeesVsLastMonth();

                    return List.of(activeEmployees, activeEmployeesThisMonthVsLastMonth);
                },

                longList -> {

                    setTextOnLabel(active_employees_count, parseNumberValueToText(longList.getFirst()));
                    setTextOnLabel(active_employees_this_month_count, parseNumberValueToText(longList.getLast()));
                }
        );
    }

    private void loadAppointmentsStats() {

        executeUITask(
                () -> {
                    Long appointmentsTodayCount = appointmentService.appointmentsToday();
                    Long finishedAppointmentsToday = appointmentService.completedAppointmentsToday();

                    return List.of(appointmentsTodayCount, finishedAppointmentsToday);
                },
                longList -> {

                    setTextOnLabel(appointments_today_count, parseNumberValueToText(longList.getFirst()));
                    setTextOnLabel(finished_appointments_today_count, parseNumberValueToText(longList.getLast()));
                }
        );
    }

    private void loadProductsStats() {

        executeUITask(
                () -> {
                    Long productsOnStockCount = productService.getProductsRegisteredCount();
                    Long lowStockProductCount = productService.getProductsOnLowStock();

                    return List.of(productsOnStockCount, lowStockProductCount);
                },
                longList -> {

                    setTextOnLabel(products_on_stock_count, parseNumberValueToText(longList.getFirst()));
                    setTextOnLabel(low_stock_products_count, parseNumberValueToText(longList.getLast()));
                }
        );
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> navBarButtonsMap = Map.of(
                navbar_dashboard_button, () -> redirectToView(ViewRedirection.DASHBOARD, borderPane, applicationContext, this::reloadDashboard),
                navbar_client_button, () -> redirectToView(ViewRedirection.CLIENTS, borderPane, applicationContext),
                navbar_employee_button, () -> redirectToView(ViewRedirection.EMPLOYEES, borderPane, applicationContext),
                navbar_appointment_button, () -> redirectToView(ViewRedirection.APPOINTMENTS, borderPane, applicationContext),
                navbar_barber_service_button, () -> redirectToView(ViewRedirection.BARBER_SERVICES, borderPane, applicationContext),
                navbar_product_button, () -> redirectToView(ViewRedirection.PRODUCTS, borderPane, applicationContext),
                navbar_settings_button, () -> redirectToView(ViewRedirection.SETTINGS, borderPane, applicationContext),
                navbar_logout_button, () -> redirectToView(ViewRedirection.LOGOUT, borderPane, applicationContext)
        );

        Map<Button, Runnable> quickAccessButtonsMap = Map.of(
                clients_view_button, () -> redirectToView(ViewRedirection.CLIENTS, borderPane, applicationContext),
                employee_view_button, () -> redirectToView(ViewRedirection.EMPLOYEES, borderPane, applicationContext),
                appointments_view_button, () -> redirectToView(ViewRedirection.APPOINTMENTS, borderPane, applicationContext),
                products_view_button, () -> redirectToView(ViewRedirection.PRODUCTS, borderPane, applicationContext)
        );

        Map<Button, Runnable> quickCreationButtonsMap = Map.of(
                create_client_button, () -> redirectToView(ViewRedirection.CLIENT_CREATION, borderPane, applicationContext),
                create_employee_button, () -> redirectToView(ViewRedirection.EMPLOYEE_CREATION, borderPane, applicationContext),
                create_appointment_button, () -> redirectToView(ViewRedirection.APPOINTMENT_CREATION, borderPane, applicationContext),
                create_product_button, () -> redirectToView(ViewRedirection.PRODUCT_CREATION, borderPane, applicationContext)
        );

        configureRunnableMaps(navBarButtonsMap, quickAccessButtonsMap, quickCreationButtonsMap);
    }
}