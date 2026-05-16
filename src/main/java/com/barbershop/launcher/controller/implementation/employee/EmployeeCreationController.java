package com.barbershop.launcher.controller.implementation.employee;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.launcher.controller.interfaces.CreationController;
import com.barbershop.launcher.controller.interfaces.EmployeeController;
import com.barbershop.service.interfaces.EmployeeService;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.EMPLOYEE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.EMPLOYEE_CREATION_VALIDATION_FAILED;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.barbershop.launcher.constants.ui.prompt_text.EmployeePromptText.*;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.getConstraintViolationsList;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.showErrorAlert;
import static com.barbershop.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class EmployeeCreationController implements CreationController, EmployeeController {

    private final ApplicationContext applicationContext;
    private final EmployeeService employeeService;

    private static final Double PERCENTAGE_VALUE_IF_TEXTFIELD_IS_NULL = -1.0;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Button back_button;

    @FXML
    private TextField first_name_field;

    @FXML
    private TextField last_name_field;

    @FXML
    private MFXDatePicker hire_date_picker;

    @FXML
    private TextField commission_field;

    @FXML
    private Button clear_button;

    @FXML
    private Button save_button;

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

            showErrorAlert(VALIDATION_ERROR_TITLE, EMPLOYEE_CREATION_VALIDATION_FAILED, errorMessages);
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