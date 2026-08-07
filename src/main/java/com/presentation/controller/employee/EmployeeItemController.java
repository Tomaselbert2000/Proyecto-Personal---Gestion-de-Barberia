package com.presentation.controller.employee;

import com.dto.employee.EmployeeInfoDTO;
import com.presentation.controller.item.ItemController;
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
import static com.presentation.support.control.UIBasicComponents.*;

@Component
@Getter
@Setter
public class EmployeeItemController implements ItemController<EmployeeInfoDTO> {

    private EmployeeInfoDTO infoDTOReference;

    private Consumer<EmployeeInfoDTO>
            onEditCallBack,
            onStatusChangeCallBack;

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

            setTextOnLabel(statusLabel, "Activo");
            addNodeStyleClass(employeeStatusBadge, ITEM_STATUS_ACTIVO);

            setTextOnButton(toggleStatusButton, "Desactivar");

        } else {

            setTextOnLabel(statusLabel, "Inactivo");
            addNodeStyleClass(employeeStatusBadge, ITEM_STATUS_INACTIVO);

            setTextOnButton(toggleStatusButton, "Activar");

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

    @Override
    public void setDataOnItem(EmployeeInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        toggleStatusComponents(infoDTO.getIsActive());

        List<Label> labels = List.of(employeeInitials, employeeName, statusLabel, hireDate, commissionPercentage, monthlyAppointments);

        String employeeFirstNameInitial = String.valueOf(infoDTO.getFirstName().charAt(0));
        String employeeLastNameInitial = String.valueOf(infoDTO.getLastName().charAt(0));
        List<String> texts = getStrings(infoDTO, employeeFirstNameInitial, employeeLastNameInitial);

        Map<Label, String> map = generateMap(labels, texts);

        setTextsOnLabelMap(map);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditEmployeeView,
                toggleStatusButton, this::changeEmployeeActivityStatus
        );

        configureRunnableMaps(map);
    }
}
