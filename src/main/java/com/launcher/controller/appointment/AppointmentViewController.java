package com.launcher.controller.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.AppointmentStatus;
import com.enums.ToastNotificationType;
import com.exceptions.appointment.InvalidAppointmentUpdateException;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.launcher.animation.AnimationEngine.fadeNodeIn;
import static com.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.launcher.constants.AnimationEngineConstants.ANIMATION_DELAY_IN_MS;
import static com.launcher.constants.StringResource.EmptyListMessage.EMPTY_APPOINTMENTS_LIST_MESSAGE;
import static com.launcher.constants.StringResource.FxmlViewLoadingErrorMessage.*;
import static com.launcher.constants.StringResource.ToastNotificationMessage.APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE;
import static com.launcher.constants.ViewPath.*;
import static com.launcher.controller.helper.ComboBoxHelper.*;
import static com.launcher.controller.helper.ContainerManager.*;
import static com.launcher.controller.helper.FXMLViewLoader.*;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.parseNumberValueToText;
import static com.launcher.controller.helper.ValidationFormatter.setStringConverter;

@Component
@RequiredArgsConstructor
public class AppointmentViewController {

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

        loadAppointmentsListOnView(appointmentInfoDTOList);
        loadEnumsOnComboBox(appointment_status_selector, AppointmentStatus.values());
        setStringConverter(appointment_status_selector, AppointmentStatus.TODOS);
        loadGenericTypeListOnComboBox(employee_selector, employees);
    }

    private void loadAppointmentsListOnView(List<AppointmentInfoDTO> appointmentInfoDTOList) {
        if (appointmentInfoDTOList.isEmpty()) {
            showEmptyListLabel(EMPTY_APPOINTMENTS_LIST_MESSAGE, appointment_list_VBox);
        } else {
            for (int i = 0; i < appointmentInfoDTOList.size(); i++) {
                AppointmentInfoDTO infoDTO = appointmentInfoDTOList.get(i);
                FXMLLoader loader = generateLoaderWithPath(APPOINTMENT_ITEM_VIEW_PATH);
                Parent appointmentView = returnParentFromLoader(loader, APPOINTMENTS_VIEW_LOADING_FAILED);
                AppointmentItemController appointmentCardItemController = loader.getController();
                appointmentCardItemController.setDataOnItem(infoDTO);
                appointmentCardItemController.setOnCompleteCallback(this::markAppointmentAsComplete);
                appointmentCardItemController.setOnCancelCallback(this::markAppointmentAsCanceled);
                appointmentCardItemController.setOnEditCallback(this::goToAppointmentEditionView);
                loadItemOnVBox(appointment_list_VBox, appointmentView);
                fadeNodeIn(appointment_list_VBox, i * ANIMATION_DELAY_IN_MS);
            }
        }
    }

    private void markAppointmentAsComplete(AppointmentInfoDTO dto) {
        try {
            appointmentService.markAppointmentAsComplete(dto);
            showToastNotification(anchor_pane, applicationContext, APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
        } catch (InvalidAppointmentUpdateException exception) {
            showToastNotification(anchor_pane, applicationContext, exception.getMessage(), ToastNotificationType.FAILED);
        }
    }

    private void markAppointmentAsCanceled(AppointmentInfoDTO dto) {
        try {
            appointmentService.markAppointmentAsCanceled(dto);
            showToastNotification(anchor_pane, applicationContext, APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
        } catch (InvalidAppointmentUpdateException exception) {
            showToastNotification(anchor_pane, applicationContext, exception.getMessage(), ToastNotificationType.FAILED);
        }
    }

    private void goToAppointmentCreationView() {
        loadViewOnPane(APPOINTMENT_CREATION_VIEW_PATH, applicationContext, APPOINTMENT_CREATION_VIEW_LOADING_FAILED, anchor_pane);
    }

    private void goToAppointmentEditionView(AppointmentInfoDTO infoDTO) {
        FXMLLoader loader = generateLoaderWithPath(APPOINTMENT_EDITION_VIEW_PATH);
        setControllerOnLoader(loader, applicationContext);
        Parent appointmentEditionView = returnParentFromLoader(loader, APPOINTMENT_EDITION_VIEW_LOADING_FAILED);
        AppointmentEditionController appointmentEditionController = loader.getController();
        appointmentEditionController.initialize(infoDTO);
        setViewOnPaneCenter(anchor_pane, appointmentEditionView);
    }

    private void loadAppointmentsStats() {
        loadAppointmentsTodayStats();
        loadPendingAppointmentsStats();
        loadMonthlyVolumeStats();
        loadCanceledAppointmentsStats();
        totalAppointmentsFound();
    }

    private void loadAppointmentsTodayStats() {
        executeUITask(
                appointmentService::getAppointmentsTodayStats,
                appointmentTodayStatsDTO -> {
                    setTextOnLabel(appointments_schedule_for_today, parseNumberValueToText(appointmentTodayStatsDTO.getAppointmentCount()));
                    setTextOnLabel(completed_appointments_today, parseNumberValueToText(appointmentTodayStatsDTO.getTotalAmountAsFinished()));
                }
        );
    }

    private void loadPendingAppointmentsStats() {
        executeUITask(
                appointmentService::getPendingAppointmentsStats,
                appointmentTomorrowStatsDTO -> {
                    setTextOnLabel(pending_appointments, parseNumberValueToText(appointmentTomorrowStatsDTO.getTotalPendingAppointments()));
                    setTextOnLabel(appointments_scheduled_tomorrow, parseNumberValueToText(appointmentTomorrowStatsDTO.getScheduledAppointmentsTomorrow()));
                }
        );
    }

    private void loadMonthlyVolumeStats() {
        executeUITask(
                appointmentService::getMonthlyComparisonStats,
                appointmentMonthlyComparisonDTO -> {
                    setTextOnLabel(current_month_appointment_count, parseNumberValueToText(appointmentMonthlyComparisonDTO.getCurrentMonthAppointments()));
                    setTextOnLabel(previous_month_appointment_count, parseNumberValueToText(appointmentMonthlyComparisonDTO.getPreviousMonthAppointments()));
                }
        );
    }

    private void loadCanceledAppointmentsStats() {
        executeUITask(
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
        executeUITask(
                appointmentService::getCount,
                count -> setTextOnLabel(total_appointments_count, parseNumberValueToText(count) + " encontrados")
        );
    }

    private void configureLiveSearch() {
        client_search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        appointment_status_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        date_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        employee_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    private void executeLiveSearch() {
        String clientName = client_search_field.getText();
        AppointmentStatus selectedAppointmentStatus = appointment_status_selector.getValue();
        EmployeeInfoDTO employeeSelected = employee_selector.getValue();
        String employeeName = employeeSelected == null ? "" : employeeSelected.getFirstName() + " " + employeeSelected.getLastName();
        if (selectedAppointmentStatus == AppointmentStatus.TODOS)
            selectedAppointmentStatus = null;
        LocalDate date = date_selector.getValue();
        List<AppointmentInfoDTO> appointments = appointmentService.liveSearch(clientName, date, selectedAppointmentStatus, employeeName);
        cleanContainer(appointment_list_VBox);
        loadAppointmentsListOnView(appointments);
        setTextOnLabel(total_appointments_count, parseNumberValueToText(appointments.size()) + " encontrados");
    }

    private void cleanFiltersAndLiveSearch() {
        cleanDatePicker(date_selector);
        setBlankTextfield(client_search_field);
        cleanComboBoxes(appointment_status_selector, employee_selector);
    }

    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                clear_filters_button, this::cleanFiltersAndLiveSearch,
                register_new_appointment_button, this::goToAppointmentCreationView
        );
        configureRunnableMaps(map);
    }
}