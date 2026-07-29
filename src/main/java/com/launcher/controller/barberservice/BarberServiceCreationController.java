/**
 * Controlador para la creación de servicios de barbero. Se encarga de gestionar la lógica de negocio relacionada con la creación de nuevos servicios de barbero,
 * incluyendo la validación de datos, la interacción con el servicio de servicios de barbero y la redirección de vistas.
 */
package com.launcher.controller.barberservice;

import com.dto.barbershopservice.BarberServiceCreationDTO;
import com.enums.BarberServiceCategory;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.exceptions.barberservice.BlankBarberServicePriceException;
import com.service.interfaces.BarberserviceService;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.launcher.constants.PromptTexts.BarberServicePromptText.*;
import static com.launcher.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.launcher.constants.StringResource.ToastNotificationMessage.BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.launcher.constants.StringResource.ValidationErrorMessage.BARBER_SERVICE_CREATION_VALIDATION_FAILED;
import static com.launcher.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.launcher.controller.helper.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.launcher.controller.helper.ContainerManager.getCurrentWindow;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.getConstraintViolationsList;
import static com.launcher.controller.helper.ValidationFormatter.setStringConverter;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class BarberServiceCreationController {

    private final BarberserviceService barberserviceService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private TextField service_name_field;
    @FXML
    private TextField price_field;
    @FXML
    private TextField internal_notes_field;

    @FXML
    private ComboBox<BarberServiceCategory> category_combo_box;

    @FXML
    private MFXButton back_button;
    @FXML
    private MFXButton clean_fields_button;
    @FXML
    private MFXButton save_button;

    @FXML
    public void initialize() {
        configureUI();
        configureButtonActions();
    }

    /**
     * Configura la interfaz de usuario, incluyendo el formato de texto para los campos de precio y la carga de categorías de servicios.
     */
    private void configureUI() {
        configureDecimalTextfieldRestrictions(price_field);
        loadEnumsOnComboBox(category_combo_box, BarberServiceCategory.values());
        setStringConverter(category_combo_box, BarberServiceCategory.TODOS);
        removeFirstItemFromComboBox(category_combo_box);
        configurePromptTexts();
    }

    /**
     * Registra un nuevo servicio de barbero.
     */
    private void registerNewBarberService() {
        try {
            String serviceName = service_name_field.getText();
            if (price_field.getText().isBlank())
                throw new BlankBarberServicePriceException();
            Double price = Double.valueOf(price_field.getText());
            BarberServiceCategory category = category_combo_box.getValue();
            String internalNotes = internal_notes_field.getText();
            BarberServiceCreationDTO creationDTO = buildDTOFromAttributes(serviceName, price, category, internalNotes);
            barberserviceService.registerNewBarberService(creationDTO);
            showToastNotification(anchor_pane, applicationContext, BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
        } catch (ConstraintViolationException exception) {
            String errorMessage = getConstraintViolationsList(exception);
            showWindowAlert(VALIDATION_ERROR_TITLE, BARBER_SERVICE_CREATION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchor_pane));
        }
    }

    /**
     * Construye un DTO de creación de servicio de barbero con los datos proporcionados.
     *
     * @param serviceName   El nombre del servicio de barbero.
     * @param price         El precio del servicio de barbero.
     * @param category      La categoría del servicio de barbero.
     * @param internalNotes Las notas internas del servicio de barbero.
     * @return Un DTO de creación de servicio de barbero con los datos proporcionados.
     */
    private BarberServiceCreationDTO buildDTOFromAttributes(String serviceName, Double price, BarberServiceCategory category, String internalNotes) {
        return BarberServiceCreationDTO.builder()
                .name(serviceName).
                price(price)
                .serviceCategory(category)
                .internalNotes(internalNotes)
                .build();
    }

    private void configurePromptTexts() {
        Map<TextField, String> map = Map.of(
                service_name_field, BARBER_SERVICE_NAME,
                price_field, BARBER_SERVICE_PRICE,
                internal_notes_field, BARBER_SERVICE_INTERNAL_NOTES
        );
        setPromptTextOnMap(map);
    }

    private void resetForm() {
        cleanTextfields(List.of(service_name_field, price_field, internal_notes_field));
    }

    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.BARBER_SERVICES, anchor_pane, applicationContext),
                clean_fields_button, this::resetForm,
                save_button, this::registerNewBarberService
        );
        configureRunnableMaps(map);
    }
}