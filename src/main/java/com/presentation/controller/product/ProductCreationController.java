package com.presentation.controller.product;

import com.dto.product.ProductCreationDTO;
import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.presentation.support.control.ValidationFormatter;
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

import static com.presentation.constants.PromptTexts.ProductPromptText.*;
import static com.presentation.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.PRODUCT_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.PRODUCT_CREATION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.io.FileImageHelper.*;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class ProductCreationController {

    private final ApplicationContext applicationContext;
    private final ProductService productService;

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

    private void registerNewProduct() {

        try {

            ProductCreationDTO creationDTO = buildDTOFromAttributes(
                    productName.getText(),
                    presentationUnitComboBox.getValue(),
                    optionalDescription.getText(),
                    productCategorySelector.getValue(),
                    brandName.getText(),
                    productPresentationField.getText(),
                    productCost.getText(),
                    minPrice.getText(),
                    currentPrice.getText(),
                    wholesalePrice.getText(),
                    maxDiscount.getText(),
                    currentStockLevel.getText(),
                    safetyStockLevel.getText()
            );

            productService.registerNewProduct(creationDTO);

            showToastNotification(anchorPane, applicationContext, PRODUCT_CREATION_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

            List<TextField> textfields = List.of(
                    currentStockLevel,
                    safetyStockLevel,
                    productCost,
                    minPrice,
                    currentPrice,
                    wholesalePrice,
                    maxDiscount,
                    productName,
                    brandName,
                    productPresentationField,
                    optionalDescription
            );

            cleanTextfields(textfields);

            cleanImageView(productImagePreview);

        } catch (ConstraintViolationException exception) {

            String errorMessages = getConstraintViolationsList(exception);

            showWindowAlert(
                    VALIDATION_ERROR_TITLE,
                    PRODUCT_CREATION_VALIDATION_FAILED,
                    errorMessages,
                    Alert.AlertType.ERROR,
                    CONFIRM_BUTTON_TEXT,
                    getCurrentWindow(anchorPane)
            );
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

    private void configurePromptTexts() {

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

    private void resetForm() {

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

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                selectImageButton, this::handleImageSelection,
                removeImageButton, () -> cleanImageView(productImagePreview),
                backButton, () -> redirectToView(ViewRedirection.PRODUCTS, anchorPane, applicationContext),
                resetFormButton, this::resetForm,
                saveButton, this::registerNewProduct
        );

        configureRunnableMaps(map);
    }

    private void handleImageSelection() {

        File file = getFileFromFileChooser(anchorPane);

        if (file != null) {

            filePath = file.getAbsolutePath();

            loadFileOnImageView(file, productImagePreview);
        }
    }
}