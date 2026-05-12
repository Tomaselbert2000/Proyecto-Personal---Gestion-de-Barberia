package com.barbershop.launcher.controller.implementation.appointment;

import com.barbershop.dto.appointment.AppointmentCreationDTO;
import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.dto.client.ClientInfoDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.barbershop.exceptions.appointment.InvalidAppointmentStartDateException;
import com.barbershop.exceptions.common.EmployeeNotAvailableException;
import com.barbershop.launcher.controller.interfaces.AppointmentController;
import com.barbershop.launcher.controller.interfaces.CreationController;
import com.barbershop.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.*;
import jakarta.validation.ConstraintViolationException;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.messages.GenericStrings.CURRENCY_STRING_ARG;
import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.APPOINTMENT_CREATION_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.APPOINTMENT_DATA_INCOMPLETE_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.APPOINTMENT_CREATION_VALIDATION_FAILED;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.barbershop.launcher.constants.ui.prompt_text.AppointmentPromptText.APPOINTMENT_CLIENT_NAME;
import static com.barbershop.launcher.constants.ui.prompt_text.AppointmentPromptText.APPOINTMENT_NOTES;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.*;
import static com.barbershop.launcher.controller.helper.ListViewHelper.cleanListView;
import static com.barbershop.launcher.controller.helper.ListViewHelper.loadItemsOnListView;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.ViewRedirectionHelper.redirectToView;
import static com.barbershop.launcher.controller.helper.VisibilityHelper.setNodeAsNotVisible;
import static com.barbershop.launcher.controller.helper.VisibilityHelper.setNodeAsVisible;
import static com.barbershop.launcher.controller.implementation.appointment.AppointmentControllerConstant.APPOINTMENT_DEFAULT_DURATION_IN_MINUTES;
import static com.barbershop.launcher.controller.implementation.appointment.AppointmentControllerConstant.DATETIME_SUMMARY_FORMAT;

@Component
@RequiredArgsConstructor
public class AppointmentCreationController implements CreationController, AppointmentController {

    private final AppointmentService appointmentService;
    private final ApplicationContext applicationContext;

    private ClientInfoDTO clientReference;
    private BarberServiceInfoDTO barberServiceReference;
    private EmployeeInfoDTO employeeReference;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton back_button;

    @FXML
    private MFXTextField client_search_field;

    @FXML
    private MFXButton create_client_button;

    @FXML
    private Label client_initials;

    @FXML
    private Label client_name;

    @FXML
    private MFXListView<ClientInfoDTO> client_result_list;

    @FXML
    private VBox selected_client_card_vbox;

    @FXML
    private Label national_id_card_number;

    @FXML
    private MFXButton change_client_button;

    @FXML
    private MFXComboBox<BarberServiceInfoDTO> barberservice_selector;

    @FXML
    private MFXComboBox<EmployeeInfoDTO> employee_selector;

    @FXML
    private VBox service_selection_container;

    @FXML
    private Label service_price;

    @FXML
    private MFXDatePicker date_selector;

    @FXML
    private MFXComboBox<LocalTime> hour_selector;

    @FXML
    private MFXComboBox<LocalTime> minute_selector;

    @FXML
    private MFXTextField appointment_notes;

    @FXML
    private Label summary_client;

    @FXML
    private Label summary_service;

    @FXML
    private Label summary_employee;

    @FXML
    private Label summary_datetime;

    @FXML
    private Label summary_price;

    @FXML
    private VBox summary_card_vbox;

    @FXML
    private MFXButton reset_form_button;

    @FXML
    private MFXButton save_button;

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

        loadDTOsOnComboBox(barberservice_selector, catalog);
        loadDTOsOnComboBox(employee_selector, employees);

