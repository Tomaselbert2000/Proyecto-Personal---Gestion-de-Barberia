package com.barbershop.launcher.controller.implementation.appointment;

import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.launcher.controller.interfaces.ItemController;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.barbershop.launcher.constants.ui.css_class.CssStylesStrings.*;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;

@Component
@Getter
@Setter
public class AppointmentItemController implements ItemController<AppointmentInfoDTO> {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private Consumer<AppointmentInfoDTO> onCompleteCallback;
    private Consumer<AppointmentInfoDTO> onCancelCallback;
    private Consumer<AppointmentInfoDTO> onEditCallback;

    private AppointmentInfoDTO infoDTOReference;

    @FXML
    private Button edit_button;

    @FXML
    private Button complete_button;

    @FXML
    private Button cancel_button;

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
    public void initialize() {

        configureButtonActions();
    }

    private void goToEditAppointment() {

        if (onEditCallback != null) onEditCallback.accept(infoDTOReference);
    }

    private void setAppointmentAsComplete() {

        if (onCompleteCallback != null) onCompleteCallback.accept(infoDTOReference);

        disableButtons(cancel_button, complete_button);
    }

    private void setAppointmentAsCanceled() {

        if (onCancelCallback != null) onCancelCallback.accept(infoDTOReference);

        disableButtons(cancel_button, complete_button);
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

    @Override
    public void setDataOnItem(AppointmentInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        if (infoDTO.getCurrentStatus() == AppointmentStatus.FINALIZADO || infoDTO.getCurrentStatus() == AppointmentStatus.CANCELADO)

            disableButtons(cancel_button, complete_button);

        String clientFullName = concatNames(infoDTO.getClientFirstName(), infoDTO.getClientLastName());
        String employeeFullName = concatNames(infoDTO.getEmployeeFirstName(), infoDTO.getEmployeeLastName());

        Map<Label, String> map = Map.ofEntries(
                Map.entry(client_name, clientFullName),
                Map.entry(employee_name, employeeFullName),
                Map.entry(service_name, infoDTO.getServiceName()),
                Map.entry(start_time, infoDTO.getStartDateTime().format(TIME_FORMATTER)),
                Map.entry(end_time, infoDTO.getEndDateTime().format(TIME_FORMATTER))
        );

        setTextsOnLabelMap(map);

        AppointmentStatus status = infoDTO.getCurrentStatus();

        updateStatusBadge(status);
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                edit_button, this::goToEditAppointment,
                complete_button, this::setAppointmentAsComplete,
                cancel_button, this::setAppointmentAsCanceled
        );

        configureRunnableMaps(map);
    }
}
