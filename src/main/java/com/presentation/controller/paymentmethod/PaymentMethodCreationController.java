package com.presentation.controller.paymentmethod;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.enums.PaymentMethodModifierType;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.PaymentMethodService;
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

import static com.enums.ViewRedirection.PAYMENT_METHODS;
import static com.presentation.constants.ControllerConstants.NULL_NUMERIC_INPUT_VALUE;
import static com.presentation.constants.PromptTexts.PaymentMethodPromptText.*;
import static com.presentation.constants.StringResource.ToastNotificationMessage.PAYMENT_METHOD_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.PAYMENT_METHOD_CREATION_VALIDATION_FAILED;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.format.NumberParser.parseTextToDouble;
import com.presentation.support.view.ViewRedirectionHelper;

@Component
public class PaymentMethodCreationController extends BaseCrudFormController<PaymentMethodCreationDTO, PaymentMethodInfoDTO> {

    private final PaymentMethodService paymentMethodService;

    private final ViewRedirectionHelper viewRedirectionHelper;

    public PaymentMethodCreationController(ApplicationContext applicationContext, PaymentMethodService paymentMethodService, ViewRedirectionHelper viewRedirectionHelper) {

        super(applicationContext);
        this.paymentMethodService = paymentMethodService;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            resetFormButton,
            saveButton;

    @FXML
    private TextField
            nameField,
            descriptionField,
            priceModifierField;

    @FXML
    private ComboBox<PaymentMethodModifierType> modifierTypeCombo;

    @FXML
    public void initialize() {

        configurePromptTexts();
        configureButtonActions();

        loadEnumsOnComboBox(modifierTypeCombo, PaymentMethodModifierType.values());

        setStringConverter(modifierTypeCombo, PaymentMethodModifierType.TODOS);

        removeFirstItemFromComboBox(modifierTypeCombo);

        configureDecimalTextfieldRestrictions(priceModifierField);
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(PaymentMethodCreationDTO paymentMethodCreationDTO) {

        paymentMethodService.registerNewPaymentMethod(paymentMethodCreationDTO);
    }

    @Override
    protected String getSuccessMessage() {

        return PAYMENT_METHOD_CREATION_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return PAYMENT_METHOD_CREATION_VALIDATION_FAILED;
    }

    @Override
    protected PaymentMethodCreationDTO buildDTO() {

        String paymentMethodName = nameField.getText();
        String paymentMethodDescription = descriptionField.getText();
        double priceModifierDoubleValue = parseTextToDouble(priceModifierField.getText(), NULL_NUMERIC_INPUT_VALUE);
        PaymentMethodModifierType modifierType = modifierTypeCombo.getValue();

        return PaymentMethodCreationDTO.builder()
                .name(paymentMethodName)
                .description(paymentMethodDescription)
                .priceModifierType(modifierType)
                .priceModifier(priceModifierDoubleValue)
                .build();
    }

    @Override
    protected void resetForm() {

        cleanTextfields(List.of(nameField, descriptionField, priceModifierField));
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(backButton, () -> viewRedirectionHelper.redirectToView(PAYMENT_METHODS, getAnchorPane(), getApplicationContext())),
                Map.entry(resetFormButton, this::resetForm),
                Map.entry(saveButton, this::saveEntity)
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                nameField, PAYMENT_METHOD_NAME,
                descriptionField, PAYMENT_METHOD_DESCRIPTION,
                priceModifierField, PAYMENT_METHOD_PRICE_VALUE
        );

        setPromptTextOnMap(map);
    }
}