package com.presentation.controller.employee;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.enums.ViewRedirection;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.EmployeeService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

import static com.presentation.constants.ControllerConstants.EmployeeControllerConstants.PERCENTAGE_VALUE_IF_TEXTFIELD_IS_NULL;
import static com.presentation.constants.PromptTexts.EmployeePromptText.*;
import static com.presentation.constants.StringResource.ToastNotificationMessage.EMPLOYEE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.EMPLOYEE_CREATION_VALIDATION_FAILED;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.format.NumberParser.parsePercentageFraction;
import com.presentation.support.view.ViewRedirectionHelper;

@Component
public class EmployeeCreationController extends BaseCrudFormController<EmployeeCreationDTO, EmployeeInfoDTO> {

    private final EmployeeService employeeService;

    private final ViewRedirectionHelper viewRedirectionHelper;

    public EmployeeCreationController(ApplicationContext applicationContext, EmployeeService employeeService, ViewRedirectionHelper viewRedirectionHelper) {

        super(applicationContext);
        this.employeeService = employeeService;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            clearButton,
            saveButton;

    @FXML
    private TextField
            firstNameField,
            lastNameField,
            commissionField;

    @FXML
    private DatePicker hireDatePicker;

    @FXML
    public void initialize() {

        configureButtonActions();

        configurePromptTexts();

        configureDecimalTextfieldRestrictions(commissionField);
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(EmployeeCreationDTO dto) {

        employeeService.registerNewEmployee(dto);
    }

    @Override
    protected String getSuccessMessage() {

        return EMPLOYEE_CREATION_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return EMPLOYEE_CREATION_VALIDATION_FAILED;
    }

    @Override
    protected EmployeeCreationDTO buildDTO() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        LocalDate hireDate = hireDatePicker.getValue();
        double commissionValueAsDouble = parsePercentageFraction(commissionField.getText(), PERCENTAGE_VALUE_IF_TEXTFIELD_IS_NULL);

        return EmployeeCreationDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .hireDate(hireDate)
                .commissionPercentage(commissionValueAsDouble)
                .build();
    }

    @Override
    protected void resetForm() {

        setBlankTextfield(firstNameField, lastNameField, commissionField);
        cleanDatePicker(hireDatePicker);
    }

    @Override
    protected void configurePromptTexts() {

        Map<TextField, String> promptTextMap = Map.of(
                firstNameField, EMPLOYEE_FIRST_NAME,
                lastNameField, EMPLOYEE_LAST_NAME,
                commissionField, COMMISION_PERCENTAGE
        );

        setPromptTextOnMap(promptTextMap);
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(backButton, () -> viewRedirectionHelper.redirectToView(ViewRedirection.EMPLOYEES, getAnchorPane(), getApplicationContext())),
                Map.entry(clearButton, this::resetForm),
                Map.entry(saveButton, this::saveEntity)
        );

        configureRunnableMaps(map);
    }
}