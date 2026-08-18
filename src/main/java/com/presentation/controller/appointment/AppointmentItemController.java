package com.presentation.controller.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.presentation.controller.AbstractItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.enums.AppointmentStatus.*;
import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.TIME_FORMATTER;
import static com.presentation.support.control.StatusBadgeHelper.applyAppointmentStatusBadge;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.format.PersonNameFormatter.fullName;

@Component
@Getter
@Setter
public class AppointmentItemController extends AbstractItemController<AppointmentInfoDTO> {

    private Consumer<AppointmentInfoDTO>
            onCompleteCallback,
            onCancelCallback,
            onEditCallback;

    @FXML
    private MFXButton
            editButton,
            completeButton,
            cancelButton;

    @FXML
    private Label
            startTime,
            endTime,
            clientName,
            serviceName,
            employeeName,
            statusBadge;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void goToEditAppointment() {

        fire(onEditCallback);
    }

    private void setAppointmentAsComplete() {

        fire(onCompleteCallback);
        disableButtons(cancelButton, completeButton);
    }

    private void setAppointmentAsCanceled() {

        fire(onCancelCallback);
        disableButtons(cancelButton, completeButton);
    }

    public void setDataOnItem(AppointmentInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        if (infoDTO.getCurrentStatus() == FINALIZADO || infoDTO.getCurrentStatus() == CANCELADO)
            disableButtons(cancelButton, completeButton);

        String clientFullName = fullName(infoDTO.getClientFirstName(), infoDTO.getClientLastName());
        String employeeFullName = fullName(infoDTO.getEmployeeFirstName(), infoDTO.getEmployeeLastName());

        Map<Label, String> map = Map.ofEntries(
                Map.entry(clientName, clientFullName),
                Map.entry(employeeName, employeeFullName),
                Map.entry(serviceName, infoDTO.getServiceName()),
                Map.entry(startTime, infoDTO.getStartDateTime().format(TIME_FORMATTER)),
                Map.entry(endTime, infoDTO.getEndDateTime().format(TIME_FORMATTER))
        );

        setTextsOnLabelMap(map);

        applyAppointmentStatusBadge(infoDTO.getCurrentStatus(), statusBadge);
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditAppointment,
                completeButton, this::setAppointmentAsComplete,
                cancelButton, this::setAppointmentAsCanceled
        );

        configureRunnableMaps(map);
    }
}