package com.presentation.controller.sale;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductItemDTO;
import com.dto.sale.SaleCreationDTO;
import com.dto.sale.SaleInfoDTO;
import com.presentation.controller.BaseCrudFormController;
import com.presentation.support.view.ViewRedirectionHelper;
import com.service.interfaces.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.enums.ViewRedirection.SALES;
import static com.presentation.constants.PromptTexts.ProductPromptText.PRODUCT_SEARCH_FIELD;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.SALE_PRODUCT_LIST_ITEM_VIEW_LOADING_FAILED;
import static com.presentation.constants.StringResource.ToastNotificationMessage.SALE_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.SALE_CREATION_VALIDATION_FAILED;
import static com.presentation.constants.ViewPath.SALE_PRODUCT_LIST_ITEM_VIEW_PATH;
import static com.presentation.support.control.ComboBoxHelper.loadGenericTypeListOnComboBox;
import static com.presentation.support.control.ComboBoxHelper.resetComboBoxFilter;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.view.ContainerManager.loadSingleItemOnController;

@Component
@Getter
public class SaleCreationController extends BaseCrudFormController<SaleCreationDTO, SaleInfoDTO> {

    private final SaleService saleService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final BarberserviceService barberserviceService;
    private final PaymentMethodService paymentMethodService;
    private final ProductService productService;

    private final ViewRedirectionHelper viewRedirectionHelper;

    private final Map<Long, ProductItemDTO> shoppingCart = new LinkedHashMap<>();
    private final Map<Long, BigDecimal> priceMap = new LinkedHashMap<>();

