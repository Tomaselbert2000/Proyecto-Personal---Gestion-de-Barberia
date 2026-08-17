package com.presentation.controller.product;

import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
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
import java.util.Map;

import static com.enums.ViewRedirection.PRODUCTS;
import static com.presentation.constants.StringResource.ToastNotificationMessage.PRODUCT_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.PRODUCT_EDITION_VALIDATION_FAILED;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.removeFirstItemFromComboBox;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextsOnTextfieldMap;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.io.FileImageHelper.*;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
public class ProductEditionController extends BaseCrudFormController<ProductUpdateDTO, ProductInfoDTO> {

    private final ProductService productService;

    public ProductEditionController(ApplicationContext applicationContext, ProductService productService) {

        super(applicationContext);
        this.productService = productService;
    }

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

        infoDTOReference = infoDTO;

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

        configureButtonActions();
    }

    private void loadCurrentProductImageIfExists(ProductUpdateDTO dto) {

        if (dto.getImageFilePath() != null) {

            File file = new File(dto.getImageFilePath());

            loadFileOnImageView(file, productImagePreview);
        }
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                backButton, () -> redirectToView(PRODUCTS, getAnchorPane(), getApplicationContext()),
                resetButton, this::resetForm,
                removeImageButton, () -> cleanImageView(productImagePreview),
                selectImageButton, this::handleImageSelection,
                saveButton, this::saveEntity
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void configurePromptTexts() {
    }

    private void handleImageSelection() {

        File newFile = getFileFromFileChooser(anchorPane);

        if (newFile != null) {

            filePath = newFile.getAbsolutePath();

            loadFileOnImageView(newFile, productImagePreview);
        }
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(ProductUpdateDTO updateDTO) {

        productService.updateProduct(infoDTOReference.getId(), updateDTO);
    }

    @Override
    protected String getSuccessMessage() {

        return PRODUCT_UPDATE_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return PRODUCT_EDITION_VALIDATION_FAILED;
    }

    @Override
    protected ProductUpdateDTO buildDTO() {

        return ProductUpdateDTO.builder()
                .name(productName.getText())
                .optionalDescription(optionalDescription.getText())
                .brandName(brandName.getText())
                .presentationUnit(productPresentationUnitSelector.getValue())
                .presentationSize(parseTextToInteger(productPresentationField.getText()))
                .productCost(parseTextToDouble(productCost.getText()))
                .minPrice(parseTextToDouble(minPrice.getText()))
                .currentPrice(parseTextToDouble(currentPrice.getText()))
                .productWholeSalePrice(parseTextToDouble(productWholeSalePrice.getText()))
                .maxDiscountPercentage(parseTextToDouble(maxDiscountPercentage.getText()))
                .category(productCategorySelector.getValue())
                .currentStockLevel(parseTextToInteger(currentStockLevel.getText()))
                .safetyStockLevel(parseTextToInteger(safetyStockLevel.getText()))
                .imageFilePath(filePath)
                .build();
    }

    @Override
    protected void resetForm() {

        loadProductDataForEdition(infoDTOReference);
    }
}
