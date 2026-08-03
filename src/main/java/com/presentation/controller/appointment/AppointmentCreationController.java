package com.presentation.controller.appointment;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import com.exceptions.common.EmployeeNotAvailableException;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import jakarta.validation.ConstraintViolationException;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.APPOINTMENT_DEFAULT_DURATION_IN_MINUTES;
import static com.presentation.constants.PromptTexts.AppointmentPromptText.APPOINTMENT_CLIENT_NAME;
import static com.presentation.constants.PromptTexts.AppointmentPromptText.APPOINTMENT_NOTES;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_CREATION_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_DATA_INCOMPLETE_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.APPOINTMENT_CREATION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.AppointmentDateTimeSummaryHelper.updateDatetimeSummary;
import static com.presentation.support.control.ComboBoxHelper.loadGenericTypeListOnComboBox;
import static com.presentation.support.control.ListViewHelper.cleanListView;
import static com.presentation.support.control.ListViewHelper.loadItemsOnListView;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.notification.ExceptionNotificationHandler.notifyValidationFailure;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;
import static com.presentation.support.view.VisibilityHelper.setNodeAsNotVisible;
import static com.presentation.support.view.VisibilityHelper.setNodeAsVisible;

@Component
public class AppointmentCreationController extends BaseAppointmentFormController {

    private ClientInfoDTO clientReference;
    private BarberServiceInfoDTO barberServiceReference;
    private EmployeeInfoDTO employeeReference;
    @FXML
    private AnchorPane anchor_pane;
    @FXML
    private MFXButton
            back_button,
            create_client_button,
            change_client_button,
            reset_form_button,
            save_button;
    @FXML
    private TextField
            client_search_field,
            appointment_notes;
    @FXML
    private Label
            client_initials,
            client_name,
            national_id_card_number,
            service_price,
            summary_client,
            summary_service,
            summary_employee,
            summary_datetime,
            summary_price;
    @FXML
    private MFXListView<ClientInfoDTO> client_result_list;
    @FXML
    private VBox
            selected_client_card_vbox,
            service_selection_container,
            summary_card_vbox;
    @FXML
    private ComboBox<BarberServiceInfoDTO> barberservice_selector;
    @FXML
    private ComboBox<EmployeeInfoDTO> employee_selector;
    @FXML
    private DatePicker date_selector;
    @FXML
    private ComboBox<LocalTime>
            hour_selector,
            minute_selector;

    public AppointmentCreationController(
            AppointmentService appointmentService,
            ApplicationContext applicationContext) {

        super(appointmentService, applicationContext);
    }

    @FXML
    public void initialize() {
        configurePromptTexts();
        configureButtonActions();
        configureClientLiveSearch();
        configureBarberServiceSelection();
        configureEmployeeSelection();
        configureTimeSelectors();

        List<BarberServiceInfoDTO> catalog = appointmentService.getBarberServicesFromServiceInstance();
        List<EmployeeInfoDTO> employees = appointmentService.getEmployeesFromServiceInstance();
        loadGenericTypeListOnComboBox(barberservice_selector, catalog);
        loadGenericTypeListOnComboBox(employee_selector, employees);

        setNodeAsNotVisible(client_result_list, selected_client_card_vbox);
    }

    private void configureClientLiveSearch() {
        client_search_field.textProperty().addListener((_, _, _) -> executeClientLiveSearchByName());
        client_result_list.getSelectionModel().selectionProperty().addListener((MapChangeListener<? super Integer, ? super ClientInfoDTO>) change -> onClientSelected(change.getValueAdded()));
    }

    private void executeClientLiveSearchByName() {
        if (client_search_field.getText().isBlank()) {
            cleanListView(client_result_list);
            setNodeAsNotVisible(client_result_list);
        } else {
            List<ClientInfoDTO> clients = appointmentService.clientLiveSearchByName(client_search_field.getText());
            loadItemsOnListView(client_result_list, clients);
            setNodeAsVisible(client_result_list);
        }
    }

    private void onClientSelected(ClientInfoDTO selectedClient) {
        if (selectedClient == null) return;
        clientReference = selectedClient;
        checkAndToggleSummary();
        String firstNameInitial = String.valueOf(selectedClient.getFirstName().charAt(0));
        Map<Label, String> labelMap = getLabelStringMap(selectedClient, firstNameInitial);
        setTextsOnLabelMap(labelMap);
        setNodeAsVisible(client_name, national_id_card_number, selected_client_card_vbox);
        setNodeAsNotVisible(client_search_field, client_result_list);
    }

    private @NonNull Map<Label, String> getLabelStringMap(ClientInfoDTO selectedClient, String firstNameInitial) {
        String lastNameInitial = String.valueOf(selectedClient.getLastName().charAt(0));
        String fullClientName = selectedClient.getFirstName() + " " + selectedClient.getLastName();
        return Map.ofEntries(
                Map.entry(client_name, fullClientName),
                Map.entry(national_id_card_number, selectedClient.getNationalIdentityCardNumber()),
                Map.entry(client_initials, firstNameInitial + lastNameInitial),
                Map.entry(summary_client, fullClientName)
        );
    }

