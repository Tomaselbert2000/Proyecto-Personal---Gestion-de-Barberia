package com.presentation.controller.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.AppointmentStatus;
import com.enums.ToastNotificationType;
import com.exceptions.appointment.InvalidAppointmentUpdateException;
import com.presentation.controller.item.BaseCatalogViewController;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
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
    private AnchorPane anchor_pane;

    @FXML
    private VBox appointment_list_VBox;

    @FXML
    private Label
            appointments_schedule_for_today,
            completed_appointments_today,
            pending_appointments,
            appointments_scheduled_tomorrow,
            current_month_appointment_count,
            previous_month_appointment_count,
            canceled_appointments_this_month,
            cancelation_percentage,
            total_appointments_count;

    @FXML
    private MFXButton
            clear_filters_button,
            register_new_appointment_button;

    @FXML
    private ComboBox<AppointmentStatus> appointment_status_selector;

    @FXML
    private ComboBox<EmployeeInfoDTO> employee_selector;

    @FXML
    private TextField client_search_field;

    @FXML
    private DatePicker date_selector;

    @FXML
    public void initialize() {
        loadAppointmentsStats();
        loadAppointmentsList();
        configureLiveSearch();
        configureButtonActions();
    }

    private void loadAppointmentsList() {
        List<AppointmentInfoDTO> appointmentInfoDTOList = appointmentService.getAppointmentsList();
        List<EmployeeInfoDTO> employees = appointmentService.getEmployeesFromServiceInstance();

        loadItemsOnView(appointmentInfoDTOList);
        loadEnumsOnComboBox(appointment_status_selector, AppointmentStatus.values());
        setStringConverter(appointment_status_selector, AppointmentStatus.TODOS);
        loadGenericTypeListOnComboBox(employee_selector, employees);
    }

    private void markAppointmentAsComplete(AppointmentInfoDTO dto) {

        try {

            appointmentService.markAppointmentAsComplete(dto);

            showToastNotification(
                    anchor_pane,
                    applicationContext,
                    APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE,
                    ToastNotificationType.SUCCESSFUL
            );

        } catch (InvalidAppointmentUpdateException exception) {

            showToastNotification(
                    anchor_pane,
                    applicationContext,
                    exception.getMessage(),
                    ToastNotificationType.FAILED
            );
        }
    }

    private void markAppointmentAsCanceled(AppointmentInfoDTO dto) {

        try {

            appointmentService.markAppointmentAsCanceled(dto);
            showToastNotification(
                    anchor_pane,
                    applicationContext,
                    APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE,
                    ToastNotificationType.SUCCESSFUL
            );

        } catch (InvalidAppointmentUpdateException exception) {

            showToastNotification(anchor_pane, applicationContext, exception.getMessage(), ToastNotificationType.FAILED);
        }
    }

    private void goToAppointmentCreationView() {

        loadViewOnPane(
                APPOINTMENT_CREATION_VIEW_PATH,
                applicationContext,
                APPOINTMENT_CREATION_VIEW_LOADING_FAILED,
                anchor_pane
        );
    }

    private void goToAppointmentEditionView(AppointmentInfoDTO infoDTO) {

        loadViewWithControllerPane(
                APPOINTMENT_EDITION_VIEW_PATH,
                applicationContext,
                APPOINTMENT_EDITION_VIEW_LOADING_FAILED,
                anchor_pane,
                AppointmentEditionController.class,
                editionController -> editionController.initialize(infoDTO)
        );
    }

    private void loadAppointmentsStats() {
        loadAppointmentsTodayStats();
        loadPendingAppointmentsStats();
        loadMonthlyVolumeStats();
        loadCanceledAppointmentsStats();
        totalAppointmentsFound();
    }

    private void loadAppointmentsTodayStats() {
        executeAsyncTask(
                appointmentService::getAppointmentsTodayStats,
                appointmentTodayStatsDTO -> {
                    setTextOnLabel(appointments_schedule_for_today, parseNumberValueToText(appointmentTodayStatsDTO.getAppointmentCount()));
                    setTextOnLabel(completed_appointments_today, parseNumberValueToText(appointmentTodayStatsDTO.getTotalAmountAsFinished()));
                }
        );
    }

    private void loadPendingAppointmentsStats() {
        executeAsyncTask(
                appointmentService::getPendingAppointmentsStats,
                appointmentTomorrowStatsDTO -> {
                    setTextOnLabel(pending_appointments, parseNumberValueToText(appointmentTomorrowStatsDTO.getTotalPendingAppointments()));
                    setTextOnLabel(appointments_scheduled_tomorrow, parseNumberValueToText(appointmentTomorrowStatsDTO.getScheduledAppointmentsTomorrow()));
                }
        );
    }

    private void loadMonthlyVolumeStats() {
        executeAsyncTask(
                appointmentService::getMonthlyComparisonStats,
                appointmentMonthlyComparisonDTO -> {
                    setTextOnLabel(current_month_appointment_count, parseNumberValueToText(appointmentMonthlyComparisonDTO.getCurrentMonthAppointments()));
                    setTextOnLabel(previous_month_appointment_count, parseNumberValueToText(appointmentMonthlyComparisonDTO.getPreviousMonthAppointments()));
                }
        );
    }

    private void loadCanceledAppointmentsStats() {
        executeAsyncTask(
                appointmentService::getCanceledStats,
                appointmentCanceledStatsDTO -> {
                    setTextOnLabel(canceled_appointments_this_month, parseNumberValueToText(appointmentCanceledStatsDTO.getCanceledAppointmentThisMonth()));
                    setTextOnLabel(
                            cancelation_percentage,
                            parseNumberValueToText(appointmentCanceledStatsDTO.getCanceledAppointmentPercentage()) + "%" + " de un total de " + appointmentCanceledStatsDTO.getCanceledAppointmentThisMonth());
                }
        );
    }

    private void totalAppointmentsFound() {
        executeAsyncTask(
                appointmentService::getCount,
                count -> setTextOnLabel(total_appointments_count, parseNumberValueToText(count) + " encontrados")
        );
    }

    private void configureLiveSearch() {

        attachLiveSearchListeners(
                client_search_field.textProperty(),
                appointment_status_selector.valueProperty(),
                date_selector.valueProperty(),
                employee_selector.valueProperty()
        );
    }

    @Override
    protected List<AppointmentInfoDTO> searchCatalog() {

        String clientName = client_search_field.getText();

        AppointmentStatus selectedAppointmentStatus = appointment_status_selector.getValue();

        EmployeeInfoDTO employeeSelected = employee_selector.getValue();

        String employeeName = employeeSelected == null ? "" : employeeSelected.getFirstName() + " " + employeeSelected.getLastName();

        if (selectedAppointmentStatus == AppointmentStatus.TODOS) selectedAppointmentStatus = null;

        LocalDate date = date_selector.getValue();

        return appointmentService.liveSearch(clientName, date, selectedAppointmentStatus, employeeName);
    }

    @Override
    protected VBox getItemListContainer() {

        return appointment_list_VBox;
    }

    @Override
    protected void loadItemsOnView(List<AppointmentInfoDTO> appointmentInfoDTOList) {

        loadItemsOnController(
                appointmentInfoDTOList,
                appointment_list_VBox,
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
    protected void afterSearch(List<AppointmentInfoDTO> items) {

        setTextOnLabel(total_appointments_count, parseNumberValueToText(items.size()) + " encontrados");
    }

    @Override
    protected void clearFilterNodes() {

        cleanDatePicker(date_selector);
        setBlankTextfield(client_search_field);
        cleanComboBoxes(appointment_status_selector, employee_selector);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                clear_filters_button, this::resetSearchFilter,
                register_new_appointment_button, this::goToAppointmentCreationView
        );

        configureRunnableMaps(map);
    }
}