/**
 * Controlador para la edición de citas. Se encarga de gestionar la lógica de negocio relacionada con la edición de citas,
 * incluyendo la validación de datos, la interacción con el servicio de citas y la redirección de vistas.
 */
package com.launcher.controller.implementation.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.barbershopservice.BarberServiceInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.AppointmentStatus;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import com.exceptions.common.EmployeeNotAvailableException;
import com.launcher.controller.interfaces.AppointmentController;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.launcher.constants.ControllerConstants.AppointmentControllerConstants.APPOINTMENT_DEFAULT_DURATION_IN_MINUTES;
import static com.launcher.constants.ControllerConstants.AppointmentControllerConstants.DATETIME_SUMMARY_FORMAT;
import static com.launcher.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.launcher.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.launcher.constants.StringResource.ToastNotificationMessage.APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE;
import static com.launcher.constants.StringResource.ValidationErrorMessage.APPOINTMENT_EDITION_VALIDATION_FAILED;
import static com.launcher.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.launcher.controller.helper.ComboBoxHelper.loadGenericTypeListOnComboBox;
import static com.launcher.controller.helper.ContainerManager.getCurrentWindow;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.*;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;
import static com.launcher.controller.helper.VisibilityHelper.setNodeAsNotVisible;
import static com.launcher.controller.helper.VisibilityHelper.setNodeAsVisible;

@Component
@RequiredArgsConstructor
@Getter
public class AppointmentEditionController implements AppointmentController {

    private final ApplicationContext applicationContext;
    private final AppointmentService appointmentService;

    private AppointmentInfoDTO infoDTOReference;
    private BarberServiceInfoDTO barberServiceReference;
    private EmployeeInfoDTO employeeReference;

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

    /**
     * Inicializa el controlador con la información de la cita a editar.
     *
     * @param infoDTO La información de la cita a editar.
     */
    @FXML
    public void initialize(AppointmentInfoDTO infoDTO) {
        this.infoDTOReference = infoDTO;
        loadAppointmentDataForEdition(infoDTO);
        configureButtonActions();
        configureBarberServiceSelection();
        configureEmployeeSelection();
        configureTimeSelectors();
    }

    /**
     * Carga los datos de la cita en el formulario para su edición.
     *
     * @param infoDTO La información de la cita a editar.
     */
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

    /**
     * Carga los estados disponibles para la cita en el selector de estados.
     *
     * @param currentStatus El estado actual de la cita.
     */
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

    /**
     * Actualiza la cita con los datos ingresados en el formulario.
     */
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
            String errorMessage;
            if (exception instanceof ConstraintViolationException) {
                errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);
                showWindowAlert(VALIDATION_ERROR_TITLE, APPOINTMENT_EDITION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchor_pane));
            } else {
                errorMessage = exception.getMessage();
                showToastNotification(anchor_pane, applicationContext, errorMessage, ToastNotificationType.FAILED);
            }
        }
    }

    /**
     * Construye un DTO de actualización de cita con los datos proporcionados.
     *
     * @param employeeID       El ID del empleado seleccionado.
     * @param barberServiceID  El ID del servicio de barbero seleccionado.
     * @param newStartDateTime La nueva fecha y hora de inicio de la cita.
     * @param newEndDateTime   La nueva fecha y hora de fin de la cita.
     * @param updatedStatus    El nuevo estado de la cita.
     * @param appointmentNotes Las notas adicionales de la cita.
     * @return Un DTO de actualización de cita con los datos proporcionados.
     */
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

    @Override
    public void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.APPOINTMENTS, anchor_pane, applicationContext),
                restore_values_button, this::resetForm,
                save_button, this::updateAppointment
        );
        configureRunnableMaps(map);
    }

    @Override
    public void configureBarberServiceSelection() {
        barber_service_selector.valueProperty().addListener((_, _, barberServiceSelected) -> onBarberServiceSelected(barberServiceSelected));
    }

    @Override
    public void configureEmployeeSelection() {
        employee_selector.valueProperty().addListener((_, _, employeeSelected) -> onEmployeeSelected(employeeSelected));
    }

    @Override
    public void configureTimeSelectors() {
        setHourAndMinuteSelectors(hour_selector, minute_selector);
        date_selector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
        hour_selector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
        minute_selector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
    }

    @Override
    public void onBarberServiceSelected(BarberServiceInfoDTO barberServiceSelected) {
        if (barberServiceSelected == null) return;
        barberServiceReference = barberServiceSelected;
        String price = parseNumberValueToText(barberServiceSelected.getPrice());
        setTextOnLabel(summary_service, barberServiceSelected.getName());
        setTextOnLabel(summary_price, CURRENCY_STRING_ARG + price);
        setNodeAsVisible(service_selection_container);
        setNodeAsVisible(appointment_summary_card);
    }

    @Override
    public void onEmployeeSelected(EmployeeInfoDTO employeeSelected) {
        if (employeeSelected == null) return;
        employeeReference = employeeSelected;
        String employeeFullName = employeeSelected.getFirstName() + " " + employeeSelected.getLastName();
        setTextOnLabel(summary_employee, employeeFullName);
        setNodeAsVisible(appointment_summary_card);
    }

    @Override
    public void updateDateTimeSummary() {
        LocalDate date = date_selector.getValue();
        LocalTime hour = hour_selector.getValue();
        LocalTime minute = minute_selector.getValue();
        if (date != null && hour != null && minute != null) {
            String dateTimeSummary = String.format(DATETIME_SUMMARY_FORMAT, date, hour.getHour(), minute.getMinute());
            setTextOnLabel(summary_datetime, dateTimeSummary);
        }
        setNodeAsVisible(appointment_summary_card);
    }

    @Override
    public void setReferenceObjectsAsNull() {
        this.employeeReference = null;
        this.barberServiceReference = null;
    }

    @Override
    public void resetForm() {
        cleanComboBoxes(employee_selector, barber_service_selector, status_selector, hour_selector, minute_selector);
        cleanDatePicker(date_selector);
        setTextOnTextfield(appointment_notes, infoDTOReference.getOptionalNotes());
        setNodeAsNotVisible(service_selection_container);
        setReferenceObjectsAsNull();
        setNodeAsNotVisible(appointment_summary_card);
    }
}