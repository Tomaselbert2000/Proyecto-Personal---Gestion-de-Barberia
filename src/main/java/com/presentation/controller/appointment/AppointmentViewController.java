package com.presentation.controller.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.AppointmentStatus;
import com.enums.ToastNotificationType;
import com.exceptions.appointment.InvalidAppointmentUpdateException;
import com.presentation.controller.BaseCatalogViewController;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_APPOINTMENTS_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.*;
import static com.presentation.constants.StringResource.StatMessageSuffix.*;
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.ViewPath.*;
import static com.presentation.support.control.ComboBoxHelper.*;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.loadViewOnPane;
import static com.presentation.support.view.FXMLViewLoader.loadViewWithControllerPane;

@Component
@RequiredArgsConstructor
public class AppointmentViewController extends BaseCatalogViewController<AppointmentInfoDTO> {

    private final AppointmentService appointmentService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox appointmentListVBox;

    @FXML
    private Label
            appointmentsScheduledToday,
            completedAppointmentsToday,
            pendingAppointments,
            appointmentsScheduledTomorrow,
            currentMonthAppointmentCount,
            previousMonthAppointmentCount,
            canceledAppointmentsThisMonth,
            cancellationPercentage,
            totalAppointmentsCount;

    @FXML
    private MFXButton
            clearFiltersButton,
            registerNewAppointmentButton;

    @FXML
    private ComboBox<AppointmentStatus> appointmentStatusSelector;

    @FXML
    private ComboBox<EmployeeInfoDTO> employeeSelector;

    @FXML
    private TextField clientSearchField;

    @FXML
    private DatePicker dateSelector;

    private void markAppointmentAsComplete(AppointmentInfoDTO dto) {

        executeAsyncTask(
                () -> {
                    try {
                        appointmentService.markAppointmentAsComplete(dto);
                        return null;
                    } catch (InvalidAppointmentUpdateException exception) {
                        return exception.getMessage();
                    }
                },
                errorMessage -> {
                    if (errorMessage == null) {
                        showToastNotification(
                                anchorPane,
                                applicationContext,
                                APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE,
                                ToastNotificationType.SUCCESSFUL
                        );
                    } else {
                        showToastNotification(anchorPane, applicationContext, errorMessage, ToastNotificationType.FAILED);
                    }
                }
        );
    }

    private void markAppointmentAsCanceled(AppointmentInfoDTO dto) {

        executeAsyncTask(

                () -> {
                    try {

                        appointmentService.markAppointmentAsCanceled(dto);

                        return null;

                    } catch (InvalidAppointmentUpdateException exception) {

                        return exception.getMessage();
                    }
                },

                errorMessage -> {
                    if (errorMessage == null) {

                        showToastNotification(
                                anchorPane,
                                applicationContext,
                                APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE,
                                ToastNotificationType.SUCCESSFUL
                        );

                    } else {

                        showToastNotification(anchorPane, applicationContext, errorMessage, ToastNotificationType.FAILED);
                    }
                }
        );
    }

    private void goToAppointmentCreationView() {

        loadViewOnPane(
                APPOINTMENT_CREATION_VIEW_PATH,
                applicationContext,
                APPOINTMENT_CREATION_VIEW_LOADING_FAILED,
                anchorPane
        );
    }

    private void goToAppointmentEditionView(AppointmentInfoDTO infoDTO) {

        loadViewWithControllerPane(
                APPOINTMENT_EDITION_VIEW_PATH,
                applicationContext,
                APPOINTMENT_EDITION_VIEW_LOADING_FAILED,
                anchorPane,
                AppointmentEditionController.class,
                editionController -> editionController.initialize(infoDTO)
        );
    }

    private void loadAppointmentsTodayStats() {
        executeAsyncTask(
                appointmentService::getAppointmentsTodayStats,
                appointmentTodayStatsDTO -> {
                    setTextOnLabel(appointmentsScheduledToday, parseNumberValueToText(appointmentTodayStatsDTO.getAppointmentCount()));
                    setTextOnLabel(completedAppointmentsToday, parseNumberValueToText(appointmentTodayStatsDTO.getTotalAmountAsFinished()) + COMPLETED);
                }
        );
    }

    private void loadPendingAppointmentsStats() {
        executeAsyncTask(
                appointmentService::getPendingAppointmentsStats,
                pendingAppointmentsStatsDTO -> {
                    setTextOnLabel(pendingAppointments, parseNumberValueToText(pendingAppointmentsStatsDTO.getTotalPendingAppointments()));
                    setTextOnLabel(appointmentsScheduledTomorrow, parseNumberValueToText(pendingAppointmentsStatsDTO.getScheduledAppointmentsTomorrow()) + SCHEDULED_TOMORROW);
                }
        );
    }

