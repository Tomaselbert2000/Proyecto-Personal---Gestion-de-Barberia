package com.presentation.controller.dashboard;

import com.dto.activity.RecentActivityDTO;
import com.service.interfaces.AppointmentService;
import com.service.interfaces.ClientService;
import com.service.interfaces.DashboardService;
import com.service.interfaces.ProductService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.enums.ViewRedirection.*;
import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.LOGOUT_ICON;
import static com.presentation.constants.StringResource.ConfirmationDialog.*;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_ACTIVITY_LOG_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.RECENT_ACTIVITY_VIEW_LOADING_FAILED;
import static com.presentation.constants.ViewPath.ACTIVITY_LOG_ITEM_VIEW_PATH;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextOnLabel;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.dialog.DialogHelper.showConfirmationDialog;
import static com.presentation.support.format.PriceFormatter.formatPriceAsString;
import static com.presentation.support.view.ContainerManager.cleanContainer;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.animateViewChange;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ClientService clientService;
    private final AppointmentService appointmentService;
    private final ProductService productService;

    private final ApplicationContext applicationContext;

    @FXML
    private StackPane stack_pane;

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
            navbar_payment_button,
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
            new_clients_this_month,
            new_clients_percentage_vs_last_month,
            expected_income,
            average_ticket_value,
            appointments_today_count,
            finished_appointments_today_count,
            low_stock_product_count,
            out_of_stock_product_count;

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

        executeAsyncTask(
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

        loadItemsOnController(
                recentActivity,
                activity_log_vbox,
                ACTIVITY_LOG_ITEM_VIEW_PATH,
                EMPTY_ACTIVITY_LOG_MESSAGE,
                RECENT_ACTIVITY_VIEW_LOADING_FAILED
        );
    }

    private void loadDashboardStats() {

        loadClientAcquisitionStats();

        loadExpectedIncomeStats();

        loadAppointmentsTodayStats();

        loadProductStockStats();
    }

    private void loadClientAcquisitionStats() {

        executeAsyncTask(
                clientService::getClientStatsVsLastMonth,
                clientAcquisitionStatsDTO -> {
                    setTextOnLabel(new_clients_this_month, parseNumberValueToText(clientAcquisitionStatsDTO.getNewClientsThisMonth()));
                    setTextOnLabel(new_clients_percentage_vs_last_month, formatAsPercentage(clientAcquisitionStatsDTO.getPercentageVsLastMonth()) + " vs mes anterior");
                }
        );
    }

    private void loadExpectedIncomeStats() {

        executeAsyncTask(
                appointmentService::getExpectedIncomeToday,
                expectedIncomeStatDTO -> {
                    setTextOnLabel(expected_income, formatAsPrice(expectedIncomeStatDTO.getExpectedIncomeSumForToday()));
                    setTextOnLabel(average_ticket_value, "Promedio por ticket " + formatPriceAsString(expectedIncomeStatDTO.getAverageTicket()));
                }
        );
    }

    private void loadAppointmentsTodayStats() {

        executeAsyncTask(
                appointmentService::getAppointmentsTodayStats,
                appointmentTodayStatsDTO -> {
                    setTextOnLabel(appointments_today_count, parseNumberValueToText(appointmentTodayStatsDTO.getAppointmentCount()));
                    setTextOnLabel(finished_appointments_today_count, parseNumberValueToText(appointmentTodayStatsDTO.getTotalAmountAsFinished()));
                }
        );
    }

    private void loadProductStockStats() {

        executeAsyncTask(
                productService::getInventoryAlertStat,
                inventoryAlertStatsDTO -> {
                    setTextOnLabel(low_stock_product_count, parseNumberValueToText(inventoryAlertStatsDTO.getLowStockProductsCount()));
                    setTextOnLabel(out_of_stock_product_count, "Agotados: " + parseNumberValueToText(inventoryAlertStatsDTO.getOutOfStockProductsCount()));
                }
        );
    }

    private void configureButtonActions() {

        Map<Button, Runnable> navBarButtonsMap = Map.of(
                navbar_dashboard_button, () -> redirectToView(DASHBOARD, borderPane, applicationContext, this::reloadDashboard),
                navbar_client_button, () -> redirectToView(CLIENTS, borderPane, applicationContext),
                navbar_employee_button, () -> redirectToView(EMPLOYEES, borderPane, applicationContext),
                navbar_appointment_button, () -> redirectToView(APPOINTMENTS, borderPane, applicationContext),
                navbar_barber_service_button, () -> redirectToView(BARBER_SERVICES, borderPane, applicationContext),
                navbar_product_button, () -> redirectToView(PRODUCTS, borderPane, applicationContext),
                navbar_payment_button, () -> redirectToView(PAYMENT_METHODS, borderPane, applicationContext),
                navbar_settings_button, () -> redirectToView(SETTINGS, borderPane, applicationContext),
                navbar_logout_button, this::manageLogout
        );

        Map<Button, Runnable> quickAccessButtonsMap = Map.of(
                clients_view_button, () -> redirectToView(CLIENTS, borderPane, applicationContext),
                employee_view_button, () -> redirectToView(EMPLOYEES, borderPane, applicationContext),
                appointments_view_button, () -> redirectToView(APPOINTMENTS, borderPane, applicationContext),
                products_view_button, () -> redirectToView(PRODUCTS, borderPane, applicationContext)
        );

        Map<Button, Runnable> quickCreationButtonsMap = Map.of(
                create_client_button, () -> redirectToView(CLIENT_CREATION, borderPane, applicationContext),
                create_employee_button, () -> redirectToView(EMPLOYEE_CREATION, borderPane, applicationContext),
                create_appointment_button, () -> redirectToView(APPOINTMENT_CREATION, borderPane, applicationContext),
                create_product_button, () -> redirectToView(PRODUCT_CREATION, borderPane, applicationContext)
        );

        configureRunnableMaps(navBarButtonsMap, quickAccessButtonsMap, quickCreationButtonsMap);
    }

    private void manageLogout() {

        showConfirmationDialog(borderPane,
                applicationContext,
                CONFIRM_LOGOUT_DIALOG_TITLE,
                CONFIRM_LOGOUT_DIALOG_MESSAGE,
                CANCEL_BUTTON_TEXT,
                CONFIRM_BUTTON_TEXT,
                LOGOUT_ICON,
                () -> redirectToView(LOGIN, stack_pane, applicationContext),
                () -> {
                }
        );
    }
}