package com.launcher.controller.employee;

import com.dto.employee.EmployeeInfoDTO;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.launcher.constants.CssStylesStrings.ITEM_STATUS_ACTIVO;
import static com.launcher.constants.CssStylesStrings.ITEM_STATUS_INACTIVO;
import static com.launcher.controller.helper.UIBasicComponents.*;

@Component
@Getter
@Setter
public class EmployeeItemController {

    private EmployeeInfoDTO infoDTOReference;

    private Consumer<EmployeeInfoDTO>
            onEditCallBack,
            onStatusChangeCallBack;

    @FXML
    private Circle employee_avatar;

    @FXML
    private Label
            employee_initials,
            employee_name,
            status_label,
            hire_date,
            commission_percentage,
            monthly_appointments;

    @FXML
    private VBox employee_status_badge;

    @FXML
    private MFXButton
            edit_button,
            toggle_status_button;

    private static @NonNull List<String> getStrings(EmployeeInfoDTO infoDTO, String employeeFirstNameInitial, String employeeLastNameInitial) {
        String employeeInitials = employeeFirstNameInitial + employeeLastNameInitial;

        String currentStatus;

        if (infoDTO.getIsActive()) {

            currentStatus = "Activo";

        } else {

            currentStatus = "Inactivo";
        }

        String employeeFullName = infoDTO.getFirstName() + " " + infoDTO.getLastName();
        String hireDate = infoDTO.getHireDateAsString();
        String commissionPercentage = (infoDTO.getCommissionPercentage() * 100) + "%";
        String monthlyAppointments = String.valueOf(10);

        return List.of(employeeInitials, employeeFullName, currentStatus, hireDate, commissionPercentage, monthlyAppointments);
    }

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void toggleStatusComponents(Boolean isActive) {

        if (isActive) {

            setTextOnLabel(status_label, "Activo");
            addNodeStyleClass(employee_status_badge, ITEM_STATUS_ACTIVO);

            setTextOnButton(toggle_status_button, "Desactivar");

        } else {

            setTextOnLabel(status_label, "Inactivo");
            addNodeStyleClass(employee_status_badge, ITEM_STATUS_INACTIVO);

            setTextOnButton(toggle_status_button, "Activar");

        }
    }

    private void goToEditEmployeeView() {

        if (onEditCallBack != null) onEditCallBack.accept(infoDTOReference);
    }

    private void changeEmployeeActivityStatus() {

        boolean currentActivityValue = infoDTOReference.getIsActive();

        infoDTOReference.setIsActive(!currentActivityValue);

        toggleStatusComponents(infoDTOReference.getIsActive());

        if (onStatusChangeCallBack != null) onStatusChangeCallBack.accept(infoDTOReference);
    }

    public void setDataOnItem(EmployeeInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        toggleStatusComponents(infoDTO.getIsActive());

        List<Label> labels = List.of(employee_initials, employee_name, status_label, hire_date, commission_percentage, monthly_appointments);

        String employeeFirstNameInitial = String.valueOf(infoDTO.getFirstName().charAt(0));
        String employeeLastNameInitial = String.valueOf(infoDTO.getLastName().charAt(0));
        List<String> texts = getStrings(infoDTO, employeeFirstNameInitial, employeeLastNameInitial);

        Map<Label, String> map = generateMap(labels, texts);

        setTextsOnLabelMap(map);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                edit_button, this::goToEditEmployeeView,
                toggle_status_button, this::changeEmployeeActivityStatus
        );

        configureRunnableMaps(map);
    }
}
