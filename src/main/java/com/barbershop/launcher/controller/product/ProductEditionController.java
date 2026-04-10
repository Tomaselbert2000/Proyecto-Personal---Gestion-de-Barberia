package com.barbershop.launcher.controller.product;

import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.ProductPresentationUnit;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
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
import static com.barbershop.launcher.controller.UI_RenderingFunctions.*;

@Component
@RequiredArgsConstructor
public class ProductEditionController {

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
    private Button save_button;

    @FXML
    public void initialize(ProductInfoDTO infoDTO) {

        loadProductDataForEdition(infoDTO);
    }

    private void loadProductDataForEdition(ProductInfoDTO infoDTO) {

        ProductUpdateDTO updateDTOFromDB = productService.getProductForUpdate(infoDTO.getId());

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
        List<String> texts = List.of(
                updateDTOFromDB.getName(),
                updateDTOFromDB.getOptionalDescription(),
                updateDTOFromDB.getBrandName(),
                parseNumberValueToText(updateDTOFromDB.getPresentationSize()),
                parseNumberValueToText(updateDTOFromDB.getProductCost()),
                parseNumberValueToText(updateDTOFromDB.getCurrentPrice()),
                parseNumberValueToText(updateDTOFromDB.getProductWholeSalePrice()),
                parseNumberValueToText(updateDTOFromDB.getMinPrice()),
                parseNumberValueToText(updateDTOFromDB.getMaxDiscountPercentage()),
                parseNumberValueToText(updateDTOFromDB.getCurrentStockLevel()),
                parseNumberValueToText(updateDTOFromDB.getSafetyStockLevel())
        );

        Map<TextField, String> map = generateMap(textfields, texts);

        setTextsOnTextfieldMap(map);

        loadEnumsOnComboBox(category, ProductCategory.values());
        loadEnumsOnComboBox(presentation_unit, ProductPresentationUnit.values());

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

    private void configureButtonActions(ProductInfoDTO infoDTOReference) {

        back_button.setOnAction(_ -> redirectToView(applicationContext, ViewRedirection.PRODUCTS));
        remove_image_button.setOnAction(_ -> cleanImageView(product_image_preview));
        select_image_button.setOnAction(_ -> handleImageUpdate());
        save_button.setOnAction(_ -> updateProduct(infoDTOReference));
    }

    private void handleImageUpdate() {

        File newFile = getFileFromFileChooser(anchor_pane);

        if (newFile != null) {

            filePath = newFile.getAbsolutePath();

            loadFileOnImageView(newFile, product_image_preview);
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
}
