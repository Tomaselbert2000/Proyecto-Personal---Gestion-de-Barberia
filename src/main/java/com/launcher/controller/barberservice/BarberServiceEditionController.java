package com.launcher.controller.barberservice;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
import com.enums.BarberServiceCategory;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.exceptions.barberservice.BlankBarberServicePriceException;
import com.service.interfaces.BarberserviceService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.launcher.constants.StringResource.ToastNotificationMessage.BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.launcher.controller.helper.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.launcher.controller.helper.ToastNotificationHelper.showExceptionErrorMessage;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

/**
 * Controlador para la edición de servicios de barbero.
 * Se encarga de gestionar la interfaz de usuario y las operaciones relacionadas con la edición de servicios de barbero.
 */
@Component
@RequiredArgsConstructor
public class BarberServiceEditionController {

    private final BarberserviceService barberserviceService;
    private final ApplicationContext applicationContext;

    @FXML
    private TextField
            service_id_field,
            service_name_field,
            price_field,
            internal_notes_field;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton
            back_button,
            restore_values_button,
            update_button;

    @FXML
    private ComboBox<BarberServiceCategory> category_combo_box;

    @FXML
    private VBox error_message_container;

    @FXML
    private Label error_message_label;

    /**
     * Inicializa el controlador con los datos del servicio de barbero a editar.
     *
     * @param infoDTO Datos del servicio de barbero a editar.
     */
    @FXML
    public void initialize(BarberServiceInfoDTO infoDTO) {
        loadServiceDataForEdition(infoDTO);
        configureUI();
        configureButtonActions(infoDTO);
    }

    /**
     * Configura la interfaz de usuario, cargando los valores y configurando las restricciones.
     */
    private void configureUI() {
        loadEnumsOnComboBox(category_combo_box, BarberServiceCategory.values());
        removeFirstItemFromComboBox(category_combo_box);
        configureDecimalTextfieldRestrictions(price_field);
    }

    /**
     * Carga los datos del servicio de barbero en los campos correspondientes para su edición.
     *
     * @param infoDTO Datos del servicio de barbero a editar.
     */
    private void loadServiceDataForEdition(BarberServiceInfoDTO infoDTO) {
        Map<TextField, String> map = Map.of(
                service_id_field, infoDTO.getBarberServiceId().toString(),
                service_name_field, infoDTO.getName(),
                price_field, infoDTO.getPrice().toString(),
                internal_notes_field, infoDTO.getInternalNotes()
        );
        setTextsOnTextfieldMap(map);
    }

    /**
     * Actualiza el servicio de barbero en la base de datos.
     *
     * @throws BlankBarberServicePriceException Si el precio del servicio está vacío.
     */
    private void updateBarberService() {
        try {
            Long id = Long.valueOf(service_id_field.getText());
            String newName = service_name_field.getText();
            if (price_field.getText().isBlank())
                throw new BlankBarberServicePriceException();
            String newPrice = price_field.getText();
            BarberServiceCategory newCategory = category_combo_box.getValue();
            String newInternalNotes = internal_notes_field.getText();
            BarberServiceUpdateDTO updateDTO = buildDTOFromAttributes(newName, newPrice, newCategory, newInternalNotes);
            barberserviceService.updateService(id, updateDTO);
            showToastNotification(anchor_pane, applicationContext, BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
        } catch (RuntimeException exception) {
            showExceptionErrorMessage(exception, error_message_label, error_message_container);
        }
    }

    /**
     * Construye un DTO de actualización a partir de los atributos proporcionados.
     *
     * @param newName          Nombre del servicio.
     * @param newPrice         Precio del servicio.
     * @param newCategory      Categoría del servicio.
     * @param newInternalNotes Notas internas del servicio.
     * @return DTO de actualización del servicio.
     */
    private BarberServiceUpdateDTO buildDTOFromAttributes(String newName, String newPrice, BarberServiceCategory newCategory, String newInternalNotes) {
        return BarberServiceUpdateDTO.builder()
                .name(newName)
                .price(Double.valueOf(newPrice))
                .serviceCategory(newCategory)
                .internalNotes(newInternalNotes)
                .build();
    }

    /**
     * Configura las acciones de los botones en la interfaz de usuario.
     *
     * @param infoDTO Datos del servicio de barbero a editar.
     */
    private void configureButtonActions(BarberServiceInfoDTO infoDTO) {
        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.BARBER_SERVICES, anchor_pane, applicationContext),
                restore_values_button, () -> resetForm(infoDTO),
                update_button, this::updateBarberService
        );
        configureRunnableMaps(map);
    }

    /**
     * Restablece el formulario con los datos del servicio de barbero original.
     *
     * @param infoDTO Datos del servicio de barbero a editar.
     */
    private void resetForm(BarberServiceInfoDTO infoDTO) {
        cleanTextfields(List.of(service_name_field, price_field, internal_notes_field));
        loadServiceDataForEdition(infoDTO);
        configureUI();
    }

}