    private void loadMonthlyVolumeStats() {
        executeAsyncTask(
                appointmentService::getMonthlyComparisonStats,
                monthlyComparisonStatsDTO -> {
                    setTextOnLabel(currentMonthAppointmentCount, parseNumberValueToText(monthlyComparisonStatsDTO.getCurrentMonthAppointments()));
                    setTextOnLabel(previousMonthAppointmentCount, parseNumberValueToText(monthlyComparisonStatsDTO.getPreviousMonthAppointments()) + VS_LAST_MONTH);
                }
        );
    }

    private void loadCanceledAppointmentsStats() {
        executeAsyncTask(
                appointmentService::getCanceledStats,
                canceledStatsDTO -> {
                    setTextOnLabel(canceledAppointmentsThisMonth, parseNumberValueToText(canceledStatsDTO.getCanceledAppointmentThisMonth()));
                    setTextOnLabel(
                            cancellationPercentage,
                            parseNumberValueToText(canceledStatsDTO.getCanceledAppointmentPercentage()) + "%" + OUT_OF_A_TOTAL_OF + canceledStatsDTO.getCanceledAppointmentThisMonth());
                }
        );
    }

    private void totalAppointmentsFound() {
        executeAsyncTask(
                appointmentService::getCount,
                count -> setTextOnLabel(totalAppointmentsCount, parseNumberValueToText(count) + RESULTS_FOUND_SUFFIX)
        );
    }

    @Override
    protected Label getResultsCountLabel() {

        return totalAppointmentsCount;
    }

    @Override
    protected List<AppointmentInfoDTO> searchCatalog() {

        String clientName = clientSearchField.getText();

        AppointmentStatus selectedAppointmentStatus = appointmentStatusSelector.getValue();

        EmployeeInfoDTO employeeSelected = employeeSelector.getValue();

        String employeeName = employeeSelected == null ? "" : employeeSelected.getFirstName() + " " + employeeSelected.getLastName();

        selectedAppointmentStatus = nullIfTodos(selectedAppointmentStatus, AppointmentStatus.TODOS);

        LocalDate date = dateSelector.getValue();

        return appointmentService.liveSearch(clientName, date, selectedAppointmentStatus, employeeName);
    }

    @Override
    protected VBox getItemListContainer() {

        return appointmentListVBox;
    }

    @Override
    protected void loadItemsOnView(List<AppointmentInfoDTO> appointmentInfoDTOList) {

        loadItemsOnController(
                appointmentInfoDTOList,
                appointmentListVBox,
                AppointmentItemController.class,
                APPOINTMENT_ITEM_VIEW_PATH,
                EMPTY_APPOINTMENTS_LIST_MESSAGE,
                APPOINTMENTS_VIEW_LOADING_FAILED,
                itemController -> {

                    itemController.setOnCompleteCallback(this::markAppointmentAsComplete);
                    itemController.setOnCancelCallback(this::markAppointmentAsCanceled);
                    itemController.setOnEditCallback(this::goToAppointmentEditionView);
                }
        );
    }

    @Override
    protected void clearFilterNodes() {

        cleanDatePicker(dateSelector);
        setBlankTextfield(clientSearchField);
        resetComboBoxFilter(appointmentStatusSelector, employeeSelector);
    }

    @Override
    protected void initializeListContent() {

        List<AppointmentInfoDTO> appointments = appointmentService.getAppointmentsList();
        List<EmployeeInfoDTO> employees = appointmentService.getEmployeesFromServiceInstance();

        loadItemsOnView(appointments);

        loadEnumsOnComboBox(appointmentStatusSelector, AppointmentStatus.values());

        setStringConverter(appointmentStatusSelector, AppointmentStatus.TODOS);

        loadGenericTypeListOnComboBox(employeeSelector, employees);

        employeeSelector.getSelectionModel().selectFirst();
    }

    @Override
    protected ObservableValue<?>[] getSearchProperties() {

        return new ObservableValue<?>[]{
                appointmentStatusSelector.valueProperty(),
                employeeSelector.valueProperty(),
                clientSearchField.textProperty(),
                dateSelector.valueProperty()
        };
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(clearFiltersButton, this::resetSearchFilter),
                Map.entry(registerNewAppointmentButton, this::goToAppointmentCreationView)
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void loadGlobalStats() {

        loadAppointmentsTodayStats();
        loadPendingAppointmentsStats();
        loadMonthlyVolumeStats();
        loadCanceledAppointmentsStats();
        totalAppointmentsFound();
    }
}