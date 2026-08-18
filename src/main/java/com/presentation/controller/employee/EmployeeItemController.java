package com.presentation.controller.employee;

import com.dto.employee.EmployeeInfoDTO;
import com.presentation.controller.AbstractItemController;
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

import static com.presentation.constants.CssStylesStrings.ITEM_STATUS_ACTIVO;
import static com.presentation.constants.CssStylesStrings.ITEM_STATUS_INACTIVO;
import static com.presentation.constants.StringResource.DisplayString.ACTIVE_STATUS_LABEL;
import static com.presentation.constants.StringResource.DisplayString.INACTIVE_STATUS_LABEL;
import static com.presentation.support.control.StatusBadgeHelper.applyBadge;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.formatAsPercentage;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.format.PersonNameFormatter.fullName;
import static com.presentation.support.format.PersonNameFormatter.initials;

@Component
@Getter
@Setter
public class EmployeeItemController extends AbstractItemController<EmployeeInfoDTO> {

    private Consumer<EmployeeInfoDTO>
            onEditCallback,
            onStatusChangeCallback;

    @FXML
    private Circle employeeAvatar;

    @FXML
    private Label
            employeeInitials,
            employeeName,
            statusLabel,
            hireDate,
            commissionPercentage,
            monthlyAppointments;

    @FXML
    private VBox employeeStatusBadge;

    @FXML
    private MFXButton
            editButton,
            toggleStatusButton;

    private static @NonNull List<String> getStrings(
            EmployeeInfoDTO infoDTO,
            String employeeFirstNameInitial,
            String employeeLastNameInitial
    ) {

        String currentStatus;

        if (infoDTO.getIsActive()) {

            currentStatus = ACTIVE_STATUS_LABEL;

        } else {

            currentStatus = INACTIVE_STATUS_LABEL;
        }

        String employeeFullName = fullName(infoDTO.getFirstName(), infoDTO.getLastName());
        String employeeInitials = initials(employeeFirstNameInitial, employeeLastNameInitial);

        String hireDate = infoDTO.getHireDateAsString();

        String commissionPercentage = formatAsPercentage(infoDTO.getCommissionPercentage());

        String monthlyAppointments = parseNumberValueToText(infoDTO.getMonthlyAppointmentsCount());

        return List.of(employeeInitials, employeeFullName, currentStatus, hireDate, commissionPercentage, monthlyAppointments);
    }

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void toggleStatusComponents(Boolean isActive) {

        applyBadge(
                isActive,
                statusLabel,
                employeeStatusBadge,
                toggleStatusButton,
                ITEM_STATUS_ACTIVO,
                ITEM_STATUS_INACTIVO
        );
    }

    private void goToEditEmployeeView() {

        fire(onEditCallback);
    }

    private void changeEmployeeActivityStatus() {

        boolean currentActivityValue = infoDTOReference.getIsActive();

        infoDTOReference.setIsActive(!currentActivityValue);

        toggleStatusComponents(infoDTOReference.getIsActive());

        fire(onStatusChangeCallback);
    }

    public void setDataOnItem(EmployeeInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        toggleStatusComponents(infoDTO.getIsActive());

        List<Label> labels = List.of(employeeInitials, employeeName, statusLabel, hireDate, commissionPercentage, monthlyAppointments);

        String firstName = infoDTO.getFirstName();
        String lastName = infoDTO.getLastName();
        String employeeFirstNameInitial = (firstName != null && !firstName.isEmpty()) ? String.valueOf(firstName.charAt(0)) : "";
        String employeeLastNameInitial = (lastName != null && !lastName.isEmpty()) ? String.valueOf(lastName.charAt(0)) : "";
        List<String> texts = getStrings(infoDTO, employeeFirstNameInitial, employeeLastNameInitial);

        Map<Label, String> map = generateMap(labels, texts);

        setTextsOnLabelMap(map);
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditEmployeeView,
                toggleStatusButton, this::changeEmployeeActivityStatus
        );

        configureRunnableMaps(map);
    }
}
