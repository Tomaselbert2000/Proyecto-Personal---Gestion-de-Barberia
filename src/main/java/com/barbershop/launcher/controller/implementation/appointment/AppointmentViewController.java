package com.barbershop.launcher.controller.implementation.appointment;

import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.exceptions.appointment.InvalidAppointmentUpdateException;
import com.barbershop.launcher.controller.interfaces.ViewController;
import com.barbershop.service.interfaces.AppointmentService;
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
    private Label appointments_today;

    @FXML
    private Label completed_appointments_today;

    @FXML
    private Label scheduled_appointments;

    @FXML
    private Label new_appointments_scheduled_today;

    @FXML
    private Label appointments_this_month;

    @FXML
    private Label percentage_of_appointments_registered_vs_previous_month;

    @FXML
    private Label canceled_appointments;

    @FXML
    private Label canceled_appointments_vs_past_week;

    @FXML
    private Label total_appointments_count;

    @FXML
    private Button clear_filters_button;

    @FXML
    private Button register_new_appointment_button;

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

        loadDTOsOnComboBox(employee_selector, employees);

        configureLiveSearch();
        configureButtonActions();
    }

    private void loadAppointmentsListOnView(List<AppointmentInfoDTO> appointmentInfoDTOList) {

        if (appointmentInfoDTOList.isEmpty()) {

            showEmptyListLabel(EMPTY_APPOINTMENTS_LIST_MESSAGE, appointment_list_VBox);

        } else {

            for (AppointmentInfoDTO infoDTO : appointmentInfoDTOList) {

                FXMLLoader loader = generateLoaderWithPath(APPOINTMENT_ITEM_VIEW_PATH);

                Parent appointmentView = returnParentFromLoader(loader, APPOINTMENTS_VIEW_LOADING_FAILED);

                AppointmentItemController appointmentCardItemController = loader.getController();

                appointmentCardItemController.setDataOnItem(infoDTO);

                appointmentCardItemController.setOnCompleteCallback(this::markAppointmentAsComplete);
                appointmentCardItemController.setOnCancelCallback(this::markAppointmentAsCanceled);
                appointmentCardItemController.setOnEditCallback(this::goToAppointmentEditionView);

                loadItemOnVBox(appointment_list_VBox, appointmentView);
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

        Long appointmentsToday = appointmentService.appointmentsToday();

        setTextOnLabel(appointments_today, appointmentsToday.toString());

        Long completedAppointmentsToday = appointmentService.completedAppointmentsToday();

        setTextOnLabel(completed_appointments_today, completedAppointmentsToday.toString());
    }

    private void statsOfScheduledAppointmentsInTheFuture() {

        Long appointmentsWithStatus_PROGRAMADO = appointmentService.appointmentsByStatus(AppointmentStatus.PROGRAMADO);

        setTextOnLabel(scheduled_appointments, appointmentsWithStatus_PROGRAMADO.toString());

        Long newAppointmentsCreatedToday = appointmentService.appointmentsCreatedToday();

        setTextOnLabel(new_appointments_scheduled_today, newAppointmentsCreatedToday.toString());
    }

    private void statsOfAppointmentsThisMonth() {

        Long appointmentsThisMonth = appointmentService.appointmentsDuringThisMonth();

        setTextOnLabel(appointments_this_month, appointmentsThisMonth.toString());

        Long percentageOfAppointmentsRegisteredVsPreviousMonth = appointmentService.calculatePercentageOfAppointmentsVsPreviousMonth();

        setTextOnLabel(percentage_of_appointments_registered_vs_previous_month, percentageOfAppointmentsRegisteredVsPreviousMonth.toString() + "%");
    }

    private void statsOfCanceledAppointments() {

        Long appointmentsCanceled = appointmentService.canceledAppointments();

        setTextOnLabel(canceled_appointments, appointmentsCanceled.toString());

        Long appointmentsCanceledVsPastWeek = appointmentService.canceledAppointmentsVsPastWeek();

        setTextOnLabel(canceled_appointments_vs_past_week, appointmentsCanceledVsPastWeek.toString());
    }

    private void totalAppointmentsFound() {

        Long appointmentsCount = appointmentService.getTotalAppointmentsCount();

        setTextOnLabel(total_appointments_count, appointmentsCount.toString() + " encontrados");
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

        cleanVBox(appointment_list_VBox);

        loadAppointmentsListOnView(appointments);
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
