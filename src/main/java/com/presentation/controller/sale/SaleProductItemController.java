package com.presentation.controller.sale;

import com.dto.product.ProductItemDTO;
import com.presentation.controller.AbstractItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.presentation.controller.sale.SaleProductItemController.UnitCounter.DECREASE;
import static com.presentation.controller.sale.SaleProductItemController.UnitCounter.INCREASE;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;

@Component
@Setter
public class SaleProductItemController extends AbstractItemController<ProductItemDTO> {

    private Consumer<ProductItemDTO> onQuantityChangeCallback;
    private BiConsumer<Node, ProductItemDTO> onDeleteProductCallback;

    @FXML
    private HBox hbox;

    @FXML
    private Label productID;

    @FXML
    private MFXButton
            removeUnitButton,
            addUnitButton,
            removeProductButton;

    @FXML
    private TextField unitTextfield;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(removeUnitButton, () -> updateCounter(DECREASE)),
                Map.entry(addUnitButton, () -> updateCounter(INCREASE)),
                Map.entry(removeProductButton, this::removeProductFromList)
        );

        configureRunnableMaps(map);
    }

    @Override
    public void setDataOnItem(ProductItemDTO item) {

        infoDTOReference = item;

        productID.setText(parseNumberValueToText(infoDTOReference.getProductID()));

        unitTextfield.setText(parseNumberValueToText(infoDTOReference.getQuantity()));
    }

    private void updateCounter(UnitCounter value) {

        switch (value) {

            case DECREASE -> {

                if (infoDTOReference.getQuantity() - 1 > 0) {

                    infoDTOReference.setQuantity(infoDTOReference.getQuantity() - 1);

                } else {

                    return;
                }
            }

            case INCREASE -> infoDTOReference.setQuantity(infoDTOReference.getQuantity() + 1);
        }

        updateTextfieldValue(infoDTOReference.getQuantity());

        fire(onQuantityChangeCallback);
    }

    private void removeProductFromList() {

        fire(hbox, onDeleteProductCallback);
    }

    private void updateTextfieldValue(Integer quantity) {

        unitTextfield.setText(parseNumberValueToText(quantity));
    }

    enum UnitCounter {

        INCREASE,
        DECREASE
    }
}