    public SaleCreationController(
            ApplicationContext applicationContext,
            SaleService saleService,
            ClientService clientService,
            EmployeeService employeeService,
            BarberserviceService barberserviceService,
            PaymentMethodService paymentMethodService,
            ProductService productService,
            ViewRedirectionHelper viewRedirectionHelper
    ) {

        super(applicationContext);
        this.saleService = saleService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.barberserviceService = barberserviceService;
        this.paymentMethodService = paymentMethodService;
        this.productService = productService;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private CheckBox
            anonClientCheckBox,
            deferredSaleCheckBox;

    @FXML
    private DatePicker deferredSaleDatePicker;

    @FXML
    private ComboBox<LocalTime>
            hourSelectorForDeferredSale,
            minuteSelectorForDeferredSale;

    @FXML
    private ComboBox<ClientInfoDTO> clientSelector;

    @FXML
    private ComboBox<EmployeeInfoDTO> employeeSelector;

    @FXML
    private ComboBox<PaymentMethodInfoDTO> paymentMethodSelector;

    @FXML
    private ComboBox<BarberServiceInfoDTO> barberServiceSelector;

    @FXML
    private TextField productSearchField;

    @FXML
    private TitledPane titledPane;

    @FXML
    private VBox productListContainer;

    @FXML
    private MFXButton
            backButton,
            resetFormButton,
            registerSale;

    @FXML
    private Label totalLabel;

    private final ContextMenu contextMenu = new ContextMenu();

    @FXML
    public void initialize() {

        loadGenericTypeListOnComboBox(employeeSelector, employeeService.getEmployeeList());
        loadGenericTypeListOnComboBox(clientSelector, clientService.getClientList());
        loadGenericTypeListOnComboBox(paymentMethodSelector, paymentMethodService.getPaymentMethodsList());
        loadGenericTypeListOnComboBox(barberServiceSelector, barberserviceService.getServicesList());

        configureProductSearchListener();

        configureButtonActions();
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(SaleCreationDTO creationDTO) {

        saleService.registerNewSale(creationDTO);
    }

    @Override
    protected String getSuccessMessage() {

        return SALE_CREATION_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return SALE_CREATION_VALIDATION_FAILED;
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(backButton, () -> viewRedirectionHelper.redirectToView(SALES, getAnchorPane(), getApplicationContext())),
                Map.entry(resetFormButton, this::resetForm),
                Map.entry(registerSale, this::saveEntity)
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void configurePromptTexts() {

        Map<TextField, String> map = Map.ofEntries(
                Map.entry(productSearchField, PRODUCT_SEARCH_FIELD)
        );

        setPromptTextOnMap(map);
    }

    @Override
    protected SaleCreationDTO buildDTO() {

        LocalDateTime dateTime = null;

        if (deferredSaleCheckBox.isSelected()) {

            LocalDate date = deferredSaleDatePicker.getValue();
            LocalTime hour = hourSelectorForDeferredSale.getValue();
            LocalTime minute = minuteSelectorForDeferredSale.getValue();

            dateTime = LocalDateTime.of(date, LocalTime.of(hour.getHour(), minute.getMinute()));
        }

        Long clientID = clientSelector.getValue() == null ? 1L : clientSelector.getValue().getId();
        Long employeeID = employeeSelector.getValue() == null ? null : employeeSelector.getValue().getId();
        Long paymentMethodID = paymentMethodSelector.getValue() == null ? null : paymentMethodSelector.getValue().getId();
        Long barberserviceID = barberServiceSelector.getValue() == null ? null : barberServiceSelector.getValue().getBarberServiceId();

        List<ProductItemDTO> itemList = shoppingCart.values().stream().toList();

        return SaleCreationDTO.builder()
                .dateAndTime(dateTime)
                .clientID(clientID)
                .employeeID(employeeID)
                .paymentMethodID(paymentMethodID)
                .barberServiceID(barberserviceID)
                .productsDetail(itemList)
                .build();
    }

    @Override
    protected void resetForm() {

        cleanTextfields(List.of(productSearchField));

        resetComboBoxFilter(
                employeeSelector,
                barberServiceSelector,
                paymentMethodSelector,
                hourSelectorForDeferredSale,
                minuteSelectorForDeferredSale
        );

        deferredSaleCheckBox.setSelected(false);
        anonClientCheckBox.setSelected(false);

        deferredSaleDatePicker.setValue(LocalDate.now());
    }

    private void onQuantityUpdated(ProductItemDTO updatedItem) {

        shoppingCart.put(updatedItem.getProductID(), updatedItem);

        calculateSaleTotal();
    }

    private void addProductToCart(ProductInfoDTO selectedProduct) {

        Long productID = selectedProduct.getId();

        if (shoppingCart.containsKey(productID)) {

            ProductItemDTO dto = shoppingCart.get(productID);

            dto.setQuantity(dto.getQuantity() + 1);

        } else {

            ProductItemDTO newItem = ProductItemDTO.builder()
                    .productID(productID)
                    .quantity(1)
                    .build();

            shoppingCart.put(productID, newItem);
            priceMap.put(productID, BigDecimal.valueOf(selectedProduct.getCurrentPrice()));

            loadSingleItemOnController(
                    newItem,
                    productListContainer,
                    SaleProductItemController.class,
                    SALE_PRODUCT_LIST_ITEM_VIEW_PATH,
                    "",
                    SALE_PRODUCT_LIST_ITEM_VIEW_LOADING_FAILED,
                    itemController -> {

                        itemController.setOnDeleteProductCallback(this::removeProductFromCart);
                        itemController.setOnQuantityChangeCallback(this::onQuantityUpdated);
                        itemController.setDataOnItem(newItem);
                    }
            );
        }

        calculateSaleTotal();

        setBlankTextfield(productSearchField);
    }

    private void removeProductFromCart(Node node, ProductItemDTO selectedProduct) {

        Long productID = selectedProduct.getProductID();

        shoppingCart.remove(productID);
        priceMap.remove(productID);

        productListContainer.getChildren().remove(node);

        calculateSaleTotal();
    }

    private void calculateSaleTotal() {

        BigDecimal total = BigDecimal.valueOf(0.0);

        for (Long id : shoppingCart.keySet()) {

            ProductItemDTO dto = shoppingCart.get(id);
            BigDecimal price = priceMap.get(id);

            total = total.add(BigDecimal.valueOf(dto.getQuantity()).multiply(price));
        }

        setTextOnLabel(totalLabel, parseNumberValueToText(total));
    }

    private void configureProductSearchListener() {

        productSearchField.textProperty().addListener((_, _, newValue) -> {

                    if (newValue.isBlank()) {

                        contextMenu.hide();

                    } else {

                        performProductSearch(newValue);
                    }
                }
        );
    }

    private void performProductSearch(String productName) {

        List<ProductInfoDTO> productList = productService.searchByName(productName);

        if (productList.isEmpty()) {

            contextMenu.hide();

            return;
        }

        contextMenu.getItems().clear();

        for (ProductInfoDTO dto : productList) {

            Label label = new Label(dto.getName());

            CustomMenuItem item = new CustomMenuItem(label);
            item.setOnAction(_ -> addProductToCart(dto));

            contextMenu.getItems().add(item);
        }

        contextMenu.show(productSearchField, Side.BOTTOM, 0, 0);
    }
}