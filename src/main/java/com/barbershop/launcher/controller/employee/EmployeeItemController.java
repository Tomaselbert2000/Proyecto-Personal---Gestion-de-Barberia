package com.barbershop.launcher.controller.employee;

import com.barbershop.dto.employee.EmployeeInfoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EmployeeItemController {

    private static final String[] AVATAR_COLORS = {
        "#D4AF37", "#8B4513", "#5D4037", "#A69076", 
        "#6D4C41", "#795548", "#4E342E", "#3E2723"
    };

    @FXML
    private Label employeeName;

    @FXML
    private Label employeeStatus;

    @FXML
    private Label hireDate;

    @FXML
    private Label commission;

    @FXML
    private VBox statusBadge;

    @FXML
    private Button actionButton;

    public void setDataOnItem(EmployeeInfoDTO infoDTO, boolean isActive) {
        String fullName = infoDTO.getFirstName() + " " + infoDTO.getLastName();
        employeeName.setText(fullName);
        
        hireDate.setText(infoDTO.getHireDateAsString());
        
        if (isActive) {
            employeeStatus.setText("ACTIVO");
            statusBadge.getStyleClass().clear();
            statusBadge.getStyleClass().add("status-badge-activo");
            actionButton.setText("Desactivar");
            actionButton.getStyleClass().clear();
            actionButton.getStyleClass().add("appointment-action-button-cancel");
        } else {
            employeeStatus.setText("INACTIVO");
            statusBadge.getStyleClass().clear();
            statusBadge.getStyleClass().add("status-badge-inactivo");
            actionButton.setText("Reactivar");
            actionButton.getStyleClass().clear();
            actionButton.getStyleClass().add("appointment-action-button-complete");
        }
    }

    public void setCommissionPercentage(Double commissionPercentage) {
        if (commissionPercentage != null) {
            commission.setText(String.format("%.0f%%", commissionPercentage));
        } else {
            commission.setText("0%");
        }
    }

    public void setAvatarColor(String name) {
        int colorIndex = Math.abs(name.hashCode()) % AVATAR_COLORS.length;
        String color = AVATAR_COLORS[colorIndex];
    }
}
