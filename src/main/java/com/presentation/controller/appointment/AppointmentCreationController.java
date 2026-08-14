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

import static com.presentation.concurrency.ConcurrencyManager.executeVoidAsyncTask;
import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.APPOINTMENT_DEFAULT_DURATION_IN_MINUTES;
import static com.presentation.constants.PromptTexts.AppointmentPromptText.APPOINTMENT_CLIENT_NAME;
import static com.presentation.constants.PromptTexts.AppointmentPromptText.APPOINTMENT_NOTES;
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_CREATION_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_DATA_INCOMPLETE_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.APPOINTMENT_CREATION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.AppointmentCatalogLoader.loadCatalog;
import static com.presentation.support.control.AppointmentDateTimeSummaryHelper.updateDatetimeSummary;
import static com.presentation.support.control.ListViewHelper.cleanListView;
import static com.presentation.support.control.ListViewHelper.loadItemsOnListView;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.format.PersonNameFormatter.fullName;
import static com.presentation.support.format.PersonNameFormatter.initials;
import static com.presentation.support.format.PriceFormatter.formatPriceAsString;
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
    private AnchorPane anchorPane;
    @FXML
    private MFXButton
            backButton,
            createClientButton,
            changeClientButton,
            resetFormButton,
            saveButton;
    @FXML
    private TextField
            clientSearchField,
            appointmentNotes;
    @FXML
    private Label
            clientInitials,
            clientName,
            nationalIdCardNumber,
            servicePrice,
            summaryClient,
            summaryService,
            summaryEmployee,
            summaryDatetime,
            summaryPrice;
    @FXML
    private MFXListView<ClientInfoDTO> clientResultList;
    @FXML
    private VBox
            selectedClientCardVbox,
            serviceSelectionContainer,
            summaryCardVbox;
    @FXML
    private ComboBox<BarberServiceInfoDTO> barberServiceSelector;
    @FXML
    private ComboBox<EmployeeInfoDTO> employeeSelector;
    @FXML
    private DatePicker dateSelector;
    @FXML
    private ComboBox<LocalTime>
            hourSelector,
            minuteSelector;

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

        executeVoidAsyncTask(
                () -> loadCatalog(barberServiceSelector, employeeSelector, appointmentService),
                _ -> {
                }
        );

        setNodeAsNotVisible(clientResultList, selectedClientCardVbox);
    }

    private void configureClientLiveSearch() {
        clientSearchField.textProperty().addListener((_, _, _) -> executeClientLiveSearchByName());
        clientResultList.getSelectionModel().selectionProperty().addListener((MapChangeListener<? super Integer, ? super ClientInfoDTO>) change -> onClientSelected(change.getValueAdded()));
    }

    private void executeClientLiveSearchByName() {

        if (clientSearchField.getText().isBlank()) {

            cleanListView(clientResultList);
            setNodeAsNotVisible(clientResultList);

        } else {

            List<ClientInfoDTO> clients = appointmentService.clientLiveSearchByName(clientSearchField.getText());

            loadItemsOnListView(clientResultList, clients);

            setNodeAsVisible(clientResultList);
        }
    }

    private void onClientSelected(ClientInfoDTO selectedClient) {

        if (selectedClient == null) return;

        clientReference = selectedClient;

        checkAndToggleSummary();

        Map<Label, String> labelMap = getLabelStringMap(selectedClient);

        setTextsOnLabelMap(labelMap);

        setNodeAsVisible(clientName, nationalIdCardNumber, selectedClientCardVbox);
        setNodeAsNotVisible(clientSearchField, clientResultList);
    }

    private @NonNull Map<Label, String> getLabelStringMap(ClientInfoDTO selectedClient) {

        String firstNameInitial = String.valueOf(selectedClient.getFirstName().charAt(0));
        String lastNameInitial = String.valueOf(selectedClient.getLastName().charAt(0));

        return Map.ofEntries(
                Map.entry(clientName, fullName(selectedClient.getFirstName(), selectedClient.getLastName())),
                Map.entry(nationalIdCardNumber, selectedClient.getNationalIdentityCardNumber()),
                Map.entry(clientInitials, initials(firstNameInitial, lastNameInitial)),
                Map.entry(summaryClient, fullName(selectedClient.getFirstName(), selectedClient.getLastName()))
        );
    }

    private void registerNewAppointment() {
        try {
            if (!isFormComplete()) {
                showToastNotification(anchorPane, applicationContext, APPOINTMENT_DATA_INCOMPLETE_NOTIFICATION_MESSAGE, ToastNotificationType.FAILED);
                return;
            }
            Long clientID = clientReference.getId();
            Long employeeID = employeeReference.getId();
            Long barberServiceID = barberServiceReference.getBarberServiceId();
            LocalDate date = dateSelector.getValue();
            LocalTime exactStartTime = LocalTime.of(hourSelector.getValue().getHour(), minuteSelector.getValue().getMinute());
            LocalDateTime startDatetime = LocalDateTime.of(date, exactStartTime);
            LocalDateTime endDatetime = startDatetime.plusMinutes(APPOINTMENT_DEFAULT_DURATION_IN_MINUTES);
            String optionalNotes = appointmentNotes.getText();
            AppointmentCreationDTO creationDTO = buildDTOFromAttributes(clientID, employeeID, barberServiceID, startDatetime, endDatetime, optionalNotes);
            appointmentService.registerNewAppointment(creationDTO);
            showToastNotification(anchorPane, applicationContext, APPOINTMENT_CREATION_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
            resetForm();
        } catch (ConstraintViolationException | InvalidAppointmentStartDateException |
                 DateTimeOutsideServiceHoursException | EmployeeNotAvailableException exception) {

            notifyValidationFailure(anchorPane, exception, VALIDATION_ERROR_TITLE, APPOINTMENT_CREATION_VALIDATION_FAILED);
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
            setNodeAsVisible(summaryCardVbox);
        } else {
            setNodeAsNotVisible(summaryCardVbox);
        }
    }

    private boolean isFormComplete() {
        return clientReference != null && employeeReference != null && barberServiceReference != null && dateSelector.getValue() != null && hourSelector.getValue() != null && minuteSelector.getValue() != null;
    }

    private void onDateTimeChanged() {
        updateDatetimeSummary(summaryDatetime, dateSelector, hourSelector, minuteSelector);
        checkAndToggleSummary();
    }

    private void resetClientSelection() {
        setBlankTextfield(clientSearchField);
        cleanListView(clientResultList);
        setNodeAsNotVisible(selectedClientCardVbox);
        setNodeAsVisible(clientSearchField);
        this.clientReference = null;
        checkAndToggleSummary();
    }

    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                backButton, () -> redirectToView(ViewRedirection.APPOINTMENTS, anchorPane, applicationContext),
                createClientButton, () -> redirectToView(ViewRedirection.CLIENT_CREATION, anchorPane, applicationContext),
                changeClientButton, this::resetClientSelection,
                resetFormButton, this::resetForm,
                saveButton, this::registerNewAppointment
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

        dateSelector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
        hourSelector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
        minuteSelector.valueProperty().addListener((_, _, _) -> onDateTimeChanged());
    }

    private void onBarberServiceSelected(BarberServiceInfoDTO barberServiceSelected) {

        if (barberServiceSelected == null) return;

        barberServiceReference = barberServiceSelected;

        checkAndToggleSummary();

        Double priceAsDouble = Double.valueOf(servicePrice.getText());

        setTextOnLabel(servicePrice, formatPriceAsString(priceAsDouble));
        setTextOnLabel(summaryService, barberServiceSelected.getName());
        setTextOnLabel(summaryPrice, formatPriceAsString(priceAsDouble));

        setNodeAsVisible(serviceSelectionContainer);
    }

    private void onEmployeeSelected(EmployeeInfoDTO employeeSelected) {

        if (employeeSelected == null) return;

        employeeReference = employeeSelected;

        checkAndToggleSummary();

        String employeeFullName = employeeSelected.getFirstName() + " " + employeeSelected.getLastName();

        setTextOnLabel(summaryEmployee, employeeFullName);
    }

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                clientSearchField, APPOINTMENT_CLIENT_NAME,
                appointmentNotes, APPOINTMENT_NOTES
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
                barberServiceSelector,
                employeeSelector,
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

        setBlankTextfield(clientSearchField);
        cleanListView(clientResultList);
        setNodeAsNotVisible(selectedClientCardVbox, serviceSelectionContainer);
        setNodeAsVisible(clientSearchField);
    }
}