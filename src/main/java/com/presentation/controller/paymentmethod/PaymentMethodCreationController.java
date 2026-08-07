package com.presentation.controller.paymentmethod;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.exceptions.paymentmethod.DuplicatedPaymentMethodNameException;
import com.service.interfaces.PaymentMethodService;
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

import static com.presentation.constants.ControllerConstants.NULL_NUMERIC_INPUT_VALUE;
import static com.presentation.constants.PromptTexts.PaymentMethodPromptText.*;
import static com.presentation.constants.StringResource.DisplayString.ACCEPT_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.PAYMENT_METHOD_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.PAYMENT_METHOD_CREATION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.getConstraintViolationsList;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.format.NumberParser.parseTextToDouble;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class PaymentMethodCreationController {

    private final ApplicationContext applicationContext;
    private final PaymentMethodService paymentMethodService;

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

    private void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                nameField, PAYMENT_METHOD_NAME,
                descriptionField, PAYMENT_METHOD_DESCRIPTION,
                priceModifierField, PAYMENT_METHOD_PRICE_VALUE
        );

        setPromptTextOnMap(map);
    }

    private void resetForm() {

        cleanTextfields(List.of(nameField, descriptionField, priceModifierField));
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                backButton, () -> redirectToView(ViewRedirection.PAYMENT_METHODS, anchorPane, applicationContext),
                resetFormButton, this::resetForm,
                saveButton, this::createPaymentMethod
        );

        configureRunnableMaps(map);
    }

    private void createPaymentMethod() {

        try {

            String paymentMethodName = nameField.getText();
            String paymentMethodDescription = descriptionField.getText();
            double priceModifierDoubleValue = parseTextToDouble(priceModifierField.getText(), NULL_NUMERIC_INPUT_VALUE);

            PaymentMethodModifierType modifierType = modifierTypeCombo.getValue();

            PaymentMethodCreationDTO creationDTO = buildCreationDTOFromAttributes(paymentMethodName, paymentMethodDescription, priceModifierDoubleValue, modifierType);

            paymentMethodService.registerNewPaymentMethod(creationDTO);

            showToastNotification(anchorPane, applicationContext, PAYMENT_METHOD_CREATION_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException | DuplicatedPaymentMethodNameException exception) {

            String errorMessage;

            if (exception instanceof ConstraintViolationException) {

                errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);

            } else {

                errorMessage = exception.getMessage();
            }

            showWindowAlert(VALIDATION_ERROR_TITLE, PAYMENT_METHOD_CREATION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, ACCEPT_BUTTON_TEXT, getCurrentWindow(anchorPane));
        }
    }

    private PaymentMethodCreationDTO buildCreationDTOFromAttributes(
            String paymentMethodName,
            String paymentMethodDescription,
            Double priceModifierValue,
            PaymentMethodModifierType modifierType) {

        return PaymentMethodCreationDTO.builder()
                .name(paymentMethodName)
                .description(paymentMethodDescription)
                .priceModifierType(modifierType)
                .priceModifier(priceModifierValue)
                .build();
    }
}