package com.presentation.controller.employee;

import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.service.interfaces.EmployeeService;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.presentation.constants.ControllerConstants.EmployeeControllerConstants.ACTIVATE;
import static com.presentation.constants.ControllerConstants.EmployeeControllerConstants.DEACTIVATE;
import static com.presentation.constants.PromptTexts.EmployeePromptText.*;
import static com.presentation.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.EMPLOYEE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.EMPLOYEE_EDITION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.formatAsPercentage;
import static com.presentation.support.control.ValidationFormatter.getConstraintViolationsList;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class EmployeeEditionController {

    private final ApplicationContext applicationContext;
    private final EmployeeService employeeService;

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

        configureButtonActions(infoDTO);

        configurePromptTexts();

        configureToggleStatusButton(infoDTO.getIsActive());

        configureDecimalTextfieldRestrictions(commissionField);

        loadEmployeeDataForEdition(infoDTO);
    }

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                firstNameField, EMPLOYEE_FIRST_NAME,
                lastNameField, EMPLOYEE_LAST_NAME,
                commissionField, COMMISION_PERCENTAGE
        );

        setPromptTextOnMap(map);
    }

    private void configureToggleStatusButton(Boolean isActive) {

        if (isActive) {

            setTextOnButton(toggleStatusButton, DEACTIVATE);

        } else {

            setTextOnButton(toggleStatusButton, ACTIVATE);

        }
    }

    private void loadEmployeeDataForEdition(EmployeeInfoDTO infoDTO) {

        Map<Label, String> map = Map.of(
                currentFirstName, infoDTO.getFirstName(),
                currentLastName, infoDTO.getLastName(),
                currentStatusLabel, infoDTO.getIsActive() ? "Activo" : "Inactivo",
                currentCommission, formatAsPercentage(infoDTO.getCommissionPercentage() * 100),
                currentHireDate, infoDTO.getHireDateAsString(),
                currentTerminationDate, infoDTO.getTerminationDateAsString()
        );

        setTextsOnLabelMap(map);
    }

    private void updateEmployee(EmployeeInfoDTO infoDTO) {

        try {

            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();

            Double commissionPercentage = convertStringPercentageToDoubleValue(commissionField.getText());

            Boolean isActive = getBooleanFlagFromToggleButtonText();

            LocalDate terminationDate = terminationDatePicker.getValue();

            EmployeeUpdateDTO updateDTO = buildDTOFromAttributes(firstName, lastName, isActive, commissionPercentage, terminationDate);

            employeeService.updateEmployee(infoDTO.getId(), updateDTO);

            showToastNotification(anchorPane, applicationContext, EMPLOYEE_UPDATE_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException exception) {

            String errorMessages = getConstraintViolationsList(exception);

            showWindowAlert(VALIDATION_ERROR_TITLE, EMPLOYEE_EDITION_VALIDATION_FAILED, errorMessages, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchorPane));
        }
    }

    private Boolean getBooleanFlagFromToggleButtonText() {

        return !toggleStatusButton.getText().equals(ACTIVATE);
    }

    private void changeTextOnToggleStatusButton() {

        if (toggleStatusButton.getText().equals(ACTIVATE)) {

            toggleStatusButton.setText(DEACTIVATE);

        } else if (toggleStatusButton.getText().equals(DEACTIVATE)) {

            toggleStatusButton.setText(ACTIVATE);
        }
    }

    private EmployeeUpdateDTO buildDTOFromAttributes(String firstName, String lastName, Boolean isActive, Double commissionPercentage, LocalDate terminationDate) {

        return EmployeeUpdateDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .terminationDate(terminationDate)
                .commissionPercentage(commissionPercentage)
                .build();
    }

    private void configureButtonActions(EmployeeInfoDTO infoDTO) {

        Map<Button, Runnable> map = Map.of(
                backButton, () -> redirectToView(ViewRedirection.EMPLOYEES, anchorPane, applicationContext),
                saveButton, () -> updateEmployee(infoDTO),
                resetButton, () -> resetForm(infoDTO),
                toggleStatusButton, this::changeTextOnToggleStatusButton
        );

        configureRunnableMaps(map);
    }

    private void resetForm(EmployeeInfoDTO infoDTO) {

        cleanTextfields(List.of(firstNameField, lastNameField, commissionField));

        cleanDatePicker(terminationDatePicker);

        loadEmployeeDataForEdition(infoDTO);
    }

    private Double convertStringPercentageToDoubleValue(String valueAsText) {

        if (valueAsText != null) {

            if (valueAsText.isBlank()) return null;

            double valueAsDouble = Double.parseDouble(valueAsText);

            return valueAsDouble / 100;

        }

        return null;
    }
}