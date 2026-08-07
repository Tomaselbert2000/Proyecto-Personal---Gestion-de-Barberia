package com.presentation.controller.barberservice;

import com.dto.barberservice.BarberServiceCreationDTO;
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

import static com.presentation.constants.PromptTexts.BarberServicePromptText.*;
import static com.presentation.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.BARBER_SERVICE_CREATION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.getConstraintViolationsList;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class BarberServiceCreationController {

    private final BarberserviceService barberserviceService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField serviceNameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField internalNotesField;

    @FXML
    private ComboBox<BarberServiceCategory> categoryComboBox;

    @FXML
    private MFXButton backButton;
    @FXML
    private MFXButton cleanFieldsButton;
    @FXML
    private MFXButton saveButton;

    @FXML
    public void initialize() {
        configureUI();
        configureButtonActions();
    }

    /**
     * Configura la interfaz de usuario, incluyendo el formato de texto para los campos de precio y la carga de categorías de servicios.
     */
    private void configureUI() {
        configureDecimalTextfieldRestrictions(priceField);
        loadEnumsOnComboBox(categoryComboBox, BarberServiceCategory.values());
        setStringConverter(categoryComboBox, BarberServiceCategory.TODOS);
        removeFirstItemFromComboBox(categoryComboBox);
        configurePromptTexts();
    }

    /**
     * Registra un nuevo servicio de barbero.
     */
    private void registerNewBarberService() {
        try {
            String serviceName = serviceNameField.getText();
            if (priceField.getText().isBlank())
                throw new BlankBarberServicePriceException();
            Double price = Double.valueOf(priceField.getText());
            BarberServiceCategory category = categoryComboBox.getValue();
            String internalNotes = internalNotesField.getText();
            BarberServiceCreationDTO creationDTO = buildDTOFromAttributes(serviceName, price, category, internalNotes);
            barberserviceService.registerNewBarberService(creationDTO);
            showToastNotification(anchorPane, applicationContext, BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);
        } catch (ConstraintViolationException exception) {
            String errorMessage = getConstraintViolationsList(exception);
            showWindowAlert(VALIDATION_ERROR_TITLE, BARBER_SERVICE_CREATION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchorPane));
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
                serviceNameField, BARBER_SERVICE_NAME,
                priceField, BARBER_SERVICE_PRICE,
                internalNotesField, BARBER_SERVICE_INTERNAL_NOTES
        );
        setPromptTextOnMap(map);
    }

    private void resetForm() {
        cleanTextfields(List.of(serviceNameField, priceField, internalNotesField));
    }

    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                backButton, () -> redirectToView(ViewRedirection.BARBER_SERVICES, anchorPane, applicationContext),
                cleanFieldsButton, this::resetForm,
                saveButton, this::registerNewBarberService
        );
        configureRunnableMaps(map);
    }
}