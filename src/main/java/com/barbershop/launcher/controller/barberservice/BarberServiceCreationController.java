package com.barbershop.launcher.controller.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.exceptions.barberservice.BlankBarberServicePriceException;
import com.barbershop.launcher.controller.interfaces.BarberServiceControllerViewFunctions;
import com.barbershop.service.interfaces.BarberserviceService;
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

import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.prompt_text.BarberServicePromptText.*;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.redirectToView;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showExceptionErrorMessage;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;

@Component
@RequiredArgsConstructor
public class BarberServiceCreationController implements BarberServiceControllerViewFunctions {

    private final BarberserviceService barberserviceService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Button back_button;

    @FXML
    private TextField service_name_field;

    @FXML
    private TextField price_field;

    @FXML
    private ComboBox<BarberServiceCategory> category_combo_box;

    @FXML
    private TextField internal_notes_field;

    @FXML
    private VBox error_message_container;

    @FXML
    private Label error_message_label;

    @FXML
    private Button clean_fields_button;

    @FXML
    private Button save_button;

    List<TextField> textfields;

    @FXML
    public void initialize() {

        textfields = List.of(service_name_field, price_field, internal_notes_field);
        List<String> promptTexts = List.of(BARBER_SERVICE_NAME, BARBER_SERVICE_PRICE, BARBER_SERVICE_INTERNAL_NOTES);
        Map<TextField, String> textfieldPrompttextMap = generateMap(textfields, promptTexts);

        setPromptTextOnMap(textfieldPrompttextMap);

        configureButtonActions();

        configurePriceTextfieldRestrictions(price_field);

        loadEnumsOnComboBox(category_combo_box, BarberServiceCategory.values());
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

        } catch (RuntimeException exception) {

            showExceptionErrorMessage(exception, error_message_label, error_message_container);
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
    public void configureButtonActions(BarberServiceInfoDTO... infoDTO) {

        back_button.setOnAction(_ -> redirectToView(applicationContext, ViewRedirection.BARBER_SERVICES));
        clean_fields_button.setOnAction(_ -> resetForm());
        save_button.setOnAction(_ -> registerNewBarberService());
    }

    @Override
    public void resetForm(BarberServiceInfoDTO... infoDTO) {

        textfields = List.of(service_name_field, price_field, internal_notes_field);

        cleanTextfields(textfields);
    }
}
