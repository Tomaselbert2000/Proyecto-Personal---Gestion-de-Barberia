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
import com.presentation.support.view.ViewRedirectionHelper;

@Component
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ClientService clientService;
    private final AppointmentService appointmentService;
    private final ProductService productService;

    private final ApplicationContext applicationContext;
    private final ViewRedirectionHelper viewRedirectionHelper;

    @FXML
    private StackPane stackPane;

    @FXML
    private Node dashboardReference;

    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox activityLogVbox;

    @FXML
    private MFXButton
            navbarDashboardButton,
            navbarClientButton,
            navbarEmployeeButton,
            navbarAppointmentButton,
            navbarBarberServiceButton,
            navbarProductButton,
            navbarPaymentButton,
            navbarSettingsButton,
            navbarLogoutButton,
            clientsViewButton,
            createClientButton,
            employeeViewButton,
            createEmployeeButton,
            appointmentsViewButton,
            createAppointmentButton,
            productsViewButton,
            createProductButton;

    @FXML
    private Label
            newClientsThisMonth,
            newClientsPercentageVsLastMonth,
            expectedIncome,
            averageTicketValue,
            appointmentsTodayCount,
            finishedAppointmentsTodayCount,
            lowStockProductCount,
            outOfStockProductCount;

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

        cleanContainer(activityLogVbox);

        loadItemsOnController(
                recentActivity,
                activityLogVbox,
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
                    setTextOnLabel(newClientsThisMonth, parseNumberValueToText(clientAcquisitionStatsDTO.getNewClientsThisMonth()));
                    setTextOnLabel(newClientsPercentageVsLastMonth, formatAsPercentage(clientAcquisitionStatsDTO.getPercentageVsLastMonth()) + " vs mes anterior");
                }
        );
    }

    private void loadExpectedIncomeStats() {

        executeAsyncTask(
                appointmentService::getExpectedIncomeToday,
                expectedIncomeStatDTO -> {
                    setTextOnLabel(expectedIncome, formatAsPrice(expectedIncomeStatDTO.getExpectedIncomeSumForToday()));
                    setTextOnLabel(averageTicketValue, "Promedio por ticket " + formatPriceAsString(expectedIncomeStatDTO.getAverageTicket()));
                }
        );
    }

    private void loadAppointmentsTodayStats() {

        executeAsyncTask(
                appointmentService::getAppointmentsTodayStats,
                appointmentTodayStatsDTO -> {
                    setTextOnLabel(appointmentsTodayCount, parseNumberValueToText(appointmentTodayStatsDTO.getAppointmentCount()));
                    setTextOnLabel(finishedAppointmentsTodayCount, parseNumberValueToText(appointmentTodayStatsDTO.getTotalAmountAsFinished()));
                }
        );
    }

    private void loadProductStockStats() {

        executeAsyncTask(
                productService::getInventoryAlertStat,
                inventoryAlertStatsDTO -> {
                    setTextOnLabel(lowStockProductCount, parseNumberValueToText(inventoryAlertStatsDTO.getLowStockProductsCount()));
                    setTextOnLabel(outOfStockProductCount, "Agotados: " + parseNumberValueToText(inventoryAlertStatsDTO.getOutOfStockProductsCount()));
                }
        );
    }

    private void configureButtonActions() {

        Map<Button, Runnable> navBarButtonsMap = Map.of(
                navbarDashboardButton, () -> viewRedirectionHelper.redirectToView(DASHBOARD, borderPane, applicationContext, this::reloadDashboard),
                navbarClientButton, () -> viewRedirectionHelper.redirectToView(CLIENTS, borderPane, applicationContext),
                navbarEmployeeButton, () -> viewRedirectionHelper.redirectToView(EMPLOYEES, borderPane, applicationContext),
                navbarAppointmentButton, () -> viewRedirectionHelper.redirectToView(APPOINTMENTS, borderPane, applicationContext),
                navbarBarberServiceButton, () -> viewRedirectionHelper.redirectToView(BARBER_SERVICES, borderPane, applicationContext),
                navbarProductButton, () -> viewRedirectionHelper.redirectToView(PRODUCTS, borderPane, applicationContext),
                navbarPaymentButton, () -> viewRedirectionHelper.redirectToView(PAYMENT_METHODS, borderPane, applicationContext),
                navbarSettingsButton, () -> viewRedirectionHelper.redirectToView(SETTINGS, borderPane, applicationContext),
                navbarLogoutButton, this::manageLogout
        );

        Map<Button, Runnable> quickAccessButtonsMap = Map.of(
                clientsViewButton, () -> viewRedirectionHelper.redirectToView(CLIENTS, borderPane, applicationContext),
                employeeViewButton, () -> viewRedirectionHelper.redirectToView(EMPLOYEES, borderPane, applicationContext),
                appointmentsViewButton, () -> viewRedirectionHelper.redirectToView(APPOINTMENTS, borderPane, applicationContext),
                productsViewButton, () -> viewRedirectionHelper.redirectToView(PRODUCTS, borderPane, applicationContext)
        );

        Map<Button, Runnable> quickCreationButtonsMap = Map.of(
                createClientButton, () -> viewRedirectionHelper.redirectToView(CLIENT_CREATION, borderPane, applicationContext),
                createEmployeeButton, () -> viewRedirectionHelper.redirectToView(EMPLOYEE_CREATION, borderPane, applicationContext),
                createAppointmentButton, () -> viewRedirectionHelper.redirectToView(APPOINTMENT_CREATION, borderPane, applicationContext),
                createProductButton, () -> viewRedirectionHelper.redirectToView(PRODUCT_CREATION, borderPane, applicationContext)
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
                () -> viewRedirectionHelper.redirectToView(LOGIN, stackPane, applicationContext),
                () -> {
                }
        );
    }
}