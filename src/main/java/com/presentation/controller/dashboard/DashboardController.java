package com.presentation.controller.dashboard;

import com.dto.activity.RecentActivityDTO;
import com.presentation.support.view.ViewRedirectionHelper;
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
import static com.presentation.constants.StringResource.StatMessageSuffix.*;
import static com.presentation.constants.ViewPath.ACTIVITY_LOG_ITEM_VIEW_PATH;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextOnLabel;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.dialog.DialogHelper.showConfirmationDialog;
import static com.presentation.support.format.PriceFormatter.formatPriceAsString;
import static com.presentation.support.view.ContainerManager.cleanContainer;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.animateViewChange;

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
                ActivityItemController.class,
                ACTIVITY_LOG_ITEM_VIEW_PATH,
                EMPTY_ACTIVITY_LOG_MESSAGE,
                RECENT_ACTIVITY_VIEW_LOADING_FAILED,
                _ -> {
                }
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
                    setTextOnLabel(newClientsPercentageVsLastMonth, formatAsPercentage(clientAcquisitionStatsDTO.getPercentageVsLastMonth()) + VS_LAST_MONTH);
                }
        );
    }

    private void loadExpectedIncomeStats() {

        executeAsyncTask(
                appointmentService::getExpectedIncomeToday,
                expectedIncomeStatDTO -> {
                    setTextOnLabel(expectedIncome, formatAsPrice(expectedIncomeStatDTO.getExpectedIncomeSumForToday()));
                    setTextOnLabel(averageTicketValue, AVERAGE_PER_TICKET + formatPriceAsString(expectedIncomeStatDTO.getAverageTicket()));
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
                    setTextOnLabel(outOfStockProductCount, OUT_OF_STOCK_PREFFIX + parseNumberValueToText(inventoryAlertStatsDTO.getOutOfStockProductsCount()));
                }
        );
    }

    private void configureButtonActions() {

        Map<Button, Runnable> navBarButtonsMap = Map.ofEntries(
                Map.entry(navbarDashboardButton, () -> viewRedirectionHelper.redirectToView(DASHBOARD, borderPane, applicationContext, this::reloadDashboard)),
                Map.entry(navbarClientButton, () -> viewRedirectionHelper.redirectToView(CLIENTS, borderPane, applicationContext)),
                Map.entry(navbarEmployeeButton, () -> viewRedirectionHelper.redirectToView(EMPLOYEES, borderPane, applicationContext)),
                Map.entry(navbarAppointmentButton, () -> viewRedirectionHelper.redirectToView(APPOINTMENTS, borderPane, applicationContext)),
                Map.entry(navbarBarberServiceButton, () -> viewRedirectionHelper.redirectToView(BARBER_SERVICES, borderPane, applicationContext)),
                Map.entry(navbarProductButton, () -> viewRedirectionHelper.redirectToView(PRODUCTS, borderPane, applicationContext)),
                Map.entry(navbarPaymentButton, () -> viewRedirectionHelper.redirectToView(PAYMENT_METHODS, borderPane, applicationContext)),
                Map.entry(navbarSettingsButton, () -> viewRedirectionHelper.redirectToView(SETTINGS, borderPane, applicationContext)),
                Map.entry(navbarLogoutButton, this::manageLogout)
        );

        Map<Button, Runnable> quickAccessButtonsMap = Map.ofEntries(
                Map.entry(clientsViewButton, () -> viewRedirectionHelper.redirectToView(CLIENTS, borderPane, applicationContext)),
                Map.entry(employeeViewButton, () -> viewRedirectionHelper.redirectToView(EMPLOYEES, borderPane, applicationContext)),
                Map.entry(appointmentsViewButton, () -> viewRedirectionHelper.redirectToView(APPOINTMENTS, borderPane, applicationContext)),
                Map.entry(productsViewButton, () -> viewRedirectionHelper.redirectToView(PRODUCTS, borderPane, applicationContext))
        );

        Map<Button, Runnable> quickCreationButtonsMap = Map.ofEntries(
                Map.entry(createClientButton, () -> viewRedirectionHelper.redirectToView(CLIENT_CREATION, borderPane, applicationContext)),
                Map.entry(createEmployeeButton, () -> viewRedirectionHelper.redirectToView(EMPLOYEE_CREATION, borderPane, applicationContext)),
                Map.entry(createAppointmentButton, () -> viewRedirectionHelper.redirectToView(APPOINTMENT_CREATION, borderPane, applicationContext)),
                Map.entry(createProductButton, () -> viewRedirectionHelper.redirectToView(PRODUCT_CREATION, borderPane, applicationContext))
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