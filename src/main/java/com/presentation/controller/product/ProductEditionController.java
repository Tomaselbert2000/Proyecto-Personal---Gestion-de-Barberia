package com.presentation.controller.product;

import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
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

import static com.presentation.constants.StringResource.ConfirmationDialog.CONFIRM_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.PRODUCT_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.PRODUCT_EDITION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.control.ComboBoxHelper.*;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.io.FileImageHelper.*;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ContainerManager.getCurrentWindow;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class ProductEditionController {

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
            resetButton,
            saveButton;

    @FXML
    private TextField
            productName,
            optionalDescription,
            brandName,
            productPresentationField,
            productCost,
            currentPrice,
            productWholeSalePrice,
            minPrice,
            maxDiscountPercentage,
            currentStockLevel,
            safetyStockLevel;

    @FXML
    private ComboBox<ProductCategory> productCategorySelector;

    @FXML
    private ComboBox<ProductPresentationUnit> productPresentationUnitSelector;

    @FXML
    private Label profitMarginValue;

    @FXML
    private ImageView productImagePreview;

    @FXML
    public void initialize(ProductInfoDTO infoDTO) {

        loadProductDataForEdition(infoDTO);
    }

    private void loadProductDataForEdition(ProductInfoDTO infoDTO) {

        ProductUpdateDTO updateDTOFromDB = productService.getProductForUpdate(infoDTO.getId());

        Map<TextField, String> map = Map.ofEntries(
                Map.entry(productName, updateDTOFromDB.getName()),
                Map.entry(optionalDescription, updateDTOFromDB.getOptionalDescription()),
                Map.entry(brandName, updateDTOFromDB.getBrandName()),
                Map.entry(productPresentationField, parseNumberValueToText(updateDTOFromDB.getPresentationSize())),
                Map.entry(productCost, parseNumberValueToText(updateDTOFromDB.getProductCost())),
                Map.entry(currentPrice, parseNumberValueToText(updateDTOFromDB.getCurrentPrice())),
                Map.entry(productWholeSalePrice, parseNumberValueToText(updateDTOFromDB.getProductWholeSalePrice())),
                Map.entry(minPrice, parseNumberValueToText(updateDTOFromDB.getMinPrice())),
                Map.entry(maxDiscountPercentage, parseNumberValueToText(updateDTOFromDB.getMaxDiscountPercentage())),
                Map.entry(currentStockLevel, parseNumberValueToText(updateDTOFromDB.getCurrentStockLevel())),
                Map.entry(safetyStockLevel, parseNumberValueToText(updateDTOFromDB.getSafetyStockLevel()))
        );

        setTextsOnTextfieldMap(map);

        loadEnumsOnComboBox(productCategorySelector, ProductCategory.values());
        removeFirstItemFromComboBox(productCategorySelector);

        loadEnumsOnComboBox(productPresentationUnitSelector, ProductPresentationUnit.values());
        removeFirstItemFromComboBox(productPresentationUnitSelector);

        setStringConverter(productCategorySelector, updateDTOFromDB.getCategory());
        setStringConverter(productPresentationUnitSelector, updateDTOFromDB.getPresentationUnit());

        loadCurrentProductImageIfExists(updateDTOFromDB);

        configureButtonActions(infoDTO);
    }

    private void loadCurrentProductImageIfExists(ProductUpdateDTO dto) {

        if (dto.getImageFilePath() != null) {

            File file = new File(dto.getImageFilePath());

            loadFileOnImageView(file, productImagePreview);
        }
    }

    private void updateProduct(ProductInfoDTO infoDTOReference) {

        try {

            String newName = productName.getText();
            ProductCategory newCategory = productCategorySelector.getValue();
            String newBrandName = brandName.getText();
            String newOptionalDescription = optionalDescription.getText();
            ProductPresentationUnit newPresentationUnit = productPresentationUnitSelector.getValue();
            String newSizeValue = productPresentationField.getText();
            String newCost = productCost.getText();
            String newMinPrice = minPrice.getText();
            String newCurrentPrice = currentPrice.getText();
            String newWholeSalePrice = productWholeSalePrice.getText();
            String newMaxDiscountPrice = maxDiscountPercentage.getText();
            String newSafetyStockLevel = safetyStockLevel.getText();

            ProductUpdateDTO updateDTO = buildDTOFromAttributesAndDTOReference(
                    infoDTOReference,
                    newName,
                    newCategory,
                    newBrandName,
                    newOptionalDescription,
                    newPresentationUnit,
                    newSizeValue,
                    newCost,
                    newMinPrice,
                    newCurrentPrice,
                    newWholeSalePrice,
                    newMaxDiscountPrice,
                    newSafetyStockLevel
            );

            productService.updateProduct(infoDTOReference.getId(), updateDTO);

            showToastNotification(anchorPane, applicationContext, PRODUCT_UPDATE_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException exception) {

            String errorMessages = getConstraintViolationsList(exception);

            showWindowAlert(VALIDATION_ERROR_TITLE, PRODUCT_EDITION_VALIDATION_FAILED, errorMessages, Alert.AlertType.ERROR, CONFIRM_BUTTON_TEXT, getCurrentWindow(anchorPane));
        }

    }

    private ProductUpdateDTO buildDTOFromAttributesAndDTOReference(
            ProductInfoDTO infoDTOReference,
            String newName,
            ProductCategory newCategory,
            String newBrandName,
            String newOptionalDescription,
            ProductPresentationUnit newPresentationUnit,
            String newSizeValue,
            String newCost,
            String newMinPrice,
            String newCurrentPrice,
            String newWholeSalePrice,
            String newMaxDiscountPrice,
            String newSafetyStockLevel) {

        Integer parsedSize = parseTextToInteger(newSizeValue);
        Integer parsedSafetyStockLevel = parseTextToInteger(newSafetyStockLevel);
        Double parsedCost = parseTextToDouble(newCost);
        Double parsedMinPrice = parseTextToDouble(newMinPrice);
        Double parsedCurrentPrice = parseTextToDouble(newCurrentPrice);
        Double parseWholeSalePrice = parseTextToDouble(newWholeSalePrice);
        Double parsedMaxDiscountPercentage = parseTextToDouble(newMaxDiscountPrice);

        return ProductUpdateDTO.builder()
                .name(newName)
                .optionalDescription(newOptionalDescription)
                .brandName(newBrandName)
                .presentationUnit(newPresentationUnit)
                .presentationSize(parsedSize)
                .productCost(parsedCost)
                .minPrice(parsedMinPrice)
                .currentPrice(parsedCurrentPrice)
                .productWholeSalePrice(parseWholeSalePrice)
                .maxDiscountPercentage(parsedMaxDiscountPercentage)
                .category(newCategory)
                .currentStockLevel(infoDTOReference.getCurrentStockLevel())
                .safetyStockLevel(parsedSafetyStockLevel)
                .imageFilePath(filePath)
                .build();
    }

    private void configureButtonActions(ProductInfoDTO dto) {

        Map<Button, Runnable> map = Map.of(
                backButton, () -> redirectToView(ViewRedirection.PRODUCTS, anchorPane, applicationContext),
                resetButton, () -> resetForm(dto),
                removeImageButton, () -> cleanImageView(productImagePreview),
                selectImageButton, this::handleImageSelection,
                saveButton, () -> updateProduct(dto)
        );

        configureRunnableMaps(map);
    }

    private void resetForm(ProductInfoDTO infoDTO) {

        cleanTextfields(List.of(
                        productName,
                        optionalDescription,
                        brandName,
                        productPresentationField,
                        productCost,
                        currentPrice,
                        productWholeSalePrice,
                        minPrice,
                        maxDiscountPercentage,
                        currentStockLevel,
                        safetyStockLevel
                )
        );

        cleanImageView(productImagePreview);

        cleanComboBoxes(productCategorySelector, productPresentationUnitSelector);

        loadProductDataForEdition(infoDTO);
    }

    private void handleImageSelection() {

        File newFile = getFileFromFileChooser(anchorPane);

        if (newFile != null) {

            filePath = newFile.getAbsolutePath();

            loadFileOnImageView(newFile, productImagePreview);
        }
    }
}
