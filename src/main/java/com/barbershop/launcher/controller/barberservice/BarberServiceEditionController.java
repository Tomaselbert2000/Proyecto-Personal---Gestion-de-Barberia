package com.barbershop.launcher.controller.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.dto.barbershopservice.BarberServiceUpdateDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.exceptions.barberservice.BlankBarberServicePriceException;
import com.barbershop.service.interfaces.BarberserviceService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.redirectToView;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showExceptionErrorMessage;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.utils.strings.RegexPatterns.PRICE_REGEX;

@Component
@RequiredArgsConstructor
public class BarberServiceEditionController {

    private final BarberserviceService barberserviceService;
    private final ApplicationContext applicationContext;

    @FXML
    private TextField service_id_field;

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
    private Button restore_values_button;

    @FXML
    private Button update_button;

    @FXML
    public void initialize(BarberServiceInfoDTO infoDTO) {

        loadServiceDataForEdition(infoDTO);

        loadEnumsOnComboBox(category_combo_box, BarberServiceCategory.values());
        removeFirstItemFromComboBox(category_combo_box);

        configureButtonActions(infoDTO);

        configurePriceTextfieldRestrictions();
    }

    public void loadServiceDataForEdition(BarberServiceInfoDTO infoDTO) {

        List<TextField> textfields = List.of(service_id_field, service_name_field, price_field, internal_notes_field);
        List<String> texts = List.of(infoDTO.getBarberServiceId().toString(), infoDTO.getName(), infoDTO.getPrice().toString(), infoDTO.getInternalNotes());

        Map<TextField, String> map = generateMap(textfields, texts);

        setTextsOnTextfieldMap(map);
    }

    private void configureButtonActions(BarberServiceInfoDTO infoDTO) {

        back_button.setOnAction(_ -> redirectToView(applicationContext, ViewRedirection.BARBER_SERVICES));
        restore_values_button.setOnAction(_ -> resetForm(infoDTO));
        update_button.setOnAction(_ -> updateBarberService());
    }

    private void configurePriceTextfieldRestrictions() {

        UnaryOperator<TextFormatter.Change> unaryOperatorFilter = generateUnaryOperatorFilterForTextFormatterWith(PRICE_REGEX);

        TextFormatter<String> stringTextFormatter = generateTextFormatterWithFilter(unaryOperatorFilter);

        price_field.setTextFormatter(stringTextFormatter);
    }

    private void resetForm(BarberServiceInfoDTO infoDTO) {

        List<TextField> textfieldsToClean = List.of(service_name_field, price_field, internal_notes_field);

        cleanTextfields(textfieldsToClean);

        loadServiceDataForEdition(infoDTO);

        loadEnumsOnComboBox(category_combo_box, BarberServiceCategory.values());
        removeFirstItemFromComboBox(category_combo_box);
    }

    private void updateBarberService() {

        try {

            Long id = Long.valueOf(service_id_field.getText());
            String newName = service_name_field.getText();

            if (price_field.getText().isBlank()) throw new BlankBarberServicePriceException();

            String newPrice = price_field.getText();
            BarberServiceCategory newCategory = category_combo_box.getValue();
            String newInternalNotes = internal_notes_field.getText();

            BarberServiceUpdateDTO updateDTO = buildDTOFromAttributes(newName, newPrice, newCategory, newInternalNotes);

            barberserviceService.updateService(id, updateDTO);

            showToastNotification(
                    anchor_pane,
                    applicationContext,
                    BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE,
                    ToastNotificationType.SUCCESSFUL
            );

        } catch (RuntimeException exception) {

            showExceptionErrorMessage(exception, error_message_label, error_message_container);
        }
    }

    private BarberServiceUpdateDTO buildDTOFromAttributes(String newName, String newPrice, BarberServiceCategory newCategory, String newInternalNotes) {

        return BarberServiceUpdateDTO.builder()
                .name(newName)
                .price(Double.valueOf(newPrice))
                .serviceCategory(newCategory)
                .internalNotes(newInternalNotes)
                .build();
    }
}
