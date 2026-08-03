package com.presentation.controller.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.AppointmentStatus;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import com.exceptions.common.EmployeeNotAvailableException;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.APPOINTMENT_DEFAULT_DURATION_IN_MINUTES;
import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.DATETIME_SUMMARY_FORMAT;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.APPOINTMENT_EDITION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.AppointmentDateTimeSummaryHelper.updateDatetimeSummary;
import static com.presentation.support.control.ComboBoxHelper.loadGenericTypeListOnComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.notification.ExceptionNotificationHandler.notifyValidationFailure;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;
import static com.presentation.support.view.VisibilityHelper.setNodeAsNotVisible;
import static com.presentation.support.view.VisibilityHelper.setNodeAsVisible;

@Component
@Getter
public class AppointmentEditionController extends BaseAppointmentFormController {

    private AppointmentInfoDTO infoDTOReference;
    private BarberServiceInfoDTO barberServiceReference;
    private EmployeeInfoDTO employeeReference;

    public AppointmentEditionController(AppointmentService appointmentService, ApplicationContext applicationContext) {
        super(appointmentService, applicationContext);
    }

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton
            back_button,
            restore_values_button,
            save_button;

    @FXML
    private Label
            current_client_name,
            current_service_name,
            current_employee_name,
            service_price,
            current_start_datetime,
            current_status_label,
            summary_client,
            summary_service,
            summary_employee,
            summary_datetime,
            summary_price;

    @FXML
    private HBox current_status_container;

    @FXML
    private ComboBox<BarberServiceInfoDTO> barber_service_selector;

    @FXML
    private ComboBox<EmployeeInfoDTO> employee_selector;

    @FXML
    private VBox
            service_selection_container,
            appointment_summary_card;

    @FXML
    private DatePicker date_selector;

    @FXML
    private ComboBox<LocalTime>
            hour_selector,
            minute_selector;

    @FXML
    private TextField appointment_notes;

    @FXML
    private ComboBox<AppointmentStatus> status_selector;

    @FXML
    public void initialize(AppointmentInfoDTO infoDTO) {
        this.infoDTOReference = infoDTO;
        loadAppointmentDataForEdition(infoDTO);
        configureButtonActions();
        configureBarberServiceSelection();
        configureEmployeeSelection();
        configureTimeSelectors();
    }

    private void loadAppointmentDataForEdition(AppointmentInfoDTO infoDTO) {
        String clientFullName = infoDTO.getClientFirstName() + " " + infoDTO.getClientLastName();
        String employeeFullName = infoDTO.getEmployeeFirstName() + " " + infoDTO.getEmployeeLastName();
        LocalDate appointmentDate = infoDTO.getStartDateTime().toLocalDate();
        int exactStartHour = infoDTO.getStartDateTime().getHour();
        int exactStartMinute = infoDTO.getStartDateTime().getMinute();
        String appointmentDateAsString = String.format(DATETIME_SUMMARY_FORMAT, appointmentDate, exactStartHour, exactStartMinute);
        List<BarberServiceInfoDTO> barberServices = appointmentService.getBarberServicesFromServiceInstance();
        List<EmployeeInfoDTO> employees = appointmentService.getEmployeesFromServiceInstance();
        Map<Label, String> map = Map.ofEntries(
                Map.entry(current_client_name, clientFullName),
                Map.entry(current_employee_name, employeeFullName),
                Map.entry(current_service_name, infoDTO.getServiceName()),
                Map.entry(current_start_datetime, appointmentDateAsString),
                Map.entry(current_status_label, infoDTO.getCurrentStatus().getDisplayName()),
                Map.entry(service_price, parseNumberValueToText(infoDTO.getServicePrice()))
        );
        setTextsOnLabelMap(map);
        loadGenericTypeListOnComboBox(barber_service_selector, barberServices);
        loadGenericTypeListOnComboBox(employee_selector, employees);
        loadAvailableStatuses(infoDTO.getCurrentStatus());
        setTextOnLabel(summary_client, clientFullName);
        setTextOnTextfield(appointment_notes, infoDTO.getOptionalNotes());
    }

