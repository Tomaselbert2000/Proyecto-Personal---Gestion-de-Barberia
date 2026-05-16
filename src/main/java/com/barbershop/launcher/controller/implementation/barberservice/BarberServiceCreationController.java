package com.barbershop.launcher.controller.implementation.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.exceptions.barberservice.BlankBarberServicePriceException;
import com.barbershop.launcher.controller.interfaces.CreationController;
import com.barbershop.service.interfaces.BarberserviceService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.*;
import static com.barbershop.launcher.constants.ui.prompt_text.BarberServicePromptText.*;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class BarberServiceCreationController implements CreationController {

    private final BarberserviceService barberserviceService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton back_button;

    @FXML
    private MFXTextField service_name_field;

    @FXML
    private MFXTextField price_field;

    @FXML
    private MFXComboBox<BarberServiceCategory> category_combo_box;

    @FXML
    private MFXTextField internal_notes_field;

    @FXML
    private MFXButton clean_fields_button;

    @FXML
    private MFXButton save_button;

    @FXML
    public void initialize() {

        configureButtonActions();

        configureDecimalTextfieldRestrictions(price_field);

        loadEnumsOnComboBox(category_combo_box, BarberServiceCategory.values());

        setStringConverter(category_combo_box, BarberServiceCategory.TODOS);

        removeFirstItemFromComboBox(category_combo_box);
    }

    private void registerNewBarberService() {

        try {

            String serviceName = service_name_field.getText();

            if (price_field.getText().isBlank()) throw new BlankBarberServicePriceException();

            Double price = Double.valueOf(price_field.getText());
            BarberServiceCategory category = category_combo_box.getValue();
            String internalNotes = internal_notes_field.getText();

            BarberServiceCreationDTO creationDTO = buildDTOFromAttributes(serviceName, price, category, internalNotes);

            barberserviceService.registerNewBarberService(creationDTO);

            showToastNotification(
                    anchor_pane,
                    applicationContext,
                    BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE,
                    ToastNotificationType.SUCCESSFUL
            );

        } catch (ConstraintViolationException exception) {

            String errorMessage = getConstraintViolationsList(exception);

            showErrorAlert(VALIDATION_ERROR_TITLE, BARBER_SERVICE_CREATION_VALIDATION_FAILED, errorMessage);
        }
    }

    private BarberServiceCreationDTO buildDTOFromAttributes(String serviceName, Double price, BarberServiceCategory category, String internalNotes) {

        return BarberServiceCreationDTO.builder()
                .name(serviceName)
                .price(price)
                .serviceCategory(category)
                .internalNotes(internalNotes)
                .build();
    }

    @Override
    public void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                service_name_field, BARBER_SERVICE_NAME,
                price_field, BARBER_SERVICE_PRICE,
                internal_notes_field, BARBER_SERVICE_INTERNAL_NOTES
        );

        setPromptTextOnMap(map);
    }

    @Override
    public void resetForm() {

        cleanTextfields(List.of(service_name_field, price_field, internal_notes_field));
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.BARBER_SERVICES, anchor_pane, applicationContext),
                clean_fields_button, this::resetForm,
                save_button, this::registerNewBarberService
        );

        configureRunnableMaps(map);
    }
}
