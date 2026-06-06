package com.barbershop.launcher.controller.implementation.appointment;

import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.exceptions.appointment.InvalidAppointmentUpdateException;
import com.barbershop.launcher.controller.interfaces.ViewController;
import com.barbershop.service.interfaces.AppointmentService;
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

import static com.barbershop.launcher.animation.AnimationEngine.fadeNodeIn;
import static com.barbershop.launcher.animation.AnimationEngineConstant.ANIMATION_DELAY_IN_MS;
import static com.barbershop.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.barbershop.launcher.constants.ui.messages.EmptyListMessage.EMPTY_APPOINTMENTS_LIST_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.helper.ContainerManager.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.*;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.*;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;

@Component
@RequiredArgsConstructor
public class AppointmentViewController implements ViewController {

    private final AppointmentService appointmentService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private VBox appointment_list_VBox;

    @FXML
    private Label
            appointments_today,
            completed_appointments_today,
            scheduled_appointments,
            new_appointments_scheduled_today,
            appointments_this_month,
            percentage_of_appointments_registered_vs_previous_month,
            canceled_appointments,
            canceled_appointments_vs_past_week,
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

        List<AppointmentInfoDTO> appointmentInfoDTOList = appointmentService.getAppointmentsList();
        List<EmployeeInfoDTO> employees = appointmentService.getEmployeesFromServiceInstance();

        loadAppointmentsListOnView(appointmentInfoDTOList);

        loadEnumsOnComboBox(appointment_status_selector, AppointmentStatus.values());
        setStringConverter(appointment_status_selector, AppointmentStatus.TODOS);

        loadGenericTypeListOnComboBox(employee_selector, employees);

        configureLiveSearch();
        configureButtonActions();
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

        setViewOnAnchorPaneCenter(anchor_pane, appointmentEditionView);
    }

    private void loadAppointmentsStats() {

        statsOfAppointmentsScheduledForToday();

        statsOfScheduledAppointmentsInTheFuture();

        statsOfAppointmentsThisMonth();

        statsOfCanceledAppointments();

        totalAppointmentsFound();
    }

    private void statsOfAppointmentsScheduledForToday() {

        executeUITask(
                () -> {
                    Long appointmentsToday = appointmentService.appointmentsToday();
                    Long completedAppointmentsToday = appointmentService.completedAppointmentsToday();

                    return List.of(appointmentsToday, completedAppointmentsToday);
                },
                uiActionValues -> {

                    setTextOnLabel(appointments_today, parseNumberValueToText(uiActionValues.getFirst()));
                    setTextOnLabel(completed_appointments_today, parseNumberValueToText(uiActionValues.getLast()));
                }
        );
    }

    private void statsOfScheduledAppointmentsInTheFuture() {

        executeUITask(
                () -> {
                    Long appointmentsWithStatus_PROGRAMADO = appointmentService.appointmentsByStatus(AppointmentStatus.PROGRAMADO);
                    Long newAppointmentsCreatedToday = appointmentService.appointmentsCreatedToday();

                    return List.of(appointmentsWithStatus_PROGRAMADO, newAppointmentsCreatedToday);
                },
                uiActionValues -> {

                    setTextOnLabel(scheduled_appointments, parseNumberValueToText(uiActionValues.getFirst()));
                    setTextOnLabel(new_appointments_scheduled_today, parseNumberValueToText(uiActionValues.getLast()));
                }
        );
    }

    private void statsOfAppointmentsThisMonth() {

        executeUITask(
                () -> {
                    Long appointmentsThisMonth = appointmentService.appointmentsDuringThisMonth();
                    Long percentageOfAppointmentsRegisteredVsPreviousMonth = appointmentService.calculatePercentageOfAppointmentsVsPreviousMonth();

                    return List.of(appointmentsThisMonth, percentageOfAppointmentsRegisteredVsPreviousMonth);
                },
                uiActionValues -> {

                    setTextOnLabel(appointments_this_month, parseNumberValueToText(uiActionValues.getFirst()));
                    setTextOnLabel(percentage_of_appointments_registered_vs_previous_month, formatAsPercentage(Double.valueOf(uiActionValues.getLast())) + "%");
                }
        );
    }

    private void statsOfCanceledAppointments() {

        executeUITask(
                () -> {
                    Long appointmentsCanceled = appointmentService.canceledAppointments();
                    Long appointmentsCanceledVsPastWeek = appointmentService.canceledAppointmentsVsPastWeek();

                    return List.of(appointmentsCanceled, appointmentsCanceledVsPastWeek);
                },
                uiActionValues -> {

                    setTextOnLabel(canceled_appointments, parseNumberValueToText(uiActionValues.getFirst()));
                    setTextOnLabel(canceled_appointments_vs_past_week, parseNumberValueToText(uiActionValues.getLast()));
                }
        );
    }

    private void totalAppointmentsFound() {

        executeUITask(
                appointmentService::getTotalAppointmentsCount,
                uiActionValue -> setTextOnLabel(total_appointments_count, parseNumberValueToText(uiActionValue) + " encontrados")
        );
    }

    @Override
    public void configureLiveSearch() {

        client_search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        appointment_status_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        date_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        employee_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    @Override
    public void executeLiveSearch() {

        String clientName = client_search_field.getText();

        AppointmentStatus selectedAppointmentStatus = appointment_status_selector.getValue();

        EmployeeInfoDTO employeeSelected = employee_selector.getValue();

        String employeeName;

        if (employeeSelected == null) {

            employeeName = "";

        } else {

            employeeName = employeeSelected.getFirstName() + " " + employeeSelected.getLastName();

        }

        if (selectedAppointmentStatus == AppointmentStatus.TODOS) {

            selectedAppointmentStatus = null;
        }

        LocalDate date = date_selector.getValue();

        List<AppointmentInfoDTO> appointments = appointmentService.liveSearch(clientName, date, selectedAppointmentStatus, employeeName);

        cleanContainer(appointment_list_VBox);

        loadAppointmentsListOnView(appointments);

        setTextOnLabel(total_appointments_count, parseNumberValueToText(appointments.size()) + " encontrados");
    }

    @Override
    public void cleanFiltersAndLiveSearch() {

        cleanDatePicker(date_selector);
        setBlankTextfield(client_search_field);
        cleanComboBoxes(appointment_status_selector, employee_selector);
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                clear_filters_button, this::cleanFiltersAndLiveSearch,
                register_new_appointment_button, this::goToAppointmentCreationView
        );

        configureRunnableMaps(map);
    }
}
