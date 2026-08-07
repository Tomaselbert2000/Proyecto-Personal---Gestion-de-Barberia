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
            paymentMethodName,
            statusLabel,
            modifierTypeLabel,
            paymentMethodDescription,
            priceModifierLabel;

    @FXML
    private VBox
            statusBadge,
            modifierBadge;

    @FXML
    private MFXButton
            editButton,
            toggleStatusButton;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void toggleBadgeAndButtonTexts(Boolean isActive) {

        if (isActive) {

            setTextOnLabel(statusLabel, "Activo");
            addNodeStyleClass(statusBadge, ITEM_STATUS_ACTIVO);

            setTextOnButton(toggleStatusButton, "Desactivar");

        } else {

            setTextOnLabel(statusLabel, "Inactivo");
            addNodeStyleClass(statusBadge, ITEM_STATUS_INACTIVO);

            setTextOnButton(toggleStatusButton, "Activar");
        }
    }

    @Override
    public void setDataOnItem(PaymentMethodInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        toggleBadgeAndButtonTexts(infoDTO.getIsActive());

        String paymentMethodNameValue = infoDTO.getName();
        String description = infoDTO.getDescription();
        String modifierType = infoDTO.getModifierType().getDisplayName();
        String priceModifierValue = parseNumberValueToText(infoDTO.getPriceModifier());

        Map<Label, String> map = Map.of(
                paymentMethodName, paymentMethodNameValue,
                paymentMethodDescription, description,
                modifierTypeLabel, modifierType,
                priceModifierLabel, CURRENCY_STRING_ARG + priceModifierValue
        );

        setTextsOnLabelMap(map);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditPaymentMethodInfo,
                toggleStatusButton, this::changePaymentMethodActiveValue
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