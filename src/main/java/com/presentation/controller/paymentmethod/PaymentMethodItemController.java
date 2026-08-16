package com.presentation.controller.paymentmethod;

import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.presentation.controller.item.AbstractItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.presentation.constants.CssStylesStrings.ITEM_STATUS_ACTIVO;
import static com.presentation.constants.CssStylesStrings.ITEM_STATUS_INACTIVO;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.support.control.StatusBadgeHelper.applyBadge;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextsOnLabelMap;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;

@Component
@Getter
@Setter
public class PaymentMethodItemController extends AbstractItemController<PaymentMethodInfoDTO> {

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

        applyBadge(
                isActive,
                statusLabel,
                statusBadge,
                toggleStatusButton,
                ITEM_STATUS_ACTIVO,
                ITEM_STATUS_INACTIVO
        );
    }

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

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditPaymentMethodInfo,
                toggleStatusButton, this::changePaymentMethodActiveValue
        );

        configureRunnableMaps(map);
    }

    private void goToEditPaymentMethodInfo() {

        fire(onEditCallback);
    }

    private void changePaymentMethodActiveValue() {

        boolean currentIsActiveValue = infoDTOReference.getIsActive();

        infoDTOReference.setIsActive(!currentIsActiveValue);

        toggleBadgeAndButtonTexts(infoDTOReference.getIsActive());

        fire(onActiveToggleCallback);
    }
}