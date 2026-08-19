package com.presentation.controller.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.AppointmentStatus;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
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

import static com.enums.AppointmentStatus.*;
import static com.enums.ViewRedirection.APPOINTMENTS;
import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.APPOINTMENT_DEFAULT_DURATION_IN_MINUTES;
import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.DATETIME_SUMMARY_FORMAT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.APPOINTMENT_EDITION_VALIDATION_FAILED;
import static com.presentation.support.control.AppointmentDateTimeSummaryHelper.updateDatetimeSummary;
import static com.presentation.support.control.ComboBoxHelper.loadGenericTypeListOnComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.format.PersonNameFormatter.fullName;
import static com.presentation.support.format.PriceFormatter.formatPriceAsString;
import com.presentation.support.view.ViewRedirectionHelper;
import static com.presentation.support.view.VisibilityHelper.setNodeAsNotVisible;
import static com.presentation.support.view.VisibilityHelper.setNodeAsVisible;

@Component
@Getter
public class AppointmentEditionController extends BaseCrudFormController<AppointmentUpdateDTO, AppointmentInfoDTO> {

    private BarberServiceInfoDTO barberServiceReference;
    private EmployeeInfoDTO employeeReference;

    private final AppointmentService appointmentService;
    private final ViewRedirectionHelper viewRedirectionHelper;

    public AppointmentEditionController(ApplicationContext applicationContext, AppointmentService appointmentService, ViewRedirectionHelper viewRedirectionHelper) {
        super(applicationContext);
        this.appointmentService = appointmentService;
        this.viewRedirectionHelper = viewRedirectionHelper;
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

        infoDTOReference = infoDTO;

        loadAppointmentDataForEdition(infoDTO);

        configureButtonActions();

        configureBarberServiceSelection();

        configureEmployeeSelection();

        configureTimeSelectors();
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(backButton, () -> viewRedirectionHelper.redirectToView(APPOINTMENTS, getAnchorPane(), getApplicationContext())),
                Map.entry(restoreValuesButton, this::resetForm),
                Map.entry(saveButton, this::saveEntity)
        );
        configureRunnableMaps(map);
    }

    @Override
    protected void configurePromptTexts() {
    }

    @Override
    protected void persistEntity(AppointmentUpdateDTO dto) {

        appointmentService.updateAppointment(infoDTOReference.getId(), dto);
    }

    @Override
    protected String getSuccessMessage() {

        return APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return APPOINTMENT_EDITION_VALIDATION_FAILED;
    }

    @Override
    protected AppointmentUpdateDTO buildDTO() {

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

        return AppointmentUpdateDTO.builder()
                .newEmployeeID(employeeID)
                .newBarberserviceID(barberServiceID)
                .newStartDateTime(newStartDateTime)
                .newEndDateTime(newEndDateTime)
                .newStatus(updatedStatus)
                .optionalNotes(appointmentNotes.getText())
                .build();
    }

    @Override
    protected void resetForm() {

        toggleContainersVisibility();

        setBlankTextfield(appointmentNotes);

        this.employeeReference = null;
        this.barberServiceReference = null;

        loadAppointmentDataForEdition(infoDTOReference);
    }

    private void loadAppointmentDataForEdition(AppointmentInfoDTO infoDTO) {

        String clientFullName = fullName(infoDTO.getClientFirstName(), infoDTO.getClientLastName());
        String employeeFullName = fullName(infoDTO.getEmployeeFirstName(), infoDTO.getEmployeeLastName());

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

        if (currentStatus == CANCELADO || currentStatus == FINALIZADO) {

            disableComboBox(statusSelector);

            return;
        }

        for (AppointmentStatus status : AppointmentStatus.values()) {

            if (status == TODOS) continue;

            if (currentStatus == status) continue;

            if (currentStatus == REPROGRAMADO && status == PROGRAMADO) continue;

            allowedStatuses.add(status);
        }

        statusSelector.getItems().addAll(allowedStatuses);

        setStringConverter(statusSelector, statusSelector.getItems().getFirst());
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

    private void toggleContainersVisibility() {

        setNodeAsNotVisible(serviceSelectionContainer, appointmentSummaryCard);
    }
}