package com.presentation.controller.appointment;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.AppointmentService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
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

import static com.enums.ViewRedirection.APPOINTMENTS;
import static com.enums.ViewRedirection.CLIENT_CREATION;
import static com.presentation.concurrency.ConcurrencyManager.executeVoidAsyncTask;
import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.APPOINTMENT_DEFAULT_DURATION_IN_MINUTES;
import static com.presentation.constants.PromptTexts.AppointmentPromptText.APPOINTMENT_CLIENT_NAME;
import static com.presentation.constants.PromptTexts.AppointmentPromptText.APPOINTMENT_NOTES;
import static com.presentation.constants.StringResource.ToastNotificationMessage.APPOINTMENT_CREATION_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.APPOINTMENT_CREATION_VALIDATION_FAILED;
import static com.presentation.support.control.AppointmentCatalogLoader.loadCatalog;
import static com.presentation.support.control.AppointmentDateTimeSummaryHelper.updateDatetimeSummary;
import static com.presentation.support.control.ListViewHelper.cleanListView;
import static com.presentation.support.control.ListViewHelper.loadItemsOnListView;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.format.PersonNameFormatter.fullName;
import static com.presentation.support.format.PersonNameFormatter.initials;
import static com.presentation.support.format.PriceFormatter.formatPriceAsString;
import com.presentation.support.view.ViewRedirectionHelper;
import static com.presentation.support.view.VisibilityHelper.setNodeAsNotVisible;
import static com.presentation.support.view.VisibilityHelper.setNodeAsVisible;

@Component
public class AppointmentCreationController extends BaseCrudFormController<AppointmentCreationDTO, AppointmentInfoDTO> {

    private ClientInfoDTO clientReference;
    private BarberServiceInfoDTO barberServiceReference;
    private EmployeeInfoDTO employeeReference;

    private final AppointmentService appointmentService;

    private final ViewRedirectionHelper viewRedirectionHelper;

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
            ApplicationContext applicationContext,
            ViewRedirectionHelper viewRedirectionHelper
    ) {

        super(applicationContext);
        this.appointmentService = appointmentService;
        this.viewRedirectionHelper = viewRedirectionHelper;
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

        String firstName = selectedClient.getFirstName();
        String lastName = selectedClient.getLastName();
        String firstNameInitial = (firstName != null && !firstName.isEmpty()) ? String.valueOf(firstName.charAt(0)) : "";
        String lastNameInitial = (lastName != null && !lastName.isEmpty()) ? String.valueOf(lastName.charAt(0)) : "";

        return Map.ofEntries(
                Map.entry(clientName, fullName(selectedClient.getFirstName(), selectedClient.getLastName())),
                Map.entry(nationalIdCardNumber, selectedClient.getNationalIdentityCardNumber()),
                Map.entry(clientInitials, initials(firstNameInitial, lastNameInitial)),
                Map.entry(summaryClient, fullName(selectedClient.getFirstName(), selectedClient.getLastName()))
        );
    }

    private void checkAndToggleSummary() {

        if (isFormComplete()) {

            setNodeAsVisible(summaryCardVbox);

        } else {

            setNodeAsNotVisible(summaryCardVbox);
        }
    }

    private boolean isFormComplete() {

        return clientReference != null
                && employeeReference != null
                && barberServiceReference != null
                && dateSelector.getValue() != null
                && hourSelector.getValue() != null
                && minuteSelector.getValue() != null;
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

    protected void toggleContainersVisibility() {

        setBlankTextfield(clientSearchField);
        cleanListView(clientResultList);
        setNodeAsNotVisible(selectedClientCardVbox, serviceSelectionContainer);
        setNodeAsVisible(clientSearchField);
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(AppointmentCreationDTO dto) {

        appointmentService.registerNewAppointment(dto);
    }

    @Override
    protected String getSuccessMessage() {

        return APPOINTMENT_CREATION_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return APPOINTMENT_CREATION_VALIDATION_FAILED;
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(

                Map.entry(backButton, () -> viewRedirectionHelper.redirectToView(APPOINTMENTS, getAnchorPane(), getApplicationContext())),
                Map.entry(createClientButton, () -> viewRedirectionHelper.redirectToView(CLIENT_CREATION, getAnchorPane(), getApplicationContext())),
                Map.entry(changeClientButton, this::resetClientSelection),
                Map.entry(resetFormButton, this::resetForm),
                Map.entry(saveButton, this::saveEntity)
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                clientSearchField, APPOINTMENT_CLIENT_NAME,
                appointmentNotes, APPOINTMENT_NOTES
        );
        setPromptTextOnMap(map);
    }

    @Override
    protected AppointmentCreationDTO buildDTO() {

        Long clientID = clientReference.getId();
        Long employeeID = employeeReference.getId();
        Long barberServiceID = barberServiceReference.getBarberServiceId();

        LocalDate date = dateSelector.getValue();

        LocalTime exactStartTime = LocalTime.of(
                hourSelector.getValue().getHour(),
                minuteSelector.getValue().getMinute()
        );

        LocalDateTime startDatetime = LocalDateTime.of(date, exactStartTime);
        LocalDateTime endDatetime = startDatetime.plusMinutes(APPOINTMENT_DEFAULT_DURATION_IN_MINUTES);

        String optionalNotes = appointmentNotes.getText();

        return AppointmentCreationDTO.builder()
                .clientID(clientID)
                .employeeID(employeeID)
                .barberserviceID(barberServiceID)
                .startDateTime(startDatetime)
                .endDateTime(endDatetime)
                .optionalNotes(optionalNotes)
                .build();
    }

    @Override
    protected void resetForm() {

        toggleContainersVisibility();

        setBlankTextfield(clientSearchField);

        cleanListView(clientResultList);

        setNodeAsNotVisible(selectedClientCardVbox, serviceSelectionContainer);

        setNodeAsVisible(clientSearchField);

        this.clientReference = null;
        this.barberServiceReference = null;
        this.employeeReference = null;
    }
}