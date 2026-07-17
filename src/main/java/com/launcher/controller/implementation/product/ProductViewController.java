package com.launcher.controller.implementation.product;

import com.dto.product.ProductInfoDTO;
import com.enums.ProductCategory;
import com.enums.StockStatus;
import com.launcher.controller.interfaces.ViewController;
import com.service.interfaces.ProductService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.launcher.animation.AnimationEngine.fadeNodeIn;
import static com.launcher.animation.AnimationEngineConstant.ANIMATION_DELAY_IN_MS;
import static com.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.launcher.constants.ui.messages.EmptyListMessage.EMPTY_PRODUCT_LIST_MESSAGE;
import static com.launcher.constants.ui.messages.GenericStrings.CURRENCY_STRING_ARG;
import static com.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.launcher.constants.view.ViewPath.*;
import static com.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.launcher.controller.helper.ContainerManager.*;
import static com.launcher.controller.helper.FXMLViewLoader.*;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.*;

@Component
@RequiredArgsConstructor
public class ProductViewController implements ViewController {

    private final ProductService productService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label
            product_count,
            products_created_this_month,
            critical_stock_level_product_count,
            low_stock_level_product_count,
            total_stock_value,
            total_stock_value_vs_last_month,
            products_found_count;

    @FXML
    private TextField product_search_field;

    @FXML
    private ComboBox<ProductCategory> product_category_selector;

    @FXML
    private ComboBox<StockStatus> product_stock_status_selector;

    @FXML
    private MFXButton
            clean_filters_button,
            create_product_button;

    @FXML
    private VBox product_list_vbox;

    @FXML
    public void initialize() {

        List<ProductInfoDTO> products = productService.getProductsList();

        loadTotalProductCountStats();

        loadStockLevelStats();

        loadTotalStockValue();

        loadEnumsOnComboBox(product_category_selector, ProductCategory.values());
        loadEnumsOnComboBox(product_stock_status_selector, StockStatus.values());

        setStringConverter(product_category_selector, ProductCategory.TODOS);
        setStringConverter(product_stock_status_selector, StockStatus.TODOS);

        configureLiveSearch();
        configureButtonActions();

        loadProductListOnView(products);
    }

    private void loadTotalProductCountStats() {

        executeUITask(
                () -> {
                    Long productCount = productService.getProductsRegisteredCount();
                    Long productsCreatedThisMonth = productService.getProductsCreatedThisMonth();

                    return List.of(productCount, productsCreatedThisMonth);
                },
                uiActionValues -> {

                    Map<Label, String> map = Map.ofEntries(
                            Map.entry(product_count, parseNumberValueToText(uiActionValues.getFirst())),
                            Map.entry(products_created_this_month, parseNumberValueToText(uiActionValues.getLast())),
                            Map.entry(products_found_count, parseNumberValueToText(uiActionValues.getFirst()) + " productos encontrados"));

                    setTextsOnLabelMap(map);
                }
        );
    }

    private void loadStockLevelStats() {

        executeUITask(
                () -> {
                    Long productsWithCriticalStockLevel = productService.getProductsOnCriticalStockCount();
                    Long productsWithLowStockLevel = productService.getProductsOnLowStock();

                    return List.of(productsWithCriticalStockLevel, productsWithLowStockLevel);
                },
                uiActionValues -> {

                    setTextOnLabel(critical_stock_level_product_count, parseNumberValueToText(uiActionValues.getFirst()));
                    setTextOnLabel(low_stock_level_product_count, parseNumberValueToText(uiActionValues.getLast()));
                }
        );
    }

    private void loadTotalStockValue() {

        executeUITask(
                () -> {
                    Double currentTotalStockValue = productService.calculateTotalStockValue();
                    Double totalStockValuePercentageVariationVsLastMonth = productService.calculateTotalStockValuePercentageVariationVsLastMonth();

                    return List.of(currentTotalStockValue, totalStockValuePercentageVariationVsLastMonth);
                },
                uiActionValues -> {

                    setTextOnLabel(total_stock_value, CURRENCY_STRING_ARG + parseNumberValueToText(uiActionValues.getFirst()));
                    setTextOnLabel(total_stock_value_vs_last_month, formatAsPercentage(uiActionValues.getLast()) + "%");
                }
        );
    }

    private void loadProductListOnView(List<ProductInfoDTO> products) {

        if (products.isEmpty()) {

            showEmptyListLabel(EMPTY_PRODUCT_LIST_MESSAGE, product_list_vbox);

        } else {

            for (int i = 0; i < products.size(); i++) {

                ProductInfoDTO infoDTO = products.get(i);

                FXMLLoader loader = generateLoaderWithPath(PRODUCT_ITEM_VIEW_PATH);

                Parent productItem = returnParentFromLoader(loader, PRODUCT_ITEM_VIEW_LOADING_FAILED);

                ProductItemController productItemController = loader.getController();

                productItemController.setDataOnItem(infoDTO);

                productItemController.setOnEditCallback(this::goToEditProductView);
                productItemController.setOnAddStockCallback(this::goToAddStockView);

                loadItemOnVBox(product_list_vbox, productItem);

                fadeNodeIn(product_list_vbox, i * ANIMATION_DELAY_IN_MS);
            }
        }
    }

    private void goToRegisterNewProductView() {

        loadViewOnPane(PRODUCT_CREATION_VIEW_PATH, applicationContext, PRODUCT_CREATION_VIEW_LOADING_FAILED, anchor_pane);
    }

    private void goToEditProductView(ProductInfoDTO productInfoDTO) {

        FXMLLoader loader = generateLoaderWithPath(PRODUCT_EDITION_VIEW_PATH);

        setControllerOnLoader(loader, applicationContext);

        Parent productEditionView = returnParentFromLoader(loader, PRODUCT_EDITION_VIEW_LOADING_FAILED);

        ProductEditionController productEditionController = loader.getController();

        productEditionController.initialize(productInfoDTO);

        setViewOnAnchorPaneCenter(anchor_pane, productEditionView);
    }

    private void goToAddStockView(ProductInfoDTO productInfoDTO) {

    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                create_product_button, this::goToRegisterNewProductView,
                clean_filters_button, this::cleanFiltersAndLiveSearch
        );

        configureRunnableMaps(map);
    }

    @Override
    public void configureLiveSearch() {

        product_search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        product_category_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        product_stock_status_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    @Override
    public void executeLiveSearch() {

        String productName = product_search_field.getText();

        ProductCategory selectedCategory = product_category_selector.getValue();

        if (selectedCategory == ProductCategory.TODOS) {

            selectedCategory = null;
        }

        StockStatus selectedStatus = product_stock_status_selector.getValue();

        if (selectedStatus == StockStatus.TODOS) {

            selectedStatus = null;
        }

        List<ProductInfoDTO> products = productService.liveSearch(productName, selectedCategory, selectedStatus);

        cleanContainer(product_list_vbox);

        loadProductListOnView(products);
    }

    @Override
    public void cleanFiltersAndLiveSearch() {

        setBlankTextfield(product_search_field);
        cleanComboBoxes(product_category_selector, product_stock_status_selector);
    }
}
