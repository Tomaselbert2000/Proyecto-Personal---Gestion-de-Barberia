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
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.formatAsPercentage;
import static com.presentation.support.control.ValidationFormatter.formatAsPrice;
import static com.presentation.support.io.FileImageHelper.loadFileOnImageView;

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
    private VBox stockStatusBadgeVbox;

    @FXML
    private ImageView productImagePlaceholder;

    @FXML
    private Label
            productName,
            stockStatusText,
            productCost,
            productPrice,
            productProfit,
            currentStock,
            safetyStock;

    @FXML
    private MFXButton
            editButton,
            addStockButton;

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

        List<Label> labels = List.of(productName, stockStatusText, productCost, productPrice, productProfit, currentStock, safetyStock);

        String name = infoDTO.getName();
        String stockStatusTextValue = infoDTO.getCurrentStockStatus().getDisplayName();
        List<String> texts = getStrings(infoDTO, name, stockStatusTextValue);
        Map<Label, String> map = generateMap(labels, texts);

        setTextsOnLabelMap(map);

        Image imageToShow;

        if (infoDTO.getImageFilePath() != null && !infoDTO.getImageFilePath().isBlank()) {

            File file = new File(infoDTO.getImageFilePath());

            loadFileOnImageView(file, productImagePlaceholder);

        } else {

            imageToShow = IMAGE_PLACEHOLDER;
            productImagePlaceholder.setImage(imageToShow);
        }
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditProductView,
                addStockButton, this::goToAddStockView
        );

        configureRunnableMaps(map);
    }
}
