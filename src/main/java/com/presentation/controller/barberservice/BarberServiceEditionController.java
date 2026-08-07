package com.presentation.controller.barberservice;

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

import static com.presentation.constants.StringResource.ToastNotificationMessage.BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.notification.ToastNotificationHelper.showExceptionErrorMessage;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

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
            serviceIdField,
            serviceNameField,
            priceField,
            internalNotesField;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            restoreValuesButton,
            updateButton;

    @FXML
    private ComboBox<BarberServiceCategory> categoryComboBox;

    @FXML
    private VBox errorMessageContainer;

    @FXML
    private Label errorMessageLabel;

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
        loadEnumsOnComboBox(categoryComboBox, BarberServiceCategory.values());
        removeFirstItemFromComboBox(categoryComboBox);
        configureDecimalTextfieldRestrictions(priceField);
    }

    /**
     * Carga los datos del servicio de barbero en los campos correspondientes para su edición.
     *
     * @param infoDTO Datos del servicio de barbero a editar.
     */
    private void loadServiceDataForEdition(BarberServiceInfoDTO infoDTO) {
        Map<TextField, String> map = Map.of(
                serviceIdField, infoDTO.getBarberServiceId().toString(),
                serviceNameField, infoDTO.getName(),
                priceField, infoDTO.getPrice().toString(),
                internalNotesField, infoDTO.getInternalNotes()
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
            Long id = Long.valueOf(serviceIdField.getText());
            String newName = serviceNameField.getText();
            if (priceField.getText().isBlank())
                throw new BlankBarberServicePriceException();
            String newPrice = priceField.getText();
            BarberServiceCategory newCategory = categoryComboBox.getValue();
            String newInternalNotes = internalNotesField.getText();
            BarberServiceUpdateDTO updateDTO = buildDTOFromAttributes(newName, newPrice, newCategory, newInternalNotes);
            barberserviceService.updateService(id, updateDTO);
            showToastNotification(anchorPane, applicationContext, BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
        } catch (RuntimeException exception) {
            showExceptionErrorMessage(exception, errorMessageLabel, errorMessageContainer);
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
                backButton, () -> redirectToView(ViewRedirection.BARBER_SERVICES, anchorPane, applicationContext),
                restoreValuesButton, () -> resetForm(infoDTO),
                updateButton, this::updateBarberService
        );
        configureRunnableMaps(map);
    }

    /**
     * Restablece el formulario con los datos del servicio de barbero original.
     *
     * @param infoDTO Datos del servicio de barbero a editar.
     */
    private void resetForm(BarberServiceInfoDTO infoDTO) {
        cleanTextfields(List.of(serviceNameField, priceField, internalNotesField));
        loadServiceDataForEdition(infoDTO);
        configureUI();
    }

}