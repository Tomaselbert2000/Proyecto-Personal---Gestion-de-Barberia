package com.presentation.controller.product;

import com.dto.product.ProductInfoDTO;
import com.enums.ProductCategory;
import com.enums.StockStatus;
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

import static com.presentation.animation.AnimationEngine.fadeNodeIn;
import static com.presentation.concurrency.ConcurrencyManager.executeUITask;
import static com.presentation.animation.AnimationEngineConstants.ANIMATION_DELAY_IN_MS;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_PRODUCT_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.*;
import static com.presentation.constants.ViewPath.*;
import static com.presentation.support.control.ComboBoxHelper.cleanComboBoxes;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.view.ContainerManager.*;
import static com.presentation.support.view.FXMLViewLoader.*;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;

@Component
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label
            product_count,
            products_on_low_or_critical_stock,
            most_sold_product_name,
            amount_of_sales,
            highest_revenue,
            total_revenue,
            total_stock_value,
            total_stock_units,
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
        loadMostSoldStats();
        loadRevenueStats();
        loadStockValueStats();

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
                productService::getProductCountAndStockStats,
                productTotalStockStatsDTO -> {
                    setTextOnLabel(product_count, parseNumberValueToText(productTotalStockStatsDTO.getProductCount()));
                    setTextOnLabel(products_on_low_or_critical_stock, parseNumberValueToText(productTotalStockStatsDTO.getOnLowOrCriticalStockCount()) + " con stock Bajo o Crítico");
                }
        );
    }

    private void loadMostSoldStats() {

        executeUITask(
                productService::getProductMostSoldStats,
                productMostSoldStatsDTO -> {
                    setTextOnLabel(most_sold_product_name, productMostSoldStatsDTO.getProductName());
                    setTextOnLabel(amount_of_sales, parseNumberValueToText(productMostSoldStatsDTO.getUnitsSold()) + " unidades vendidas");
                }
        );
    }

    private void loadRevenueStats() {

        executeUITask(
                productService::getProductHighestRevenueStats,
                productHighestRevenueStatsDTO -> {
                    setTextOnLabel(highest_revenue, productHighestRevenueStatsDTO.getProductName());
                    setTextOnLabel(total_revenue, CURRENCY_STRING_ARG + parseNumberValueToText(productHighestRevenueStatsDTO.getRevenue()));
                }
        );
    }

    private void loadStockValueStats() {

        executeUITask(
                productService::getProductStockValueStat,
                productStockValueStatDTO -> {
                    setTextOnLabel(total_stock_value, CURRENCY_STRING_ARG + parseNumberValueToText(productStockValueStatDTO.getTotalStockValue()));
                    setTextOnLabel(total_stock_units, "En " + parseNumberValueToText(productStockValueStatDTO.getTotalUnits()) + " unidades físicas");
                }
        );
    }

    private void loadProductListOnView(List<ProductInfoDTO> products) {

        if (products.isEmpty()) {

            showEmptyListLabel(EMPTY_PRODUCT_LIST_MESSAGE, product_list_vbox);

        } else {

            for (int i = 0; i < products.size(); i++) {

                setTextOnLabel(products_found_count, parseNumberValueToText(products.size()));

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

        setViewOnPaneCenter(anchor_pane, productEditionView);
    }

    private void goToAddStockView(ProductInfoDTO productInfoDTO) {

    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                create_product_button, this::goToRegisterNewProductView,
                clean_filters_button, this::cleanFiltersAndLiveSearch
        );

        configureRunnableMaps(map);
    }

    private void configureLiveSearch() {

        product_search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        product_category_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        product_stock_status_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    private void executeLiveSearch() {

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

    private void cleanFiltersAndLiveSearch() {

        setBlankTextfield(product_search_field);
        cleanComboBoxes(product_category_selector, product_stock_status_selector);
    }
}
