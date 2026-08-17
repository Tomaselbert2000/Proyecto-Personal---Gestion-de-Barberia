package com.presentation.controller.product;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.ProductService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.enums.ViewRedirection.PRODUCTS;
import static com.presentation.constants.PromptTexts.ProductPromptText.*;
import static com.presentation.constants.StringResource.ToastNotificationMessage.PRODUCT_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.PRODUCT_CREATION_VALIDATION_FAILED;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.io.FileImageHelper.*;
import com.presentation.support.view.ViewRedirectionHelper;

@Component
public class ProductCreationController extends BaseCrudFormController<ProductCreationDTO, ProductInfoDTO> {

    private final ProductService productService;

    private final ViewRedirectionHelper viewRedirectionHelper;

    public ProductCreationController(ApplicationContext applicationContext, ProductService productService, ViewRedirectionHelper viewRedirectionHelper) {

        super(applicationContext);
        this.productService = productService;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    private String filePath = "";

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            selectImageButton,
            removeImageButton,
            resetFormButton,
            saveButton;

    @FXML
    private TextField
            productName,
            optionalDescription,
            brandName,
            productPresentationField,
            productCost,
            currentPrice,
            wholesalePrice,
            minPrice,
            maxDiscount,
            currentStockLevel,
            safetyStockLevel;

    @FXML
    private ComboBox<ProductCategory> productCategorySelector;

    @FXML
    private ComboBox<ProductPresentationUnit> presentationUnitComboBox;

    @FXML
    private Label profitMarginValue;

    @FXML
    private ImageView productImagePreview;

    @FXML
    public void initialize() {

        configurePromptTexts();

        loadEnumsOnComboBox(productCategorySelector, ProductCategory.values());
        loadEnumsOnComboBox(presentationUnitComboBox, ProductPresentationUnit.values());

        setStringConverter(productCategorySelector, ProductCategory.TODOS);
        removeFirstItemFromComboBox(productCategorySelector);

        setStringConverter(presentationUnitComboBox, ProductPresentationUnit.TODOS);
        removeFirstItemFromComboBox(presentationUnitComboBox);

        configureButtonActions();
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(ProductCreationDTO dto) {

        productService.registerNewProduct(dto);
    }

    @Override
    protected String getSuccessMessage() {

        return PRODUCT_CREATION_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return PRODUCT_CREATION_VALIDATION_FAILED;
    }

    @Override
    protected ProductCreationDTO buildDTO() {

        return ProductCreationDTO.builder()
                .name(productName.getText())
                .optionalDescription(optionalDescription.getText())
                .brandName(brandName.getText())
                .presentationUnit(presentationUnitComboBox.getValue())
                .presentationSize(parseTextToInteger(productPresentationField.getText()))
                .productCost(parseTextToDouble(productCost.getText()))
                .minPrice(parseTextToDouble(minPrice.getText()))
                .currentPrice(parseTextToDouble(currentPrice.getText()))
                .productWholeSalePrice(parseTextToDouble(wholesalePrice.getText()))
                .maxDiscountPercentage(parseTextToDouble(maxDiscount.getText()))
                .category(productCategorySelector.getValue())
                .currentStockLevel(parseTextToInteger(currentStockLevel.getText()))
                .safetyStockLevel(parseTextToInteger(safetyStockLevel.getText()))
                .imageFilePath(filePath)
                .build();
    }

    @Override
    protected void resetForm() {

        cleanTextfields(
                List.of(
                        productName,
                        brandName,
                        productPresentationField,
                        optionalDescription,
                        productCost,
                        minPrice,
                        currentPrice,
                        wholesalePrice,
                        maxDiscount,
                        currentStockLevel,
                        safetyStockLevel
                )
        );

        cleanImageView(productImagePreview);
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                selectImageButton, this::handleImageSelection,
                removeImageButton, () -> cleanImageView(productImagePreview),
                backButton, () -> viewRedirectionHelper.redirectToView(PRODUCTS, getAnchorPane(), getApplicationContext()),
                resetFormButton, this::resetForm,
                saveButton, this::saveEntity
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void configurePromptTexts() {

        List<TextField> stockLevels = List.of(currentStockLevel, safetyStockLevel);
        setPromptTextOnList(stockLevels, STOCK_LEVEL_DEFAULT_VALUE);

        List<TextField> prices = List.of(productCost, minPrice, currentPrice, wholesalePrice, maxDiscount);
        setPromptTextOnList(prices, PRICE_FIELD_DEFAULT_VALUE);

        Map<TextField, String> map = Map.of(
                productName, PRODUCT_NAME,
                brandName, PRODUCT_BRAND,
                productPresentationField, PRODUCT_SIZE_OR_VOLUME,
                optionalDescription, PRODUCT_OPTIONAL_DESCRIPTION
        );

        setPromptTextOnMap(map);

        setTextOnLabel(profitMarginValue, DISCOUNT_PERCENTAGE_DEFAULT_VALUE);
    }

    private void handleImageSelection() {

        String selectedPath = openImageFileChooser(anchorPane, productImagePreview);

        if (selectedPath != null) {

            filePath = selectedPath;
        }
    }
}