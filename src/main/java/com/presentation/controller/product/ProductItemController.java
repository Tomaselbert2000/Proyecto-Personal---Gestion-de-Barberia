package com.presentation.controller.product;

import com.dto.product.ProductInfoDTO;
import com.presentation.controller.item.ItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static com.presentation.constants.ControllerConstants.ProductControllerConstants.PLACEHOLDER_PATH;
import static com.presentation.support.io.FileImageHelper.loadFileOnImageView;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.formatAsPercentage;
import static com.presentation.support.control.ValidationFormatter.formatAsPrice;

@Component
@Getter
@Setter
public class ProductItemController implements ItemController<ProductInfoDTO> {

    private static final Image IMAGE_PLACEHOLDER;

    static {

        IMAGE_PLACEHOLDER = new Image(Objects.requireNonNull(ProductItemController.class.getResource(PLACEHOLDER_PATH)).toExternalForm());
    }

    private ProductInfoDTO infoDTOReference;
    private Consumer<ProductInfoDTO>
            onEditCallback,
            onAddStockCallback;
    @FXML
    private VBox stock_status_badge_vbox;

    @FXML
    private ImageView product_image_placeholder;

    @FXML
    private Label
            product_name,
            stock_status_text,
            product_cost,
            product_price,
            product_profit,
            current_stock,
            safety_stock;

    @FXML
    private MFXButton
            edit_button,
            add_stock_button;

    private static @NonNull List<String> getStrings(ProductInfoDTO infoDTO, String name, String stockStatusText) {
        String productCost = formatAsPrice(infoDTO.getProductCost());
        String productPrice = formatAsPrice(infoDTO.getCurrentPrice());
        String profit = formatAsPercentage(infoDTO.getCalculatedProfit());
        String currentStock = infoDTO.getCurrentStockLevel().toString();
        String safetyStock = "Min: " + infoDTO.getSafetyStockLevel().toString();

        return List.of(name, stockStatusText, productCost, productPrice, profit, currentStock, safetyStock);
    }

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void goToEditProductView() {

        if (onEditCallback != null) onEditCallback.accept(infoDTOReference);
    }

    private void goToAddStockView() {

        if (onAddStockCallback != null) onAddStockCallback.accept(infoDTOReference);
    }

    @Override
    public void setDataOnItem(ProductInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        List<Label> labels = List.of(product_name, stock_status_text, product_cost, product_price, product_profit, current_stock, safety_stock);

        String name = infoDTO.getName();
        String stockStatusText = infoDTO.getCurrentStockStatus().getDisplayName();
        List<String> texts = getStrings(infoDTO, name, stockStatusText);
        Map<Label, String> map = generateMap(labels, texts);

        setTextsOnLabelMap(map);

        Image imageToShow;

        if (infoDTO.getImageFilePath() != null && !infoDTO.getImageFilePath().isBlank()) {

            File file = new File(infoDTO.getImageFilePath());

            loadFileOnImageView(file, product_image_placeholder);

        } else {

            imageToShow = IMAGE_PLACEHOLDER;
            product_image_placeholder.setImage(imageToShow);
        }
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                edit_button, this::goToEditProductView,
                add_stock_button, this::goToAddStockView
        );

        configureRunnableMaps(map);
    }
}

//TODO: completar plan de refactor en OpenCode
