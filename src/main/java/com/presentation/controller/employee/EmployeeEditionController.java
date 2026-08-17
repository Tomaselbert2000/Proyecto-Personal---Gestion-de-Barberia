package com.presentation.controller.employee;

import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.presentation.controller.BaseCrudFormController;
import com.presentation.support.view.ViewRedirectionHelper;
import com.service.interfaces.EmployeeService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

import static com.enums.ViewRedirection.EMPLOYEES;
import static com.presentation.constants.ControllerConstants.EmployeeControllerConstants.ACTIVATE;
import static com.presentation.constants.ControllerConstants.EmployeeControllerConstants.DEACTIVATE;
import static com.presentation.constants.PromptTexts.EmployeePromptText.*;
import static com.presentation.constants.StringResource.DisplayString.*;
import static com.presentation.constants.StringResource.ToastNotificationMessage.EMPLOYEE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.EMPLOYEE_EDITION_VALIDATION_FAILED;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.formatAsPercentage;
import static com.presentation.support.format.NumberParser.parsePercentageFraction;

@Component
public class EmployeeEditionController extends BaseCrudFormController<EmployeeUpdateDTO, EmployeeInfoDTO> {

    private final EmployeeService employeeService;

    private Boolean isActive;

    private final ViewRedirectionHelper viewRedirectionHelper;

    public EmployeeEditionController(ApplicationContext applicationContext, EmployeeService employeeService, ViewRedirectionHelper viewRedirectionHelper) {

        super(applicationContext);
        this.employeeService = employeeService;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            toggleStatusButton,
            resetButton,
            saveButton;

    @FXML
    private Label
            currentFirstName,
            currentLastName,
            currentHireDate,
            currentCommission,
            currentStatusLabel,
            currentTerminationDate;

    @FXML
    private TextField
            firstNameField,
            lastNameField,
            commissionField;

    @FXML
    private DatePicker terminationDatePicker;

    @FXML
    public void initialize(EmployeeInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        configureButtonActions();

        configurePromptTexts();

        configureToggleStatusButton(infoDTO.getIsActive());

        configureDecimalTextfieldRestrictions(commissionField);

        loadEmployeeDataForEdition(infoDTO);
    }

    private void configureToggleStatusButton(Boolean isActive) {

        if (isActive) {

            setTextOnButton(toggleStatusButton, DEACTIVATE);

        } else {

            setTextOnButton(toggleStatusButton, ACTIVATE);

        }
    }

    private void loadEmployeeDataForEdition(EmployeeInfoDTO infoDTO) {

        isActive = infoDTO.getIsActive();

        Map<Label, String> map = Map.of(
                currentFirstName, infoDTO.getFirstName(),
                currentLastName, infoDTO.getLastName(),
                currentStatusLabel, infoDTO.getIsActive() ? ACTIVE_STATUS_LABEL : INACTIVE_STATUS_LABEL,
                currentCommission, formatAsPercentage(infoDTO.getCommissionPercentage() * 100),
                currentHireDate, infoDTO.getHireDateAsString(),
                currentTerminationDate, infoDTO.getTerminationDateAsString()
        );

        String terminationDateAsString = infoDTO.getTerminationDateAsString();

        if (!terminationDateAsString.isBlank() && !terminationDateAsString.equals(DEFAULT_TERMINATION_DATE_STRING)) {

            terminationDatePicker.setValue(LocalDate.parse(terminationDateAsString));
        }

        setTextsOnLabelMap(map);
    }

    private void changeTextOnToggleStatusButton() {

        isActive = !isActive;

        setTextOnButton(toggleStatusButton, isActive ? DEACTIVATE : ACTIVATE);
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(EmployeeUpdateDTO dto) {

        employeeService.updateEmployee(infoDTOReference.getId(), dto);
    }

    @Override
    protected String getSuccessMessage() {

        return EMPLOYEE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {
        return EMPLOYEE_EDITION_VALIDATION_FAILED;
    }

    @Override
    protected EmployeeUpdateDTO buildDTO() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        Double commissionPercentage = parsePercentageFraction(commissionField.getText(), null);
        Boolean isActive = this.isActive;
        LocalDate terminationDate = terminationDatePicker.getValue();

        return EmployeeUpdateDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .terminationDate(terminationDate)
                .commissionPercentage(commissionPercentage)
                .build();
    }

    @Override
    protected void resetForm() {

        loadEmployeeDataForEdition(infoDTOReference);
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                backButton, () -> viewRedirectionHelper.redirectToView(EMPLOYEES, anchorPane, getApplicationContext()),
                saveButton, this::saveEntity,
                resetButton, this::resetForm,
                toggleStatusButton, this::changeTextOnToggleStatusButton
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                firstNameField, EMPLOYEE_FIRST_NAME,
                lastNameField, EMPLOYEE_LAST_NAME,
                commissionField, COMMISION_PERCENTAGE
        );

        setPromptTextOnMap(map);
    }
}