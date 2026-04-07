package com.barbershop.launcher.controller.product;

import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.ProductPresentationUnit;
import com.barbershop.service.interfaces.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.controller.UI_RenderingFunctions.*;

@Component
@RequiredArgsConstructor
public class ProductEditionController {

    private final ApplicationContext applicationContext;
    private final ProductService productService;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Button back_button;

    @FXML
    private TextField name;

    @FXML
    private ComboBox<ProductCategory> category;

    @FXML
    private TextField optional_description;

    @FXML
    private TextField brand_name;

    @FXML
    private TextField presentation_size;

    @FXML
    private ComboBox<ProductPresentationUnit> presentation_unit;

    @FXML
    private TextField product_cost;

    @FXML
    private TextField current_price;

    @FXML
    private Label profit_margin_value;

    @FXML
    private TextField product_wholesale_price;

    @FXML
    private TextField min_price;

    @FXML
    private TextField max_discount_percentage;

    @FXML
    private TextField current_stock_level;

    @FXML
    private TextField safety_stock_level;

    @FXML
    private ImageView product_image_preview;

    @FXML
    private Button select_image_button;

    @FXML
    private Button remove_image_button;

    @FXML
    private Button save_button;

    @FXML
    public void initialize(ProductInfoDTO infoDTO){

        loadProductDataForEdition(infoDTO);
    }

    private void loadProductDataForEdition(ProductInfoDTO infoDTO) {

        List<TextField> textfields = List.of(
                name,
                optional_description,
                brand_name,
                presentation_size,
                product_cost,
                current_price,
                product_wholesale_price,
                min_price,
                max_discount_percentage,
                current_stock_level,
                safety_stock_level
                );
    }
}
