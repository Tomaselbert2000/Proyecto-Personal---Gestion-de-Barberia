package com.presentation.controller.product;

import com.dto.product.ProductInfoDTO;
import com.enums.ProductCategory;
import com.enums.StockStatus;
import com.presentation.controller.item.BaseCatalogViewController;
import com.service.interfaces.ProductService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
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

import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_PRODUCT_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.*;
import static com.presentation.constants.ViewPath.*;
import static com.presentation.support.control.ComboBoxHelper.cleanComboBoxes;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.loadViewOnPane;
import static com.presentation.support.view.FXMLViewLoader.loadViewWithControllerPane;

@Component
@RequiredArgsConstructor
public class ProductViewController extends BaseCatalogViewController<ProductInfoDTO> {

    private final ProductService productService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label
            productCount,
            productsOnLowOrCriticalStock,
            mostSoldProductName,
            amountOfSales,
            highestRevenue,
            totalRevenue,
            totalStockValue,
            totalStockUnits,
            productsFoundCount;

    @FXML
    private TextField productSearchField;

    @FXML
    private ComboBox<ProductCategory> productCategorySelector;

    @FXML
    private ComboBox<StockStatus> productStockStatusSelector;

    @FXML
    private MFXButton
            cleanFiltersButton,
            createProductButton;

    @FXML
    private VBox productListVBox;

    @FXML
    public void initialize() {

        List<ProductInfoDTO> products = productService.getProductsList();

        loadTotalProductCountStats();
        loadMostSoldStats();
        loadRevenueStats();
        loadStockValueStats();

        loadEnumsOnComboBox(productCategorySelector, ProductCategory.values());
        loadEnumsOnComboBox(productStockStatusSelector, StockStatus.values());

        setStringConverter(productCategorySelector, ProductCategory.TODOS);
        setStringConverter(productStockStatusSelector, StockStatus.TODOS);

        configureLiveSearch();
        configureButtonActions();

        loadItemsOnView(products);
    }

    private void loadTotalProductCountStats() {

        executeAsyncTask(
                productService::getProductCountAndStockStats,
                productTotalStockStatsDTO -> {
                    setTextOnLabel(productCount, parseNumberValueToText(productTotalStockStatsDTO.getProductCount()));
                    setTextOnLabel(productsOnLowOrCriticalStock, parseNumberValueToText(productTotalStockStatsDTO.getOnLowOrCriticalStockCount()) + " con stock Bajo o Crítico");
                }
        );
    }

    private void loadMostSoldStats() {

        executeAsyncTask(
                productService::getProductMostSoldStats,
                productMostSoldStatsDTO -> {
                    setTextOnLabel(mostSoldProductName, productMostSoldStatsDTO.getProductName());
                    setTextOnLabel(amountOfSales, parseNumberValueToText(productMostSoldStatsDTO.getUnitsSold()) + " unidades vendidas");
                }
        );
    }

    private void loadRevenueStats() {

        executeAsyncTask(
                productService::getProductHighestRevenueStats,
                productHighestRevenueStatsDTO -> {
                    setTextOnLabel(highestRevenue, productHighestRevenueStatsDTO.getProductName());
                    setTextOnLabel(totalRevenue, CURRENCY_STRING_ARG + parseNumberValueToText(productHighestRevenueStatsDTO.getRevenue()));
                }
        );
    }

    private void loadStockValueStats() {

        executeAsyncTask(
                productService::getProductStockValueStat,
                productStockValueStatDTO -> {
                    setTextOnLabel(totalStockValue, CURRENCY_STRING_ARG + parseNumberValueToText(productStockValueStatDTO.getTotalStockValue()));
                    setTextOnLabel(totalStockUnits, "En " + parseNumberValueToText(productStockValueStatDTO.getTotalUnits()) + " unidades físicas");
                }
        );
    }

    private void goToRegisterNewProductView() {

        loadViewOnPane(PRODUCT_CREATION_VIEW_PATH, applicationContext, PRODUCT_CREATION_VIEW_LOADING_FAILED, anchorPane);
    }

    private void goToEditProductView(ProductInfoDTO infoDTO) {

        loadViewWithControllerPane(
                PRODUCT_EDITION_VIEW_PATH,
                applicationContext,
                PRODUCT_EDITION_VIEW_LOADING_FAILED,
                anchorPane,
                ProductEditionController.class,
                editionController -> editionController.initialize(infoDTO)
        );
    }

    private void goToAddStockView(ProductInfoDTO productInfoDTO) {

    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                createProductButton, this::goToRegisterNewProductView,
                cleanFiltersButton, this::resetSearchFilter
        );

        configureRunnableMaps(map);
    }

    private void configureLiveSearch() {

        attachLiveSearchListeners(
                productSearchField.textProperty(),
                productCategorySelector.valueProperty(),
                productStockStatusSelector.valueProperty()
        );
    }

    @Override
    protected List<ProductInfoDTO> searchCatalog() {

        String productName = productSearchField.getText();

        ProductCategory selectedCategory = productCategorySelector.getValue();

        if (selectedCategory == ProductCategory.TODOS) {

            selectedCategory = null;
        }

        StockStatus selectedStatus = productStockStatusSelector.getValue();

        if (selectedStatus == StockStatus.TODOS) {

            selectedStatus = null;
        }

        return productService.liveSearch(productName, selectedCategory, selectedStatus);
    }

    @Override
    protected VBox getItemListContainer() {

        return productListVBox;
    }

    @Override
    protected void loadItemsOnView(List<ProductInfoDTO> items) {

        loadItemsOnController(
                items,
                productListVBox,
                ProductItemController.class,
                PRODUCT_ITEM_VIEW_PATH,
                EMPTY_PRODUCT_LIST_MESSAGE,
                PRODUCT_ITEM_VIEW_LOADING_FAILED,
                itemController -> {

                    itemController.setOnEditCallback(this::goToEditProductView);
                    itemController.setOnAddStockCallback(this::goToAddStockView);
                }
        );
    }

    @Override
    protected void clearFilterNodes() {

        setBlankTextfield(productSearchField);
        cleanComboBoxes(productCategorySelector, productStockStatusSelector);
    }

    @Override
    protected void afterSearch(List<ProductInfoDTO> items) {

        setTextOnLabel(productsFoundCount, parseNumberValueToText(items.size()) + " encontrados");
    }
}