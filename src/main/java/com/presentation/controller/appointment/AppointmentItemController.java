package com.presentation.controller.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.enums.AppointmentStatus;
import com.presentation.controller.item.ItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.TIME_FORMATTER;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialDesignStyleClass.*;
import static com.presentation.support.control.UIBasicComponents.*;

@Component
@Getter
@Setter
public class AppointmentItemController implements ItemController<AppointmentInfoDTO> {

    private Consumer<AppointmentInfoDTO>
            onCompleteCallback,
            onCancelCallback,
            onEditCallback;

    private AppointmentInfoDTO infoDTOReference;

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

    /**
     * Navega a la edición de la cita.
     */
    private void goToEditAppointment() {
        if (onEditCallback != null) onEditCallback.accept(infoDTOReference);
    }

    /**
     * Marca la cita como completada.
     */
    private void setAppointmentAsComplete() {
        if (onCompleteCallback != null) onCompleteCallback.accept(infoDTOReference);
        disableButtons(cancelButton, completeButton);
    }

    /**
     * Marca la cita como cancelada.
     */
    private void setAppointmentAsCanceled() {
        if (onCancelCallback != null) onCancelCallback.accept(infoDTOReference);
        disableButtons(cancelButton, completeButton);
    }

    /**
     * Actualiza el estado de la cita en el badge.
     *
     * @param status El estado de la cita.
     */
    private void updateStatusBadge(AppointmentStatus status) {
        statusBadge.getStyleClass().clear();
        statusBadge.getStyleClass().add(BADGE);

        switch (status) {
            case PROGRAMADO, REPROGRAMADO:
                addLabelStyle(statusBadge, SCHEDULED_BADGE);
                setTextOnLabel(statusBadge, status == AppointmentStatus.PROGRAMADO ? AppointmentStatus.PROGRAMADO.getDisplayName() : AppointmentStatus.REPROGRAMADO.getDisplayName());
                break;
            case FINALIZADO:
                addLabelStyle(statusBadge, COMPLETED_BADGE);
                setTextOnLabel(statusBadge, AppointmentStatus.FINALIZADO.getDisplayName());
                break;
            case CANCELADO:
                addLabelStyle(statusBadge, CANCELED_BADGE);
                setTextOnLabel(statusBadge, AppointmentStatus.CANCELADO.getDisplayName());
                break;
        }
    }

    public void setDataOnItem(AppointmentInfoDTO infoDTO) {
        infoDTOReference = infoDTO;

        if (infoDTO.getCurrentStatus() == AppointmentStatus.FINALIZADO || infoDTO.getCurrentStatus() == AppointmentStatus.CANCELADO)
            disableButtons(cancelButton, completeButton);

        String clientFullName = String.join(" ", infoDTO.getClientFirstName(), infoDTO.getClientLastName());
        String employeeFullName = String.join(" ", infoDTO.getEmployeeFirstName(), infoDTO.getEmployeeLastName());

        Map<Label, String> map = Map.ofEntries(
                Map.entry(clientName, clientFullName),
                Map.entry(employeeName, employeeFullName),
                Map.entry(serviceName, infoDTO.getServiceName()),
                Map.entry(startTime, infoDTO.getStartDateTime().format(TIME_FORMATTER)),
                Map.entry(endTime, infoDTO.getEndDateTime().format(TIME_FORMATTER))
        );

        setTextsOnLabelMap(map);

        updateStatusBadge(infoDTO.getCurrentStatus());
    }

    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditAppointment,
                completeButton, this::setAppointmentAsComplete,
                cancelButton, this::setAppointmentAsCanceled
        );
        configureRunnableMaps(map);
    }
}