package com.barbershop.launcher.controller.product;

import com.barbershop.dto.product.ProductInfoDTO;
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

import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.FileImageHelper.loadFileOnImageView;

@Component
@Getter
@Setter
public class ProductItemController {

    private ProductInfoDTO infoDTOReference;

    private Consumer<ProductInfoDTO> onEditCallback;
    private Consumer<ProductInfoDTO> onAddStockCallback;

    private static final Image IMAGE_PLACEHOLDER;
    private static final String PLACEHOLDER_PATH = "/images/placeholder-image.jpg";

    static {

        IMAGE_PLACEHOLDER = new Image(Objects.requireNonNull(ProductItemController.class.getResource(PLACEHOLDER_PATH)).toExternalForm());
    }

    @FXML
    private VBox stock_status_badge_vbox;

    @FXML
    private ImageView product_image_placeholder;

    @FXML
    private Label product_name;

    @FXML
    private Label stock_status_text;

    @FXML
    private Label product_cost;

    @FXML
    private Label product_price;

    @FXML
    private Label product_profit;

    @FXML
    private Label current_stock;

    @FXML
    private Label safety_stock;

    @FXML
    private Button edit_button;

    @FXML
    private Button add_stock_button;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                edit_button, this::goToEditProductView,
                add_stock_button, this::goToAddStockView
        );

        configureRunnableMaps(map);
    }

    private void goToEditProductView() {

        if (onEditCallback != null) onEditCallback.accept(infoDTOReference);
    }

    private void goToAddStockView() {

        if (onAddStockCallback != null) onAddStockCallback.accept(infoDTOReference);
    }

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

    private static @NonNull List<String> getStrings(ProductInfoDTO infoDTO, String name, String stockStatusText) {
        String productCost = formatAsPrice(infoDTO.getProductCost());
        String productPrice = formatAsPrice(infoDTO.getCurrentPrice());
        String profit = formatAsPercentage(infoDTO.getCurrentProfitPercentage());
        String currentStock = infoDTO.getCurrentStockLevel().toString();
        String safetyStock = "Min: " + infoDTO.getSafetyStockLevel().toString();

        return List.of(name, stockStatusText, productCost, productPrice, profit, currentStock, safetyStock);
    }
}