    private void loadAvailableStatuses(AppointmentStatus currentStatus) {
        List<AppointmentStatus> allowedStatuses = new ArrayList<>();
        if (currentStatus == AppointmentStatus.CANCELADO || currentStatus == AppointmentStatus.FINALIZADO) {
            disableComboBox(status_selector);
            return;
        }
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status == AppointmentStatus.TODOS) continue;
            if (currentStatus == status) continue;
            if (currentStatus == AppointmentStatus.REPROGRAMADO && status == AppointmentStatus.PROGRAMADO) continue;
            allowedStatuses.add(status);
        }
        status_selector.getItems().addAll(allowedStatuses);
        setStringConverter(status_selector, status_selector.getItems().getFirst());
    }

    private void updateAppointment() {
        Long employeeID = null;
        Long barberServiceID = null;
        LocalDateTime newStartDateTime = null;
        LocalDateTime newEndDateTime = null;
        AppointmentStatus updatedStatus = null;
        if (employee_selector.getValue() != null) employeeID = employee_selector.getValue().getId();
        if (barber_service_selector.getValue() != null)
            barberServiceID = barber_service_selector.getValue().getBarberServiceId();
        if (date_selector.getValue() != null && hour_selector.getValue() != null && minute_selector.getValue() != null) {
            newStartDateTime = LocalDateTime.of(date_selector.getValue(), LocalTime.of(hour_selector.getValue().getHour(), minute_selector.getValue().getMinute()));
            newEndDateTime = newStartDateTime.plusMinutes(APPOINTMENT_DEFAULT_DURATION_IN_MINUTES);
        }
        if (status_selector.getValue() != null) updatedStatus = status_selector.getValue();
        AppointmentUpdateDTO updateDTO = buildDTOFromAttributes(employeeID, barberServiceID, newStartDateTime, newEndDateTime, updatedStatus, appointment_notes.getText());
        try {
            appointmentService.updateAppointment(infoDTOReference.getId(), updateDTO);
            showToastNotification(anchor_pane, applicationContext, APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
            resetForm();
        } catch (ConstraintViolationException | InvalidAppointmentStartDateException |
                 DateTimeOutsideServiceHoursException | EmployeeNotAvailableException exception) {

            notifyValidationFailure(anchor_pane, exception, VALIDATION_ERROR_TITLE, APPOINTMENT_EDITION_VALIDATION_FAILED);
        }
    }

    private AppointmentUpdateDTO buildDTOFromAttributes(Long employeeID, Long barberServiceID, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, AppointmentStatus updatedStatus, String appointmentNotes) {
        return AppointmentUpdateDTO.builder()
                .newEmployeeID(employeeID)
                .newBarberserviceID(barberServiceID)
                .newStartDateTime(newStartDateTime)
                .newEndDateTime(newEndDateTime)
                .newStatus(updatedStatus)
                .optionalNotes(appointmentNotes)
                .build();
    }

    public void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.APPOINTMENTS, anchor_pane, applicationContext),
                restore_values_button, this::resetForm,
                save_button, this::updateAppointment
        );
        configureRunnableMaps(map);
    }

    private void configureBarberServiceSelection() {
        barber_service_selector.valueProperty().addListener((_, _, barberServiceSelected) -> onBarberServiceSelected(barberServiceSelected));
    }

    private void configureEmployeeSelection() {
        employee_selector.valueProperty().addListener((_, _, employeeSelected) -> onEmployeeSelected(employeeSelected));
    }

    private void configureTimeSelectors() {
        setHourAndMinuteSelectors(hour_selector, minute_selector);
        date_selector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
        hour_selector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
        minute_selector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
    }

    private void onBarberServiceSelected(BarberServiceInfoDTO barberServiceSelected) {
        if (barberServiceSelected == null) return;
        barberServiceReference = barberServiceSelected;
        String price = parseNumberValueToText(barberServiceSelected.getPrice());
        setTextOnLabel(summary_service, barberServiceSelected.getName());
        setTextOnLabel(summary_price, CURRENCY_STRING_ARG + price);
        setNodeAsVisible(service_selection_container);
        setNodeAsVisible(appointment_summary_card);
    }

    private void onEmployeeSelected(EmployeeInfoDTO employeeSelected) {
        if (employeeSelected == null) return;
        employeeReference = employeeSelected;
        String employeeFullName = employeeSelected.getFirstName() + " " + employeeSelected.getLastName();
        setTextOnLabel(summary_employee, employeeFullName);
        setNodeAsVisible(appointment_summary_card);
    }

    private void updateDateTimeSummary() {
        updateDatetimeSummary(summary_datetime, date_selector, hour_selector, minute_selector);
        setNodeAsVisible(appointment_summary_card);
    }

    @Override
    protected void resetReferenceObjects() {

        this.employeeReference = null;
        this.barberServiceReference = null;
    }

    @Override
    protected ComboBox<?>[] getComboboxesToReset() {
        return new ComboBox<?>[]{
                employee_selector,
                barber_service_selector,
                status_selector,
                hour_selector,
                minute_selector
        };
    }

    @Override
    protected DatePicker getDatePickerToReset() {

        return date_selector;
    }

    @Override
    protected void restoreNotes() {

        setBlankTextfield(appointment_notes);
    }

    @Override
    protected void toggleContainersVisibility() {

        setNodeAsNotVisible(service_selection_container, appointment_summary_card);
    }
}