        setNodeAsNotVisible(client_result_list, selected_client_card_vbox);
    }

    private void configureClientLiveSearch() {

        client_search_field.textProperty().addListener((_, _, _) -> executeClientLiveSearchByName());
        client_result_list.getSelectionModel().selectionProperty().addListener((MapChangeListener<? super Integer, ? super ClientInfoDTO>) change -> {

                    if (change.wasAdded()) {

                        onClientSelected(change.getValueAdded());
                    }
                }
        );
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
        String lastNameInitial = String.valueOf(selectedClient.getLastName().charAt(0));
        String fullClientName = selectedClient.getFirstName() + " " + selectedClient.getLastName();

        setTextOnLabel(client_name, fullClientName);
        setTextOnLabel(national_id_card_number, selectedClient.getNationalIdentityCardNumber());
        setTextOnLabel(client_initials, firstNameInitial + lastNameInitial);
        setTextOnLabel(summary_client, fullClientName);

        setNodeAsVisible(client_name, national_id_card_number, selected_client_card_vbox);
        setNodeAsNotVisible(client_search_field, client_result_list);
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

            String errorMessage;

            if (exception instanceof ConstraintViolationException) {

                errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);
                showErrorAlert(VALIDATION_ERROR_TITLE, APPOINTMENT_CREATION_VALIDATION_FAILED, errorMessage);

            } else {

                errorMessage = exception.getMessage();
                showToastNotification(anchor_pane, applicationContext, errorMessage, ToastNotificationType.FAILED);
            }
        }
    }

    private AppointmentCreationDTO buildDTOFromAttributes(
            Long clientID,
            Long employeeID,
            Long barberServiceID,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            String optionalNotes
    ) {
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

        return clientReference != null
                && employeeReference != null
                && barberServiceReference != null
                && date_selector.getValue() != null
                && hour_selector.getValue() != null
                && minute_selector.getValue() != null;
    }

    private void onDateTimeChanged() {

        updateDateTimeSummary();
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

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.APPOINTMENTS, anchor_pane, applicationContext),
                create_client_button, () -> redirectToView(ViewRedirection.CLIENT_CREATION, anchor_pane, applicationContext),
                change_client_button, this::resetClientSelection,
                reset_form_button, this::resetForm,
                save_button, this::registerNewAppointment
        );

        configureRunnableMaps(map);
    }

    @Override
    public void configureBarberServiceSelection() {

        barberservice_selector.valueProperty().addListener((_, _, barberServiceSelected) -> onBarberServiceSelected(barberServiceSelected));
    }

    @Override
    public void configureEmployeeSelection() {

        employee_selector.valueProperty().addListener((_, _, employeeSelected) -> onEmployeeSelected(employeeSelected));
    }

    @Override
    public void configureTimeSelectors() {

        setTimeSelectors(hour_selector, minute_selector);

        date_selector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
        hour_selector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
        minute_selector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
    }

    @Override
    public void onBarberServiceSelected(BarberServiceInfoDTO barberServiceSelected) {

        if (barberServiceSelected == null) return;

        barberServiceReference = barberServiceSelected;

        checkAndToggleSummary();

        String price = parseNumberValueToText(barberServiceSelected.getPrice());

        setTextOnLabel(service_price, CURRENCY_STRING_ARG + price);
        setTextOnLabel(summary_service, barberServiceSelected.getName());
        setTextOnLabel(summary_price, CURRENCY_STRING_ARG + price);

        setNodeAsVisible(service_selection_container);
    }

    @Override
    public void onEmployeeSelected(EmployeeInfoDTO employeeSelected) {

        if (employeeSelected == null) return;

        employeeReference = employeeSelected;

        checkAndToggleSummary();

        String employeeFullName = employeeSelected.getFirstName() + " " + employeeSelected.getLastName();

        setTextOnLabel(summary_employee, employeeFullName);
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
    }

    @Override
    public void setReferenceObjectsAsNull() {

        this.clientReference = null;
        this.barberServiceReference = null;
        this.employeeReference = null;
    }

    @Override
    public void resetForm() {

        setBlankTextfield(client_search_field);

        cleanListView(client_result_list);

        setNodeAsNotVisible(selected_client_card_vbox);
        setNodeAsNotVisible(service_selection_container);
        setNodeAsVisible(client_search_field);

        setReferenceObjectsAsNull();

        cleanComboBoxes(barberservice_selector, employee_selector, hour_selector, minute_selector);

        cleanDatePicker(date_selector);

        setBlankTextfield(appointment_notes);

        checkAndToggleSummary();
    }

    @Override
    public void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                client_search_field, APPOINTMENT_CLIENT_NAME,
                appointment_notes, APPOINTMENT_NOTES
        );

        setPromptTextOnMap(map);
    }
}
