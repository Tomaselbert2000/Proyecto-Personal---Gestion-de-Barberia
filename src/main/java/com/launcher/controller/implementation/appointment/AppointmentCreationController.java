/**
 * Controlador para la creación de citas. Se encarga de gestionar la lógica de negocio relacionada con la creación de citas,
 * incluyendo la validación de datos, la interacción con el servicio de citas y la redirección de vistas.
 */
package com.launcher.controller.implementation.appointment;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.barbershopservice.BarberServiceInfoDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import com.exceptions.common.EmployeeNotAvailableException;
import com.launcher.controller.interfaces.AppointmentController;
import com.launcher.controller.interfaces.CreationController;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import jakarta.validation.ConstraintViolationException;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.launcher.constants.ControllerConstants.AppointmentControllerConstants.APPOINTMENT_DEFAULT_DURATION_IN_MINUTES;
import static com.launcher.constants.ControllerConstants.AppointmentControllerConstants.DATETIME_SUMMARY_FORMAT;
import static com.launcher.constants.PromptTexts.AppointmentPromptText.APPOINTMENT_CLIENT_NAME;
import static com.launcher.constants.PromptTexts.AppointmentPromptText.APPOINTMENT_NOTES;
import static com.launcher.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.launcher.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.launcher.constants.StringResource.ToastNotificationMessage.APPOINTMENT_CREATION_NOTIFICATION_MESSAGE;
import static com.launcher.constants.StringResource.ToastNotificationMessage.APPOINTMENT_DATA_INCOMPLETE_NOTIFICATION_MESSAGE;
import static com.launcher.constants.StringResource.ValidationErrorMessage.APPOINTMENT_CREATION_VALIDATION_FAILED;
import static com.launcher.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.launcher.controller.helper.ComboBoxHelper.loadGenericTypeListOnComboBox;
import static com.launcher.controller.helper.ContainerManager.getCurrentWindow;
import static com.launcher.controller.helper.ListViewHelper.cleanListView;
import static com.launcher.controller.helper.ListViewHelper.loadItemsOnListView;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.getConstraintViolationsList;
import static com.launcher.controller.helper.ValidationFormatter.parseNumberValueToText;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;
import static com.launcher.controller.helper.VisibilityHelper.setNodeAsNotVisible;
import static com.launcher.controller.helper.VisibilityHelper.setNodeAsVisible;

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

    /**
     * Inicializa el controlador, configurando los elementos de la interfaz y cargando los datos iniciales.
     */
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

    /**
     * Configura el comportamiento de la búsqueda en tiempo real del cliente.
     */
    private void configureClientLiveSearch() {
        client_search_field.textProperty().addListener((_, _, _) -> executeClientLiveSearchByName());
        client_result_list.getSelectionModel().selectionProperty().addListener((MapChangeListener<? super Integer, ? super ClientInfoDTO>) change -> onClientSelected(change.getValueAdded()));
    }

    /**
     * Ejecuta la búsqueda en tiempo real del cliente por nombre.
     */
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

    /**
     * Maneja la selección de un cliente.
     *
     * @param selectedClient El cliente seleccionado.
     */
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

    /**
     * Obtiene un mapa de etiquetas y sus valores correspondientes para un cliente seleccionado.
     *
     * @param selectedClient   El cliente seleccionado.
     * @param firstNameInitial La inicial del nombre del cliente.
     * @return Un mapa de etiquetas y sus valores correspondientes.
     */
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

    /**
     * Registra una nueva cita.
     */
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
                showWindowAlert(VALIDATION_ERROR_TITLE, APPOINTMENT_CREATION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchor_pane));
            } else {
                errorMessage = exception.getMessage();
                showToastNotification(anchor_pane, applicationContext, errorMessage, ToastNotificationType.FAILED);
            }
        }
    }

    /**
     * Construye un DTO de cita a partir de los atributos proporcionados.
     *
     * @param clientID        El ID del cliente.
     * @param employeeID      El ID del empleado.
     * @param barberServiceID El ID del servicio de barbero.
     * @param startDatetime   La fecha y hora de inicio de la cita.
     * @param endDatetime     La fecha y hora de finalización de la cita.
     * @param optionalNotes   Notas adicionales para la cita.
     * @return Un DTO de cita.
     */
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

    /**
     * Verifica y actualiza la visibilidad del resumen de la cita.
     */
    private void checkAndToggleSummary() {
        if (isFormComplete()) {
            setNodeAsVisible(summary_card_vbox);
        } else {
            setNodeAsNotVisible(summary_card_vbox);
        }
    }

    /**
     * Verifica si el formulario está completo.
     *
     * @return true si el formulario está completo, false en caso contrario.
     */
    private boolean isFormComplete() {
        return clientReference != null && employeeReference != null && barberServiceReference != null && date_selector.getValue() != null && hour_selector.getValue() != null && minute_selector.getValue() != null;
    }

    /**
     * Actualiza el resumen de la fecha y hora de la cita.
     */
    private void onDateTimeChanged() {
        updateDateTimeSummary();
        checkAndToggleSummary();
    }

    /**
     * Restablece la selección del cliente.
     */
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
        setHourAndMinuteSelectors(hour_selector, minute_selector);
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