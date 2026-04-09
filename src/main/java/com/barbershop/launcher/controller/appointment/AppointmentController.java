package com.barbershop.launcher.controller.appointment;

import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.service.interfaces.AppointmentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.barbershop.launcher.controller.UI_RenderingFunctions.*;
import static com.barbershop.launcher.constants.ui.messages.UIMessages.APPOINTMENS_VIEW_LIST_LOADING_FAILED;
import static com.barbershop.launcher.constants.ui.messages.UIMessages.EMPTY_APPOINTMENTS_LIST_MESSAGE;
import static com.barbershop.launcher.constants.view.ViewPath.APPOINTMENT_ITEM_VIEW_PATH;

@Component
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

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
    private ComboBox<String> employee_selector;

    @FXML
    private TextField client_search_field;

    @FXML
    private DatePicker date_selector;

    @FXML
    public void initialize() {

        loadAppointmentsStats();

        List<AppointmentInfoDTO> appointmentInfoDTOList = appointmentService.getAppointmentsList();

        loadAppointmentsListOnView(appointmentInfoDTOList);

        loadEnumsOnComboBox(appointment_status_selector, AppointmentStatus.values());
        setStringConverter(appointment_status_selector, AppointmentStatus.TODOS);

        loadStringsOnComboBox(employee_selector, appointmentService.getEmployeeNames());

        configureAppointmentsLiveSearch();
        configureButtonActions();
    }

    private void configureAppointmentsLiveSearch() {

        client_search_field.textProperty().addListener((_, _, _) -> executeAppointmentLiveSearch());
        appointment_status_selector.valueProperty().addListener((_, _, _) -> executeAppointmentLiveSearch());
        date_selector.valueProperty().addListener((_, _, _) -> executeAppointmentLiveSearch());
        employee_selector.valueProperty().addListener((_, _, _) -> executeAppointmentLiveSearch());
    }

    private void executeAppointmentLiveSearch() {

        String clientName = client_search_field.getText();

        AppointmentStatus selectedAppointmentStatus = appointment_status_selector.getValue();

        String employeeName = employee_selector.getValue();

        if(selectedAppointmentStatus == AppointmentStatus.TODOS){

            selectedAppointmentStatus = null;
        }

        LocalDate date = date_selector.getValue();

        List<AppointmentInfoDTO> appointments = appointmentService.liveSearch(clientName, date, selectedAppointmentStatus, employeeName);

        cleanVBox(appointment_list_VBox);

        loadAppointmentsListOnView(appointments);
    }

    private void configureButtonActions() {

        clear_filters_button.setOnAction(_ -> cleanFiltersAndSearchField());
        register_new_appointment_button.setOnAction(_ -> goToAppointmentCreationView());
    }

    private void cleanFiltersAndSearchField() {

        cleanDatePicker(date_selector);
        setBlankTextfield(client_search_field);
        cleanComboBoxes(appointment_status_selector, employee_selector);
    }

    private void goToAppointmentCreationView() {


    }

    private void loadAppointmentsListOnView(List<AppointmentInfoDTO> appointmentInfoDTOList) {

        if (appointmentInfoDTOList.isEmpty()) {

            showEmptyListLabel(EMPTY_APPOINTMENTS_LIST_MESSAGE, appointment_list_VBox);

        } else {

            for (AppointmentInfoDTO infoDTO : appointmentInfoDTOList) {

                FXMLLoader loader = generateLoaderWithPath(APPOINTMENT_ITEM_VIEW_PATH);

                Parent appointmentView = returnParentFromLoader(loader, APPOINTMENS_VIEW_LIST_LOADING_FAILED);

                AppointmentCardItemController appointmentCardItemController = loader.getController();

                appointmentCardItemController.setDataOnItem(infoDTO);

                loadItemOnVBox(appointment_list_VBox, appointmentView);
            }
        }
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
}
