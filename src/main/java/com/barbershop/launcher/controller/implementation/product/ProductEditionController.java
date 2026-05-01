package com.barbershop.launcher.controller.implementation.product;

import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.ProductPresentationUnit;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.launcher.controller.interfaces.EditionController;
import com.barbershop.launcher.controller.interfaces.ProductController;
import com.barbershop.service.interfaces.ProductService;
import jakarta.validation.ConstraintViolationException;
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

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.PRODUCT_UPDATE_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.PRODUCT_EDITION_VALIDATION_FAILED;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.*;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.FileImageHelper.*;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class ProductEditionController implements EditionController<ProductInfoDTO>, ProductController {

    private final ApplicationContext applicationContext;
    private final ProductService productService;

    private String filePath = "";

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
    private Button reset_button;

    @FXML
    private Button save_button;

    @FXML
    public void initialize(ProductInfoDTO infoDTO) {

        loadProductDataForEdition(infoDTO);
    }

    private void loadProductDataForEdition(ProductInfoDTO infoDTO) {

        ProductUpdateDTO updateDTOFromDB = productService.getProductForUpdate(infoDTO.getId());

        Map<TextField, String> map = Map.ofEntries(
                Map.entry(name, updateDTOFromDB.getName()),
                Map.entry(optional_description, updateDTOFromDB.getOptionalDescription()),
                Map.entry(brand_name, updateDTOFromDB.getBrandName()),
                Map.entry(presentation_size, parseNumberValueToText(updateDTOFromDB.getPresentationSize())),
                Map.entry(product_cost, parseNumberValueToText(updateDTOFromDB.getProductCost())),
                Map.entry(current_price, parseNumberValueToText(updateDTOFromDB.getCurrentPrice())),
                Map.entry(product_wholesale_price, parseNumberValueToText(updateDTOFromDB.getProductWholeSalePrice())),
                Map.entry(min_price, parseNumberValueToText(updateDTOFromDB.getMinPrice())),
                Map.entry(max_discount_percentage, parseNumberValueToText(updateDTOFromDB.getMaxDiscountPercentage())),
                Map.entry(current_stock_level, parseNumberValueToText(updateDTOFromDB.getCurrentStockLevel())),
                Map.entry(safety_stock_level, parseNumberValueToText(updateDTOFromDB.getSafetyStockLevel()))
        );

        setTextsOnTextfieldMap(map);

        loadEnumsOnComboBox(category, ProductCategory.values());
        removeFirstItemFromComboBox(category);

        loadEnumsOnComboBox(presentation_unit, ProductPresentationUnit.values());
        removeFirstItemFromComboBox(presentation_unit);

        setStringConverter(category, updateDTOFromDB.getCategory());
        setStringConverter(presentation_unit, updateDTOFromDB.getPresentationUnit());

        loadCurrentProductImageIfExists(updateDTOFromDB);

        configureButtonActions(infoDTO);
    }

    private void loadCurrentProductImageIfExists(ProductUpdateDTO dto) {

        if (dto.getImageFilePath() != null) {

            File file = new File(dto.getImageFilePath());

            loadFileOnImageView(file, product_image_preview);
        }
    }

    private void updateProduct(ProductInfoDTO infoDTOReference) {

        try {

            String newName = name.getText();
            ProductCategory newCategory = category.getValue();
            String newBrandName = brand_name.getText();
            String newOptionalDescription = optional_description.getText();
            ProductPresentationUnit newPresentationUnit = presentation_unit.getValue();
            String newSizeValue = presentation_size.getText();
            String newCost = product_cost.getText();
            String newMinPrice = min_price.getText();
            String newCurrentPrice = current_price.getText();
            String newWholeSalePrice = product_wholesale_price.getText();
            String newMaxDiscountPrice = max_discount_percentage.getText();
            String newSafetyStockLevel = safety_stock_level.getText();

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

            showToastNotification(anchor_pane, applicationContext, PRODUCT_UPDATE_TOAST_NOTIFICATION_MESSAGE, ToastNotificationType.SUCCESSFUL);

        } catch (ConstraintViolationException exception) {

            String errorMessages = getConstraintViolationsList(exception);

            showErrorAlert(VALIDATION_ERROR_TITLE, PRODUCT_EDITION_VALIDATION_FAILED, errorMessages);
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

    @Override
    public void configureButtonActions(ProductInfoDTO dto) {

        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.PRODUCTS, anchor_pane, applicationContext),
                reset_button, () -> resetForm(dto),
                remove_image_button, () -> cleanImageView(product_image_preview),
                select_image_button, this::handleImageSelection,
                save_button, () -> updateProduct(dto)
        );

        configureRunnableMaps(map);
    }

    @Override
    public void resetForm(ProductInfoDTO infoDTO) {

        cleanTextfields(List.of(
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
                )
        );

        cleanImageView(product_image_preview);

        cleanComboBoxes(category, presentation_unit);

        loadProductDataForEdition(infoDTO);
    }

    @Override
    public void configureButtonActions() {
    }

    @Override
    public void handleImageSelection() {

        File newFile = getFileFromFileChooser(anchor_pane);

        if (newFile != null) {

            filePath = newFile.getAbsolutePath();

            loadFileOnImageView(newFile, product_image_preview);
        }
    }
}
