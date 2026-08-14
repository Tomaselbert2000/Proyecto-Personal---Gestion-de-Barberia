package com.presentation.controller.paymentmethod;

import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.enums.PaymentMethodModifierType;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.PaymentMethodService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.enums.ViewRedirection.PAYMENT_METHODS;
import static com.presentation.constants.StringResource.ToastNotificationMessage.PAYMENT_METHOD_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.PAYMENT_METHOD_EDITION_VALIDATION_FAILED;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextsOnTextfieldMap;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.format.NumberParser.parseTextToDouble;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
public class PaymentMethodEditionController extends BaseCrudFormController<PaymentMethodUpdateDTO, PaymentMethodInfoDTO> {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodEditionController(ApplicationContext applicationContext, PaymentMethodService paymentMethodService) {

        super(applicationContext);
        this.paymentMethodService = paymentMethodService;
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            resetFormButton,
            saveButton;

    @FXML
    private TextField nameField, descriptionField, priceModifierField;

    @FXML
    private ComboBox<PaymentMethodModifierType> modifierTypeCombo;

    @FXML
    private CheckBox isActiveCheck;

    @FXML
    public void initialize(PaymentMethodInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        configureButtonActions();

        loadPaymentMethodDataForEdition(infoDTO);
    }

    private void loadPaymentMethodDataForEdition(PaymentMethodInfoDTO infoDTO) {

        Map<TextField, String> map = Map.of(
                nameField, infoDTO.getName(),
                descriptionField, infoDTO.getDescription(),
                priceModifierField, parseNumberValueToText(infoDTO.getPriceModifier())
        );

        setTextsOnTextfieldMap(map);

        isActiveCheck.setSelected(infoDTO.getIsActive());

        modifierTypeCombo.setValue(infoDTO.getModifierType());

        loadEnumsOnComboBox(modifierTypeCombo, PaymentMethodModifierType.values());

        setStringConverter(modifierTypeCombo, PaymentMethodModifierType.TODOS);

        removeFirstItemFromComboBox(modifierTypeCombo);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                saveButton, this::saveEntity,
                backButton, () -> redirectToView(PAYMENT_METHODS, getAnchorPane(), getApplicationContext()),
                resetFormButton, this::resetForm
        );

        configureRunnableMaps(map);
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(PaymentMethodUpdateDTO updateDTO) {

        paymentMethodService.updatePaymentMethod(infoDTOReference.getId(), updateDTO);
    }

    @Override
    protected String getSuccessMessage() {

        return PAYMENT_METHOD_UPDATE_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return PAYMENT_METHOD_EDITION_VALIDATION_FAILED;
    }

    @Override
    protected PaymentMethodUpdateDTO buildDTO() {

        String newName = nameField.getText();
        String newDescription = descriptionField.getText();
        Boolean isNowActive = isActiveCheck.isSelected();
        PaymentMethodModifierType newModifierType = modifierTypeCombo.getValue();
        double newPriceModifierValue = parseTextToDouble(priceModifierField.getText(), 0.0);

        return PaymentMethodUpdateDTO.builder()
                .newName(newName)
                .newDescription(newDescription)
                .isActive(isNowActive)
                .newModifierType(newModifierType)
                .priceModifier(newPriceModifierValue)
                .build();
    }

    @Override
    protected void resetForm() {

        loadPaymentMethodDataForEdition(infoDTOReference);
    }
}