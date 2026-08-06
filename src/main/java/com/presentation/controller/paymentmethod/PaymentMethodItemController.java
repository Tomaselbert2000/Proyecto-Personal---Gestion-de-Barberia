package com.presentation.controller.paymentmethod;

import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.presentation.controller.item.ItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.presentation.constants.CssStylesStrings.ITEM_STATUS_ACTIVO;
import static com.presentation.constants.CssStylesStrings.ITEM_STATUS_INACTIVO;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class PaymentMethodItemController implements ItemController<PaymentMethodInfoDTO> {

    private PaymentMethodInfoDTO infoDTOReference;

    private Consumer<PaymentMethodInfoDTO>
            onEditCallback,
            onActiveToggleCallback;

    @FXML
    private Label
            payment_method_name,
            status_label,
            modifier_type_label,
            payment_method_description,
            price_modifier_label;

    @FXML
    private VBox
            status_badge,
            modifier_badge;

    @FXML
    private MFXButton
            edit_button,
            toggle_status_button;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void toggleBadgeAndButtonTexts(Boolean isActive) {

        if (isActive) {

            setTextOnLabel(status_label, "Activo");
            addNodeStyleClass(status_badge, ITEM_STATUS_ACTIVO);

            setTextOnButton(toggle_status_button, "Desactivar");

        } else {

            setTextOnLabel(status_label, "Inactivo");
            addNodeStyleClass(status_badge, ITEM_STATUS_INACTIVO);

            setTextOnButton(toggle_status_button, "Activar");
        }
    }

    @Override
    public void setDataOnItem(PaymentMethodInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        toggleBadgeAndButtonTexts(infoDTO.getIsActive());

        String paymentMethodName = infoDTO.getName();
        String description = infoDTO.getDescription();
        String modifierType = infoDTO.getModifierType().getDisplayName();
        String priceModifierValue = parseNumberValueToText(infoDTO.getPriceModifier());

        Map<Label, String> map = Map.of(
                payment_method_name, paymentMethodName,
                payment_method_description, description,
                modifier_type_label, modifierType,
                price_modifier_label, CURRENCY_STRING_ARG + priceModifierValue
        );

        setTextsOnLabelMap(map);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                edit_button, this::goToEditPaymentMethodInfo,
                toggle_status_button, this::changePaymentMethodActiveValue
        );

        configureRunnableMaps(map);
    }

    private void goToEditPaymentMethodInfo() {

        if (onEditCallback != null) onEditCallback.accept(infoDTOReference);
    }

    private void changePaymentMethodActiveValue() {

        boolean currentIsActiveValue = infoDTOReference.getIsActive();

        infoDTOReference.setIsActive(!currentIsActiveValue);

        toggleBadgeAndButtonTexts(infoDTOReference.getIsActive());

        if (onActiveToggleCallback != null) onActiveToggleCallback.accept(infoDTOReference);
    }
}