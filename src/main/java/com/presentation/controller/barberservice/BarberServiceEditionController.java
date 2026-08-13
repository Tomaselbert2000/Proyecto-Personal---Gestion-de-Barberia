package com.presentation.controller.barberservice;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
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

import java.util.Map;

import static com.enums.ViewRedirection.BARBER_SERVICES;
import static com.presentation.constants.StringResource.ToastNotificationMessage.BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.BARBER_SERVICE_UPDATE_VALIDATION_FAILED;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.format.NumberParser.parsePrice;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
public class BarberServiceEditionController extends BaseCrudFormController<BarberServiceUpdateDTO, BarberServiceInfoDTO> {

    private final BarberserviceService barberserviceService;

    public BarberServiceEditionController(ApplicationContext applicationContext, BarberserviceService barberserviceService) {

        super(applicationContext);
        this.barberserviceService = barberserviceService;
    }

    private BarberServiceInfoDTO internalReferenceToInfoDTO;

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
    public void initialize(BarberServiceInfoDTO infoDTO) {

        internalReferenceToInfoDTO = infoDTO;

        loadServiceDataForEdition(infoDTO);
        configureUI();
        configureButtonActions();
    }

    private void configureUI() {

        loadEnumsOnComboBox(categoryComboBox, BarberServiceCategory.values());

        removeFirstItemFromComboBox(categoryComboBox);

        configureDecimalTextfieldRestrictions(priceField);
    }

    private void loadServiceDataForEdition(BarberServiceInfoDTO infoDTO) {

        Map<TextField, String> map = Map.of(
                serviceIdField, infoDTO.getBarberServiceId().toString(),
                serviceNameField, infoDTO.getName(),
                priceField, infoDTO.getPrice().toString(),
                internalNotesField, infoDTO.getInternalNotes()
        );

        categoryComboBox.setValue(infoDTO.getCategory());

        setTextsOnTextfieldMap(map);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                backButton, () -> redirectToView(BARBER_SERVICES, anchorPane, getApplicationContext()),
                restoreValuesButton, this::resetForm,
                updateButton, this::saveEntity
        );

        configureRunnableMaps(map);
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(BarberServiceUpdateDTO dto) {

        barberserviceService.updateService(internalReferenceToInfoDTO.getBarberServiceId(), dto);
    }

    @Override
    protected String getSuccessMessage() {

        return BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return BARBER_SERVICE_UPDATE_VALIDATION_FAILED;
    }

    @Override
    protected BarberServiceUpdateDTO buildDTO() {

        String newName = serviceNameField.getText();
        Double newPrice = parsePrice(priceField.getText());
        BarberServiceCategory newCategory = categoryComboBox.getValue();
        String newInternalNotes = internalNotesField.getText();

        return BarberServiceUpdateDTO.builder()
                .name(newName)
                .price(newPrice)
                .serviceCategory(newCategory)
                .internalNotes(newInternalNotes)
                .build();
    }

    @Override
    protected void resetForm() {

        loadServiceDataForEdition(internalReferenceToInfoDTO);
    }
}