package com.launcher.controller.payment_method;

import com.dto.payment.PaymentMethodInfoDTO;
import com.dto.payment.PaymentMethodUpdateDTO;
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
import static com.launcher.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.launcher.constants.StringResource.ToastNotificationMessage.PAYMENT_METHOD_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.launcher.constants.StringResource.ValidationErrorMessage.PAYMENT_METHOD_EDITION_VALIDATION_FAILED;
import static com.launcher.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.launcher.controller.helper.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.launcher.controller.helper.ContainerManager.getCurrentWindow;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.*;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;
import static com.launcher.controller.payment_method.helper.PaymentMethodControllerHelper.convertPriceStringToDouble;

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