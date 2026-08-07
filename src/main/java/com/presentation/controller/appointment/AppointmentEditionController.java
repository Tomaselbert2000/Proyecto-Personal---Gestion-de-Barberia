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
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.APPOINTMENT_EDITION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.AppointmentDateTimeSummaryHelper.updateDatetimeSummary;
import static com.presentation.support.control.ComboBoxHelper.loadGenericTypeListOnComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.format.PriceFormatter.formatPriceAsString;
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
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            restoreValuesButton,
            saveButton;

    @FXML
    private Label
            currentClientName,
            currentServiceName,
            currentEmployeeName,
            servicePrice,
            currentStartDateTime,
            currentStatusLabel,
            summaryClient,
            summaryService,
            summaryEmployee,
            summaryDateTime,
            summaryPrice;

    @FXML
    private HBox currentStatusContainer;

    @FXML
    private ComboBox<BarberServiceInfoDTO> barberServiceSelector;

    @FXML
    private ComboBox<EmployeeInfoDTO> employeeSelector;

    @FXML
    private VBox
            serviceSelectionContainer,
            appointmentSummaryCard;

    @FXML
    private DatePicker dateSelector;

    @FXML
    private ComboBox<LocalTime>
            hourSelector,
            minuteSelector;

    @FXML
    private TextField appointmentNotes;

    @FXML
    private ComboBox<AppointmentStatus> statusSelector;

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
                Map.entry(currentClientName, clientFullName),
                Map.entry(currentEmployeeName, employeeFullName),
                Map.entry(currentServiceName, infoDTO.getServiceName()),
                Map.entry(currentStartDateTime, appointmentDateAsString),
                Map.entry(currentStatusLabel, infoDTO.getCurrentStatus().getDisplayName()),
                Map.entry(servicePrice, parseNumberValueToText(infoDTO.getServicePrice()))
        );
        setTextsOnLabelMap(map);
        loadGenericTypeListOnComboBox(barberServiceSelector, barberServices);
        loadGenericTypeListOnComboBox(employeeSelector, employees);
        loadAvailableStatuses(infoDTO.getCurrentStatus());
        setTextOnLabel(summaryClient, clientFullName);
        setTextOnTextfield(appointmentNotes, infoDTO.getOptionalNotes());
    }

    private void loadAvailableStatuses(AppointmentStatus currentStatus) {
        List<AppointmentStatus> allowedStatuses = new ArrayList<>();
        if (currentStatus == AppointmentStatus.CANCELADO || currentStatus == AppointmentStatus.FINALIZADO) {
            disableComboBox(statusSelector);
            return;
        }
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status == AppointmentStatus.TODOS) continue;
            if (currentStatus == status) continue;
            if (currentStatus == AppointmentStatus.REPROGRAMADO && status == AppointmentStatus.PROGRAMADO) continue;
            allowedStatuses.add(status);
        }
        statusSelector.getItems().addAll(allowedStatuses);
        setStringConverter(statusSelector, statusSelector.getItems().getFirst());
    }

    private void updateAppointment() {
        Long employeeID = null;
        Long barberServiceID = null;
        LocalDateTime newStartDateTime = null;
        LocalDateTime newEndDateTime = null;
        AppointmentStatus updatedStatus = null;
        if (employeeSelector.getValue() != null) employeeID = employeeSelector.getValue().getId();
        if (barberServiceSelector.getValue() != null)
            barberServiceID = barberServiceSelector.getValue().getBarberServiceId();
        if (dateSelector.getValue() != null && hourSelector.getValue() != null && minuteSelector.getValue() != null) {
            newStartDateTime = LocalDateTime.of(dateSelector.getValue(), LocalTime.of(hourSelector.getValue().getHour(), minuteSelector.getValue().getMinute()));
            newEndDateTime = newStartDateTime.plusMinutes(APPOINTMENT_DEFAULT_DURATION_IN_MINUTES);
        }
        if (statusSelector.getValue() != null) updatedStatus = statusSelector.getValue();
        AppointmentUpdateDTO updateDTO = buildDTOFromAttributes(employeeID, barberServiceID, newStartDateTime, newEndDateTime, updatedStatus, appointmentNotes.getText());
        try {
            appointmentService.updateAppointment(infoDTOReference.getId(), updateDTO);
            showToastNotification(anchorPane, applicationContext, APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
            resetForm();
        } catch (ConstraintViolationException | InvalidAppointmentStartDateException |
                 DateTimeOutsideServiceHoursException | EmployeeNotAvailableException exception) {

            notifyValidationFailure(anchorPane, exception, VALIDATION_ERROR_TITLE, APPOINTMENT_EDITION_VALIDATION_FAILED);
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
                backButton, () -> redirectToView(ViewRedirection.APPOINTMENTS, anchorPane, applicationContext),
                restoreValuesButton, this::resetForm,
                saveButton, this::updateAppointment
        );
        configureRunnableMaps(map);
    }

    private void configureBarberServiceSelection() {
        barberServiceSelector.valueProperty().addListener((_, _, barberServiceSelected) -> onBarberServiceSelected(barberServiceSelected));
    }

    private void configureEmployeeSelection() {
        employeeSelector.valueProperty().addListener((_, _, employeeSelected) -> onEmployeeSelected(employeeSelected));
    }

    private void configureTimeSelectors() {
        setHourAndMinuteSelectors(hourSelector, minuteSelector);
        dateSelector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
        hourSelector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
        minuteSelector.valueProperty().addListener((_, _, _) -> updateDateTimeSummary());
    }

    private void onBarberServiceSelected(BarberServiceInfoDTO barberServiceSelected) {

        if (barberServiceSelected == null) return;

        barberServiceReference = barberServiceSelected;

        setTextOnLabel(summaryService, barberServiceSelected.getName());
        setTextOnLabel(summaryPrice, formatPriceAsString(barberServiceSelected.getPrice()));

        setNodeAsVisible(serviceSelectionContainer);
        setNodeAsVisible(appointmentSummaryCard);
    }

    private void onEmployeeSelected(EmployeeInfoDTO employeeSelected) {
        if (employeeSelected == null) return;
        employeeReference = employeeSelected;
        String employeeFullName = employeeSelected.getFirstName() + " " + employeeSelected.getLastName();
        setTextOnLabel(summaryEmployee, employeeFullName);
        setNodeAsVisible(appointmentSummaryCard);
    }

    private void updateDateTimeSummary() {
        updateDatetimeSummary(summaryDateTime, dateSelector, hourSelector, minuteSelector);
        setNodeAsVisible(appointmentSummaryCard);
    }

    @Override
    protected void resetReferenceObjects() {

        this.employeeReference = null;
        this.barberServiceReference = null;
    }

    @Override
    protected ComboBox<?>[] getComboboxesToReset() {
        return new ComboBox<?>[]{
                employeeSelector,
                barberServiceSelector,
                statusSelector,
                hourSelector,
                minuteSelector
        };
    }

    @Override
    protected DatePicker getDatePickerToReset() {

        return dateSelector;
    }

    @Override
    protected void restoreNotes() {

        setBlankTextfield(appointmentNotes);
    }

    @Override
    protected void toggleContainersVisibility() {

        setNodeAsNotVisible(serviceSelectionContainer, appointmentSummaryCard);
    }
}