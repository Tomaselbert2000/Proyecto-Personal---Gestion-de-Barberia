package com.barbershop.launcher.controller.product;

import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.StockStatus;
import com.barbershop.launcher.controller.interfaces.GenericControllerViewFunctions;
import com.barbershop.service.interfaces.ProductService;
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

import static com.barbershop.launcher.constants.ui.messages.EmptyListMessage.EMPTY_PRODUCT_LIST_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.GenericStrings.*;
import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.barbershop.launcher.controller.helper.ContainerManager.*;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.*;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;

@Component
@RequiredArgsConstructor
public class ProductController implements GenericControllerViewFunctions {

    private final ProductService productService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label product_count;

    @FXML
    private Label products_created_this_month;

    @FXML
    private Label critical_stock_level_product_count;

    @FXML
    private Label low_stock_level_product_count;

    @FXML
    private Label total_stock_value;

    @FXML
    private Label total_stock_value_vs_last_month;

    @FXML
    private Label products_found_count;

    @FXML
    private TextField product_search_field;

    @FXML
    private ComboBox<ProductCategory> product_category_selector;

    @FXML
    private ComboBox<StockStatus> product_stock_status_selector;

    @FXML
    private Button clean_filters_button;

    @FXML
    private Button create_product_button;

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

        configureProductLiveSearch();
        configureButtonActions();

        loadProductListOnView(products);
    }

    private void loadTotalProductCountStats() {

        Long productCount = productService.getProductsRegisteredCount();
        Long productsCreatedThisMonth = productService.getProductsCreatedThisMonth();

        setTextOnLabel(product_count, productCount.toString());
        setTextOnLabel(products_created_this_month, productsCreatedThisMonth.toString());
        setTextOnLabel(products_found_count, productCount + " productos encontrados");
    }

    private void loadStockLevelStats() {

        Long productsWithCriticalStockLevel = productService.getProductsOnCriticalStockCount();
        Long productsWithLowStockLevel = productService.getProductsOnLowStock();

        setTextOnLabel(critical_stock_level_product_count, productsWithCriticalStockLevel.toString());
        setTextOnLabel(low_stock_level_product_count, productsWithLowStockLevel.toString());
    }

    private void loadTotalStockValue() {

        Double currentTotalStockValue = productService.calculateTotalStockValue();
        Double totalStockValuePercentageVariationVsLastMonth = productService.calculateTotalStockValuePercentageVariationVsLastMonth();

        setTextOnLabel(total_stock_value, CURRENCY_STRING_ARG + currentTotalStockValue);
        setTextOnLabel(total_stock_value_vs_last_month, totalStockValuePercentageVariationVsLastMonth.toString() + "%");
    }

    private void configureProductLiveSearch() {

        product_search_field.textProperty().addListener((_, _, _) -> executeProductLiveSearch());
        product_category_selector.valueProperty().addListener((_, _, _) -> executeProductLiveSearch());
        product_stock_status_selector.valueProperty().addListener((_, _, _) -> executeProductLiveSearch());
    }

    private void executeProductLiveSearch() {

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

        cleanVBox(product_list_vbox);

        loadProductListOnView(products);
    }

    private void loadProductListOnView(List<ProductInfoDTO> products) {

        if (products.isEmpty()) {

            showEmptyListLabel(EMPTY_PRODUCT_LIST_MESSAGE, product_list_vbox);

        } else {

            for (ProductInfoDTO infoDTO : products) {

                FXMLLoader loader = generateLoaderWithPath(PRODUCT_ITEM_VIEW_PATH);

                Parent productItem = returnParentFromLoader(loader, PRODUCT_ITEM_VIEW_LOADING_FAILED);

                ProductItemController productItemController = loader.getController();

                productItemController.setDataOnItem(infoDTO);

                productItemController.setOnEditCallback(this::goToEditProductView);
                productItemController.setOnAddStockCallback(this::goToAddStockView);

                loadItemOnVBox(product_list_vbox, productItem);
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
    public void cleanFiltersAndLiveSearch() {

        setBlankTextfield(product_search_field);
        cleanComboBoxes(product_category_selector, product_stock_status_selector);
    }
}