    private void registerNewAppointment() {
        try {
            if (!isFormComplete()) {
                showToastNotification(anchor_pane, applicationContext, APPOINTMENT_DATA_INCOMPLETE_NOTIFICATION_MESSAGE, ToastNotificationType.FAILED);
                return;
            }
            Long clientID = clientReference.getId();
            Long employeeID = employeeReference.getId();
            Long barberServiceID = barberServiceReference.getBarberServiceId();
            LocalDate date = date_selector.getValue();
            LocalTime exactStartTime = LocalTime.of(hour_selector.getValue().getHour(), minute_selector.getValue().getMinute());
            LocalDateTime startDatetime = LocalDateTime.of(date, exactStartTime);
            LocalDateTime endDatetime = startDatetime.plusMinutes(APPOINTMENT_DEFAULT_DURATION_IN_MINUTES);
            String optionalNotes = appointment_notes.getText();
            AppointmentCreationDTO creationDTO = buildDTOFromAttributes(clientID, employeeID, barberServiceID, startDatetime, endDatetime, optionalNotes);
            appointmentService.registerNewAppointment(creationDTO);
            showToastNotification(anchor_pane, applicationContext, APPOINTMENT_CREATION_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
            resetForm();
        } catch (ConstraintViolationException | InvalidAppointmentStartDateException |
                 DateTimeOutsideServiceHoursException | EmployeeNotAvailableException exception) {

            notifyValidationFailure(anchor_pane, exception, VALIDATION_ERROR_TITLE, APPOINTMENT_CREATION_VALIDATION_FAILED);
        }
    }

    private AppointmentCreationDTO buildDTOFromAttributes(Long clientID, Long employeeID, Long barberServiceID, LocalDateTime startDatetime, LocalDateTime endDatetime, String optionalNotes) {
        return AppointmentCreationDTO.builder()
                .clientID(clientID)
                .employeeID(employeeID)
                .barberserviceID(barberServiceID)
                .startDateTime(startDatetime)
                .endDateTime(endDatetime)
                .optionalNotes(optionalNotes)
                .build();
    }

    private void checkAndToggleSummary() {
        if (isFormComplete()) {
            setNodeAsVisible(summary_card_vbox);
        } else {
            setNodeAsNotVisible(summary_card_vbox);
        }
    }

    private boolean isFormComplete() {
        return clientReference != null && employeeReference != null && barberServiceReference != null && date_selector.getValue() != null && hour_selector.getValue() != null && minute_selector.getValue() != null;
    }

    private void onDateTimeChanged() {
        updateDatetimeSummary(summary_datetime, date_selector, hour_selector, minute_selector);
        checkAndToggleSummary();
    }

    private void resetClientSelection() {
        setBlankTextfield(client_search_field);
        cleanListView(client_result_list);
        setNodeAsNotVisible(selected_client_card_vbox);
        setNodeAsVisible(client_search_field);
        this.clientReference = null;
        checkAndToggleSummary();
    }

    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.APPOINTMENTS, anchor_pane, applicationContext),
                create_client_button, () -> redirectToView(ViewRedirection.CLIENT_CREATION, anchor_pane, applicationContext),
                change_client_button, this::resetClientSelection,
                reset_form_button, this::resetForm,
                save_button, this::registerNewAppointment
        );
        configureRunnableMaps(map);
    }

    private void configureBarberServiceSelection() {
        barberservice_selector.valueProperty().addListener((_, _, barberServiceSelected) -> onBarberServiceSelected(barberServiceSelected));
    }

    private void configureEmployeeSelection() {
        employee_selector.valueProperty().addListener((_, _, employeeSelected) -> onEmployeeSelected(employeeSelected));
    }

    private void configureTimeSelectors() {

        setHourAndMinuteSelectors(hour_selector, minute_selector);

        date_selector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
        hour_selector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
        minute_selector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
    }

    private void onBarberServiceSelected(BarberServiceInfoDTO barberServiceSelected) {

        if (barberServiceSelected == null) return;

        barberServiceReference = barberServiceSelected;

        checkAndToggleSummary();

        String price = parseNumberValueToText(barberServiceSelected.getPrice());

        setTextOnLabel(service_price, CURRENCY_STRING_ARG + price);
        setTextOnLabel(summary_service, barberServiceSelected.getName());
        setTextOnLabel(summary_price, CURRENCY_STRING_ARG + price);

        setNodeAsVisible(service_selection_container);
    }

    private void onEmployeeSelected(EmployeeInfoDTO employeeSelected) {

        if (employeeSelected == null) return;

        employeeReference = employeeSelected;

        checkAndToggleSummary();

        String employeeFullName = employeeSelected.getFirstName() + " " + employeeSelected.getLastName();

        setTextOnLabel(summary_employee, employeeFullName);
    }

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                client_search_field, APPOINTMENT_CLIENT_NAME,
                appointment_notes, APPOINTMENT_NOTES
        );
        setPromptTextOnMap(map);
    }

    @Override
    protected void resetReferenceObjects() {

        this.clientReference = null;
        this.barberServiceReference = null;
        this.employeeReference = null;
    }

    @Override
    protected ComboBox<?>[] getComboboxesToReset() {

        return new ComboBox<?>[]{
                barberservice_selector,
                employee_selector,
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

        setBlankTextfield(client_search_field);
        cleanListView(client_result_list);
        setNodeAsNotVisible(selected_client_card_vbox, service_selection_container);
        setNodeAsVisible(client_search_field);
    }
}