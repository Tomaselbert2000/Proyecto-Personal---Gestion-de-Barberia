package com.barbershop.launcher.controller.implementation.employee;

import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.launcher.controller.interfaces.EditionController;
import com.barbershop.launcher.controller.interfaces.EmployeeController;
import com.barbershop.service.interfaces.EmployeeService;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.EMPLOYEE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.EMPLOYEE_EDITION_VALIDATION_FAILED;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.barbershop.launcher.constants.ui.prompt_text.EmployeePromptText.*;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class EmployeeEditionController implements EditionController<EmployeeInfoDTO>, EmployeeController {

    private final ApplicationContext applicationContext;
    private final EmployeeService employeeService;

    private static final String ACTIVATE = "Activar";
    private static final String DEACTIVATE = "Desactivar";

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Button back_button;

    @FXML
    private Label current_first_name;

    @FXML
    private Label current_last_name;

    @FXML
    private Label current_hire_date;

    @FXML
    private Label current_commission;

    @FXML
    private Label current_status_label;

    @FXML
    private Label current_termination_date;

    @FXML
    private TextField first_name_field;

    @FXML
    private TextField last_name_field;

    @FXML
    private TextField commission_field;

    @FXML
    private Button toggle_status_button;

    @FXML
    private MFXDatePicker termination_date_picker;

    @FXML
    private Button reset_button;

    @FXML
    private Button save_button;

    @FXML
    public void initialize(EmployeeInfoDTO infoDTO) {

        configureButtonActions(infoDTO);

        configurePromptTexts();

        configureToggleStatusButton(infoDTO.getIsActive());

        configureDecimalTextfieldRestrictions(commission_field);

        loadEmployeeDataForEdition(infoDTO);
    }

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.ofEntries(
                Map.entry(first_name_field, EMPLOYEE_FIRST_NAME),
                Map.entry(last_name_field, EMPLOYEE_LAST_NAME),
                Map.entry(commission_field, COMMISION_PERCENTAGE)
        );

        setPromptTextOnMap(map);
    }

    private void configureToggleStatusButton(Boolean isActive) {

        if (isActive) {

            setTextOnButton(toggle_status_button, DEACTIVATE);

        } else {

            setTextOnButton(toggle_status_button, ACTIVATE);

        }
    }

    private void loadEmployeeDataForEdition(EmployeeInfoDTO infoDTO) {

        Map<Label, String> map = Map.ofEntries(
                Map.entry(current_first_name, infoDTO.getFirstName()),
                Map.entry(current_last_name, infoDTO.getLastName()),
                Map.entry(current_status_label, infoDTO.getIsActive() ? "Activo" : "Inactivo"),
                Map.entry(current_commission, formatAsPercentage(infoDTO.getCommissionPercentage() * 100)),
                Map.entry(current_hire_date, infoDTO.getHireDateAsString()),
                Map.entry(current_termination_date, infoDTO.getTerminationDateAsString())
        );

        setTextsOnLabelMap(map);
    }

    private void updateEmployee(EmployeeInfoDTO infoDTO) {

        try {

            String firstName = first_name_field.getText();
            String lastName = last_name_field.getText();

            Double commissionPercentage = convertStringPercentageToDoubleValue(commission_field.getText());

            Boolean isActive = getBooleanFlagFromToggleButtonText();

            LocalDate terminationDate = termination_date_picker.getValue();

            EmployeeUpdateDTO updateDTO = buildDTOFromAttributes(firstName, lastName, isActive, commissionPercentage, terminationDate);

            employeeService.updateEmployee(infoDTO.getId(), updateDTO);

            showToastNotification(anchor_pane, applicationContext, EMPLOYEE_UPDATE_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException exception) {

            String errorMessages = getConstraintViolationsList(exception);

            showErrorAlert(VALIDATION_ERROR_TITLE, EMPLOYEE_EDITION_VALIDATION_FAILED, errorMessages);
        }
    }

    private Boolean getBooleanFlagFromToggleButtonText() {

        return !toggle_status_button.getText().equals(ACTIVATE);
    }

    private void changeTextOnToggleStatusButton() {

        if (toggle_status_button.getText().equals(ACTIVATE)) {

            toggle_status_button.setText(DEACTIVATE);

        } else if (toggle_status_button.getText().equals(DEACTIVATE)) {

            toggle_status_button.setText(ACTIVATE);
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

    @Override
    public void configureButtonActions(EmployeeInfoDTO infoDTO) {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(back_button, () -> redirectToView(ViewRedirection.EMPLOYEES, anchor_pane, applicationContext)),
                Map.entry(save_button, () -> updateEmployee(infoDTO)),
                Map.entry(reset_button, () -> resetForm(infoDTO)),
                Map.entry(toggle_status_button, this::changeTextOnToggleStatusButton)
        );

        configureRunnableMaps(map);
    }

    @Override
    public void resetForm(EmployeeInfoDTO infoDTO) {

        cleanTextfields(List.of(first_name_field, last_name_field, commission_field));

        cleanDatePicker(termination_date_picker);

        loadEmployeeDataForEdition(infoDTO);
    }

    @Override
    public void configureButtonActions() {
    }

    @Override
    public Double convertStringPercentageToDoubleValue(String valueAsText) {

        if (valueAsText != null) {

            if (valueAsText.isBlank()) return null;

            double valueAsDouble = Double.parseDouble(valueAsText);

            return valueAsDouble / 100;

        }

        return null;
    }
}