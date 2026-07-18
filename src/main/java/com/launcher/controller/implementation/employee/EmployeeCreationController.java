package com.launcher.controller.implementation.employee;

import com.dto.employee.EmployeeCreationDTO;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.launcher.controller.interfaces.CreationController;
import com.launcher.controller.interfaces.EmployeeController;
import com.service.interfaces.EmployeeService;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

import static com.launcher.constants.ui.messages.ToastNotificationMessage.EMPLOYEE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.launcher.constants.ui.messages.ValidationErrorMessage.EMPLOYEE_CREATION_VALIDATION_FAILED;
import static com.launcher.constants.ui.messages.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.launcher.constants.ui.prompt_text.EmployeePromptText.*;
import static com.launcher.controller.helper.ContainerManager.getCurrentWindow;
import static com.launcher.controller.helper.HelperConstants.ACCEPT_BUTTON_TEXT;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.getConstraintViolationsList;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class EmployeeCreationController implements CreationController, EmployeeController {

    private static final Double PERCENTAGE_VALUE_IF_TEXTFIELD_IS_NULL = -1.0;
    private final ApplicationContext applicationContext;
    private final EmployeeService employeeService;
    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton
            back_button,
            clear_button,
            save_button;

    @FXML
    private TextField
            first_name_field,
            last_name_field,
            commission_field;

    @FXML
    private DatePicker hire_date_picker;

    @FXML
    public void initialize() {

        configureButtonActions();

        configurePromptTexts();

        configureDecimalTextfieldRestrictions(commission_field);
    }

    @Override
    public void configurePromptTexts() {

        Map<TextField, String> promptTextMap = Map.of(
                first_name_field, EMPLOYEE_FIRST_NAME,
                last_name_field, EMPLOYEE_LAST_NAME,
                commission_field, COMMISION_PERCENTAGE
        );

        setPromptTextOnMap(promptTextMap);
    }

    @Override
    public void resetForm() {

        setBlankTextfield(first_name_field, last_name_field, commission_field);

        cleanDatePicker(hire_date_picker);
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.EMPLOYEES, anchor_pane, applicationContext),
                clear_button, this::resetForm,
                save_button, this::registerNewEmployee
        );

        configureRunnableMaps(map);
    }

    private void registerNewEmployee() {

        try {

            String firstName = first_name_field.getText();
            String lastName = last_name_field.getText();
            LocalDate hireDate = hire_date_picker.getValue();

            double commissionValueAsDouble = convertStringPercentageToDoubleValue(commission_field.getText());

            EmployeeCreationDTO creationDTO = buildDTOFromAttributes(firstName, lastName, hireDate, commissionValueAsDouble);

            employeeService.registerNewEmployee(creationDTO);

            showToastNotification(anchor_pane, applicationContext, EMPLOYEE_CREATION_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

            resetForm();

        } catch (ConstraintViolationException exception) {

            String errorMessages = getConstraintViolationsList(exception);

            showWindowAlert(VALIDATION_ERROR_TITLE, EMPLOYEE_CREATION_VALIDATION_FAILED, errorMessages, Alert.AlertType.ERROR, ACCEPT_BUTTON_TEXT, getCurrentWindow(anchor_pane));
        }
    }

    private EmployeeCreationDTO buildDTOFromAttributes(String firstName, String lastName, LocalDate hireDate, double commissionValueAsDouble) {

        return EmployeeCreationDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .hireDate(hireDate)
                .commissionPercentage(commissionValueAsDouble)
                .build();
    }

    @Override
    public Double convertStringPercentageToDoubleValue(String valueAsText) {

        if (valueAsText == null || valueAsText.isEmpty()) return PERCENTAGE_VALUE_IF_TEXTFIELD_IS_NULL;

        double valueAsDouble = Double.parseDouble(valueAsText);

        return valueAsDouble / 100;
    }
}