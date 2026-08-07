package com.presentation.controller.paymentmethod;

import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.ToastNotificationType;
import com.exceptions.paymentmethod.DuplicatedPaymentMethodNameException;
import com.service.interfaces.PaymentMethodService;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.enums.ViewRedirection.PAYMENT_METHODS;
import static com.presentation.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.PAYMENT_METHOD_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.PAYMENT_METHOD_EDITION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.controller.paymentmethod.PaymentMethodControllerHelper.convertPriceStringToDouble;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class PaymentMethodEditionController {

    private final ApplicationContext applicationContext;
    private final PaymentMethodService paymentMethodService;
    private PaymentMethodInfoDTO infoDTOReference;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            resetFormButton,
            saveButton;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<PaymentMethodModifierType> modifierTypeCombo;

    @FXML
    private TextField
            descriptionField,
            priceModifierField;

    @FXML
    private CheckBox isActiveCheck;

    @FXML
    public void initialize(PaymentMethodInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        configureButtonActions(infoDTOReference);

        configureDecimalTextfieldRestrictions(priceModifierField);

        loadPaymentMethodDataForEdition(infoDTO);

        loadEnumsOnComboBox(modifierTypeCombo, PaymentMethodModifierType.values());

        setStringConverter(modifierTypeCombo, PaymentMethodModifierType.TODOS);

        removeFirstItemFromComboBox(modifierTypeCombo);
    }

    private void configureButtonActions(PaymentMethodInfoDTO infoDTO) {

        Map<Button, Runnable> map = Map.of(
                saveButton, this::updatePaymentMethod,
                backButton, () -> redirectToView(PAYMENT_METHODS, anchorPane, applicationContext),
                resetFormButton, () -> resetForm(infoDTO)
        );

        configureRunnableMaps(map);
    }

    private void resetForm(PaymentMethodInfoDTO infoDTO) {

        cleanTextfields(List.of(nameField, descriptionField, priceModifierField));
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
    }

    private void updatePaymentMethod() {

        try {

            String newName = nameField.getText();
            String newDescription = descriptionField.getText();
            Boolean isNowActive = isActiveCheck.isSelected();
            PaymentMethodModifierType newModifierType = modifierTypeCombo.getValue();
            double newPriceModifierValue = convertPriceStringToDouble(priceModifierField.getText());

            PaymentMethodUpdateDTO updateDTO = buildUpdateDTO(newName, newDescription, isNowActive, newModifierType, newPriceModifierValue);

            paymentMethodService.updatePaymentMethod(infoDTOReference.getId(), updateDTO);

            showToastNotification(anchorPane, applicationContext, PAYMENT_METHOD_UPDATE_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

            loadPaymentMethodDataForEdition(infoDTOReference);

        } catch (ConstraintViolationException | DuplicatedPaymentMethodNameException exception) {

            String errorMessage;

            if (exception instanceof ConstraintViolationException) {

                errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);
            } else {

                errorMessage = exception.getMessage();
            }

            showWindowAlert(VALIDATION_ERROR_TITLE, PAYMENT_METHOD_EDITION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchorPane));
        }
    }

    private PaymentMethodUpdateDTO buildUpdateDTO(
            String newName,
            String newDescription,
            Boolean isNowActive,
            PaymentMethodModifierType newModifierType,
            double newPriceModifierValue) {

        return PaymentMethodUpdateDTO.builder()
                .newName(newName)
                .newDescription(newDescription)
                .isActive(isNowActive)
                .newModifierType(newModifierType)
                .priceModifier(newPriceModifierValue)
                .build();
    }
}