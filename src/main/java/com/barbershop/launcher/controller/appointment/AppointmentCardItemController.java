package com.barbershop.launcher.controller.appointment;

import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.launcher.controller.UI_RenderingFunctions;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.css_class.CssStylesStrings.*;
import static com.barbershop.launcher.controller.UI_RenderingFunctions.configureLabelStyle;

public class AppointmentCardItemController {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private Button editButton;

    @FXML
    private Button completeButton;

    @FXML
    private Button cancelButton;

    //TODO: completar esto más tarde

    @FXML
    private Label start_time;

    @FXML
    private Label end_time;

    @FXML
    private Label client_name;

    @FXML
    private Label service_name;

    @FXML
    private Label employee_name;

    @FXML
    private Label status_badge;

    @FXML
    public void setDataOnItem(AppointmentInfoDTO infoDTO) {

        String clientFullName = concatNames(infoDTO.getClientFirstName(), infoDTO.getClientLastName());
        String employeeFullName = concatNames(infoDTO.getEmployeeFirstName(), infoDTO.getEmployeeLastName());

        List<Label> labels = List.of(client_name, employee_name, service_name, start_time, end_time);
        List<String> texts = List.of(clientFullName, employeeFullName, infoDTO.getServiceName(), infoDTO.getStartDateTime().format(TIME_FORMATTER), infoDTO.getEndDateTime().format(TIME_FORMATTER));
        Map<Label, String> labelTextsMap = UI_RenderingFunctions.generateMap(labels, texts);

        UI_RenderingFunctions.setTextsOnLabelMap(labelTextsMap);

        AppointmentStatus status = infoDTO.getCurrentStatus();

        updateStatusBadge(status);
    }

    private String concatNames(String firstName, String lastName) {

        return firstName + " " + lastName;
    }

    private void updateStatusBadge(AppointmentStatus status) {

        status_badge.getStyleClass().clear();

        switch (status) {
            case PROGRAMADO:
                configureLabelStyle(status_badge, APPOINTMENT_STATUS_PROGRAMADO_STYLE_CLASS);
                break;
            case FINALIZADO:
                configureLabelStyle(status_badge, APPOINTMENT_STATUS_FINALIZADO_STYLE_CLASS);
                break;
            case CANCELADO:
                configureLabelStyle(status_badge, APPOINTMENT_STATUS_CANCELADO_STYLE_CLASS);
                break;
            case REPROGRAMADO:
                configureLabelStyle(status_badge, APPOINTMENT_STATUS_REPROGRAMADO_STYLE_CLASS);
                break;
        }
    }
}
