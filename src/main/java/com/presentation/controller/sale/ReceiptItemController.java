package com.presentation.controller.sale;

import com.dto.sale.ReceiptItemDTO;
import com.presentation.controller.AbstractItemController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

import static com.presentation.support.control.UIBasicComponents.setTextsOnLabelMap;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;

public class ReceiptItemController extends AbstractItemController<ReceiptItemDTO> {

    @FXML
    private Label
            productName,
            quantity,
            unitPrice;

    @Override
    protected void configureButtonActions() {
    }

    @Override
    public void setDataOnItem(ReceiptItemDTO item) {

        Map<Label, String> map = Map.ofEntries(
                Map.entry(productName, item.getProductName()),
                Map.entry(quantity, parseNumberValueToText(item.getQuantity())),
                Map.entry(unitPrice, parseNumberValueToText(item.getUnitPrice()))
        );

        setTextsOnLabelMap(map);
    }
}
