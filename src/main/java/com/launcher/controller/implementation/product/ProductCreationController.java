package com.launcher.controller.implementation.product;

import com.dto.product.ProductCreationDTO;
import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.launcher.controller.helper.ValidationFormatter;
import com.launcher.controller.interfaces.CreationController;
import com.launcher.controller.interfaces.ProductController;
import com.service.interfaces.ProductService;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.launcher.constants.ui.messages.ToastNotificationMessage.PRODUCT_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.launcher.constants.ui.messages.ValidationErrorMessage.PRODUCT_CREATION_VALIDATION_FAILED;
import static com.launcher.constants.ui.messages.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.launcher.constants.ui.prompt_text.ProductPromptText.*;
import static com.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.launcher.controller.helper.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.launcher.controller.helper.ContainerManager.getCurrentWindow;
import static com.launcher.controller.helper.FileImageHelper.*;
import static com.launcher.controller.helper.HelperConstants.ACCEPT_BUTTON_TEXT;
import static com.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.*;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class ProductCreationController implements CreationController, ProductController {

    private final ApplicationContext applicationContext;
    private final ProductService productService;

    private String filePath = "";

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton
            back_button,
            select_image_button,
            remove_image_button,
            reset_form_button,
            save_button;

    @FXML
    private TextField
            name,
            optional_description,
            brand_name,
            product_presentation_field,
            cost,
            current_price,
            wholesale_price,
            min_price,
            max_discount,
            current_stock_level,
            safety_stock_level;

    @FXML
    private ComboBox<ProductCategory> product_category_selector;

    @FXML
    private ComboBox<ProductPresentationUnit> presentation_unit_combobox;

    @FXML
    private Label profit_margin_value;

    @FXML
    private ImageView product_image_preview;

    @FXML
    public void initialize() {

        configurePromptTexts();

        loadEnumsOnComboBox(product_category_selector, ProductCategory.values());
        loadEnumsOnComboBox(presentation_unit_combobox, ProductPresentationUnit.values());

        setStringConverter(product_category_selector, ProductCategory.TODOS);
        removeFirstItemFromComboBox(product_category_selector);

        setStringConverter(presentation_unit_combobox, ProductPresentationUnit.TODOS);
        removeFirstItemFromComboBox(presentation_unit_combobox);

        configureButtonActions();
    }

    private void registerNewProduct() {

        try {

            String productName = name.getText();
            ProductPresentationUnit presentationUnit = presentation_unit_combobox.getValue();
            String optionalProductDescription = optional_description.getText();
            ProductCategory productCategory = product_category_selector.getValue();
            String brandName = brand_name.getText();
            String presentationSize = product_presentation_field.getText();
            String productCost = cost.getText();
            String minPrice = min_price.getText();
            String currentPrice = current_price.getText();
            String wholeSalePrice = wholesale_price.getText();
            String maxDiscountPercentage = max_discount.getText();
            String currentStockLevel = current_stock_level.getText();
            String safetyStockLevel = safety_stock_level.getText();

            ProductCreationDTO creationDTO = buildDTOFromAttributes(productName, presentationUnit, optionalProductDescription, productCategory, brandName, presentationSize, productCost, minPrice, currentPrice, wholeSalePrice, maxDiscountPercentage, currentStockLevel, safetyStockLevel);

            productService.registerNewProduct(creationDTO);

            showToastNotification(anchor_pane, applicationContext, PRODUCT_CREATION_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

            List<TextField> textfields = List.of(
                    current_stock_level,
                    safety_stock_level,
                    cost,
                    min_price,
                    current_price,
                    wholesale_price,
                    max_discount,
                    name,
                    brand_name,
                    product_presentation_field,
                    optional_description
            );

            cleanTextfields(textfields);

            cleanImageView(product_image_preview);

        } catch (ConstraintViolationException exception) {

            String errorMessages = getConstraintViolationsList(exception);

            showWindowAlert(VALIDATION_ERROR_TITLE, PRODUCT_CREATION_VALIDATION_FAILED, errorMessages, Alert.AlertType.ERROR, ACCEPT_BUTTON_TEXT, getCurrentWindow(anchor_pane));
        }
    }

    private ProductCreationDTO buildDTOFromAttributes(
            String productName,
            ProductPresentationUnit presentationUnit,
            String optionalProductDescription,
            ProductCategory productCategory,
            String brandName,
            String presentationSize,
            String productCost,
            String minPrice,
            String currentPrice,
            String wholeSalePrice,
            String maxDiscountPercentage,
            String currentStockLevel,
            String safetyStockLevel
    ) {

        Integer parsedPresentationSize = parseTextToInteger(presentationSize);
        Integer parsedCurrentStock = parseTextToInteger(currentStockLevel);
        Integer parsedSafetyStockLevel = parseTextToInteger(safetyStockLevel);

        Double parsedCost = ValidationFormatter.parseTextToDouble(productCost);
        Double parsedMinPrice = parseTextToDouble(minPrice);
        Double parsedCurrentPrice = parseTextToDouble(currentPrice);
        Double parsedWholeSalePrice = parseTextToDouble(wholeSalePrice);
        Double parsedMaxDiscountPercentage = parseTextToDouble(maxDiscountPercentage);

        return ProductCreationDTO.builder()
                .name(productName)
                .optionalDescription(optionalProductDescription)
                .brandName(brandName)
                .presentationUnit(presentationUnit)
                .presentationSize(parsedPresentationSize)
                .productCost(parsedCost)
                .minPrice(parsedMinPrice)
                .currentPrice(parsedCurrentPrice)
                .productWholeSalePrice(parsedWholeSalePrice)
                .maxDiscountPercentage(parsedMaxDiscountPercentage)
                .category(productCategory)
                .currentStockLevel(parsedCurrentStock)
                .safetyStockLevel(parsedSafetyStockLevel)
                .imageFilePath(filePath)
                .build();
    }

    @Override
    public void configurePromptTexts() {

        List<TextField> stockLevels = List.of(current_stock_level, safety_stock_level);
        setPromptTextOnList(stockLevels, STOCK_LEVEL_DEFAULT_VALUE);

        List<TextField> prices = List.of(cost, min_price, current_price, wholesale_price, max_discount);
        setPromptTextOnList(prices, PRICE_FIELD_DEFAULT_VALUE);

        Map<TextField, String> map = Map.of(
                name, PRODUCT_NAME,
                brand_name, PRODUCT_BRAND,
                product_presentation_field, PRODUCT_SIZE_OR_VOLUME,
                optional_description, PRODUCT_OPTIONAL_DESCRIPTION
        );

        setPromptTextOnMap(map);

        setTextOnLabel(profit_margin_value, DISCOUNT_PERCENTAGE_DEFAULT_VALUE);
    }

    @Override
    public void resetForm() {

        cleanTextfields(
                List.of(
                        name,
                        brand_name,
                        product_presentation_field,
                        optional_description,
                        cost,
                        min_price,
                        current_price,
                        wholesale_price,
                        max_discount,
                        current_stock_level,
                        safety_stock_level
                )
        );

        cleanImageView(product_image_preview);
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                select_image_button, this::handleImageSelection,
                remove_image_button, () -> cleanImageView(product_image_preview),
                back_button, () -> redirectToView(ViewRedirection.PRODUCTS, anchor_pane, applicationContext),
                reset_form_button, this::resetForm,
                save_button, this::registerNewProduct
        );

        configureRunnableMaps(map);
    }

    @Override
    public void handleImageSelection() {

        File file = getFileFromFileChooser(anchor_pane);

        if (file != null) {

            filePath = file.getAbsolutePath();

            loadFileOnImageView(file, product_image_preview);
        }
    }
}