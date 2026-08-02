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
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;
import static com.presentation.controller.paymentmethod.PaymentMethodControllerHelper.convertPriceStringToDouble;

@Component
@RequiredArgsConstructor
public class PaymentMethodEditionController {

    private final ApplicationContext applicationContext;
    private final PaymentMethodService paymentMethodService;
    private PaymentMethodInfoDTO infoDTOReference;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton
            back_button,
            reset_form_button,
            save_button;

    @FXML
    private TextField name_field;

    @FXML
    private ComboBox<PaymentMethodModifierType> modifier_type_combo;

    @FXML
    private TextField
            description_field,
            price_modifier_field;

    @FXML
    private CheckBox is_active_check;

    @FXML
    public void initialize(PaymentMethodInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        configureButtonActions(infoDTOReference);

        configureDecimalTextfieldRestrictions(price_modifier_field);

        loadPaymentMethodDataForEdition(infoDTO);

        loadEnumsOnComboBox(modifier_type_combo, PaymentMethodModifierType.values());

        setStringConverter(modifier_type_combo, PaymentMethodModifierType.TODOS);

        removeFirstItemFromComboBox(modifier_type_combo);
    }

    private void configureButtonActions(PaymentMethodInfoDTO infoDTO) {

        Map<Button, Runnable> map = Map.of(
                save_button, this::updatePaymentMethod,
                back_button, () -> redirectToView(PAYMENT_METHODS, anchor_pane, applicationContext),
                reset_form_button, () -> resetForm(infoDTO)
        );

        configureRunnableMaps(map);
    }

    private void resetForm(PaymentMethodInfoDTO infoDTO) {

        cleanTextfields(List.of(name_field, description_field, price_modifier_field));
        loadPaymentMethodDataForEdition(infoDTO);
    }

    private void loadPaymentMethodDataForEdition(PaymentMethodInfoDTO infoDTO) {

        Map<TextField, String> map = Map.of(
                name_field, infoDTO.getName(),
                description_field, infoDTO.getDescription(),
                price_modifier_field, parseNumberValueToText(infoDTO.getPriceModifier())
        );

        setTextsOnTextfieldMap(map);

        is_active_check.setSelected(infoDTO.getIsActive());

        modifier_type_combo.setValue(infoDTO.getModifierType());
    }

    private void updatePaymentMethod() {

        try {

            String newName = name_field.getText();
            String newDescription = description_field.getText();
            Boolean isNowActive = is_active_check.isSelected();
            PaymentMethodModifierType newModifierType = modifier_type_combo.getValue();
            double newPriceModifierValue = convertPriceStringToDouble(price_modifier_field.getText());

            PaymentMethodUpdateDTO updateDTO = buildUpdateDTO(newName, newDescription, isNowActive, newModifierType, newPriceModifierValue);

            paymentMethodService.updatePaymentMethod(infoDTOReference.getId(), updateDTO);

            showToastNotification(anchor_pane, applicationContext, PAYMENT_METHOD_UPDATE_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

            loadPaymentMethodDataForEdition(infoDTOReference);

        } catch (ConstraintViolationException | DuplicatedPaymentMethodNameException exception) {

            String errorMessage;

            if (exception instanceof ConstraintViolationException) {

                errorMessage = getConstraintViolationsList((ConstraintViolationException) exception);
            } else {

                errorMessage = exception.getMessage();
            }

            showWindowAlert(VALIDATION_ERROR_TITLE, PAYMENT_METHOD_EDITION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchor_pane));
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