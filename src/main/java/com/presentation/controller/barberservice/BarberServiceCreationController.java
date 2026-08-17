package com.presentation.controller.barberservice;

import com.dto.barberservice.BarberServiceCreationDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.enums.BarberServiceCategory;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.BarberserviceService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.enums.ViewRedirection.BARBER_SERVICES;
import static com.presentation.constants.PromptTexts.BarberServicePromptText.*;
import static com.presentation.constants.StringResource.ToastNotificationMessage.BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.BARBER_SERVICE_CREATION_VALIDATION_FAILED;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.format.NumberParser.parsePrice;
import com.presentation.support.view.ViewRedirectionHelper;

@Component
public class BarberServiceCreationController extends BaseCrudFormController<BarberServiceCreationDTO, BarberServiceInfoDTO> {

    private final BarberserviceService barberserviceService;

    private final ViewRedirectionHelper viewRedirectionHelper;

    public BarberServiceCreationController(ApplicationContext applicationContext, BarberserviceService barberserviceService, ViewRedirectionHelper viewRedirectionHelper) {

        super(applicationContext);
        this.barberserviceService = barberserviceService;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField
            serviceNameField,
            priceField,
            internalNotesField;

    @FXML
    private ComboBox<BarberServiceCategory> categoryComboBox;

    @FXML
    private MFXButton
            backButton,
            cleanFieldsButton,
            saveButton;

    @FXML
    public void initialize() {

        configureUI();
        configureButtonActions();
    }

    private void configureUI() {

        configureDecimalTextfieldRestrictions(priceField);

        loadEnumsOnComboBox(categoryComboBox, BarberServiceCategory.values());

        setStringConverter(categoryComboBox, BarberServiceCategory.TODOS);

        removeFirstItemFromComboBox(categoryComboBox);

        configurePromptTexts();
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(BarberServiceCreationDTO dto) {

        barberserviceService.registerNewBarberService(dto);
    }

    @Override
    protected String getSuccessMessage() {

        return BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {
        return BARBER_SERVICE_CREATION_VALIDATION_FAILED;
    }

    @Override
    protected BarberServiceCreationDTO buildDTO() {

        String serviceName = serviceNameField.getText();
        Double price = parsePrice(priceField.getText());
        BarberServiceCategory category = categoryComboBox.getValue();
        String internalNotes = internalNotesField.getText();

        return BarberServiceCreationDTO.builder()
                .name(serviceName).
                price(price)
                .serviceCategory(category)
                .internalNotes(internalNotes)
                .build();
    }

    @Override
    protected void resetForm() {

        cleanTextfields(List.of(serviceNameField, priceField, internalNotesField));
    }

    @Override
    protected void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                serviceNameField, BARBER_SERVICE_NAME,
                priceField, BARBER_SERVICE_PRICE,
                internalNotesField, BARBER_SERVICE_INTERNAL_NOTES
        );

        setPromptTextOnMap(map);
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                backButton, () -> viewRedirectionHelper.redirectToView(BARBER_SERVICES, getAnchorPane(), getApplicationContext()),
                cleanFieldsButton, this::resetForm,
                saveButton, this::saveEntity
        );

        configureRunnableMaps(map);
    }
}