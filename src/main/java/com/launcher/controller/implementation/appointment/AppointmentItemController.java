/**
 * Controlador para un ítem de cita. Se encarga de gestionar la lógica de negocio relacionada con la visualización y edición de citas,
 * incluyendo la interacción con callbacks para acciones como editar, completar y cancelar citas.
 */
package com.launcher.controller.implementation.appointment;

import com.dto.appointment.AppointmentInfoDTO;
import com.enums.AppointmentStatus;
import com.launcher.controller.interfaces.ItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.launcher.constants.ControllerConstants.AppointmentControllerConstants.TIME_FORMATTER;
import static com.launcher.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialDesignStyleClass.*;
import static com.launcher.controller.helper.UIBasicComponents.*;

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
            edit_button,
            complete_button,
            cancel_button;

    @FXML
    private Label
            start_time,
            end_time,
            client_name,
            service_name,
            employee_name,
            status_badge;

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
        disableButtons(cancel_button, complete_button);
    }

    /**
     * Marca la cita como cancelada.
     */
    private void setAppointmentAsCanceled() {
        if (onCancelCallback != null) onCancelCallback.accept(infoDTOReference);
        disableButtons(cancel_button, complete_button);
    }

    /**
     * Concatena el nombre y apellido de una persona.
     *
     * @param firstName El nombre de la persona.
     * @param lastName  El apellido de la persona.
     * @return El nombre completo de la persona.
     */
    private String concatNames(String firstName, String lastName) {
        return firstName + " " + lastName;
    }

    /**
     * Actualiza el estado de la cita en el badge.
     *
     * @param status El estado de la cita.
     */
    private void updateStatusBadge(AppointmentStatus status) {
        status_badge.getStyleClass().clear();
        status_badge.getStyleClass().add(BADGE);

        switch (status) {
            case PROGRAMADO, REPROGRAMADO:
                addLabelStyle(status_badge, SCHEDULED_BADGE);
                setTextOnLabel(status_badge, status == AppointmentStatus.PROGRAMADO ? AppointmentStatus.PROGRAMADO.getDisplayName() : AppointmentStatus.REPROGRAMADO.getDisplayName());
                break;
            case FINALIZADO:
                addLabelStyle(status_badge, COMPLETED_BADGE);
                setTextOnLabel(status_badge, AppointmentStatus.FINALIZADO.getDisplayName());
                break;
            case CANCELADO:
                addLabelStyle(status_badge, CANCELED_BADGE);
                setTextOnLabel(status_badge, AppointmentStatus.CANCELADO.getDisplayName());
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

        updateStatusBadge(infoDTO.getCurrentStatus());
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