package com.presentation.controller.sale;

import com.dto.sale.SaleInfoDTO;
import com.enums.SaleCompositionFilter;
import com.presentation.controller.BaseCatalogViewController;
import com.presentation.support.format.NumberParser;
import com.presentation.support.view.ViewRedirectionHelper;
import com.service.interfaces.EmployeeService;
import com.service.interfaces.PaymentMethodService;
import com.service.interfaces.SaleService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_SALE_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.SALE_ITEM_VIEW_LOADING_FAILED;
import static com.presentation.constants.ViewPath.SALE_ITEM_VIEW_PATH;
import static com.presentation.support.control.ComboBoxHelper.*;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;

@Component
@RequiredArgsConstructor
public class SalesViewController extends BaseCatalogViewController<SaleInfoDTO> {

    private final SaleService saleService;
    private final PaymentMethodService paymentMethodService;
    private final EmployeeService employeeService;
    private final ViewRedirectionHelper viewRedirectionHelper;

    private static final BigDecimal DEFAULT_DECIMAL = BigDecimal.valueOf(0.0);

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label
            monthtlyIncome,
            incomeTrendPercentageVsLastMonth,
            averageTicketTotal,
            extraProductsSoldCount,
            salesTodayCount,
            mostPopularBarberService,
            productsIncome,
            mostSoldProduct,
            totalSalesCount;

    @FXML
    private DatePicker saleDateFilter;

    @FXML
    private TextField
            minPriceTextfield,
            maxPriceTextfield;

    @FXML
    private ComboBox<String> paymentMethodFilter;

    @FXML
    private ComboBox<String> employeeFilter;

    @FXML
    private ComboBox<SaleCompositionFilter> saleCompositionFilter;

    @FXML
    private MFXButton
            cleanFilterButton,
            cancelSaleButton,
            viewSaleDetailsButton,
            registerNewSale;

    @FXML
    private VBox saleListContainer;

    @Override
    protected ObservableValue<?>[] getSearchProperties() {

        return new ObservableValue<?>[]{

                saleDateFilter.valueProperty(),
                minPriceTextfield.textProperty(),
                maxPriceTextfield.textProperty(),
                paymentMethodFilter.valueProperty(),
                employeeFilter.valueProperty(),
                saleCompositionFilter.valueProperty()
        };
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(registerNewSale, this::registerNewSale),
                Map.entry(cleanFilterButton, this::resetSearchFilter)
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void loadGlobalStats() {

        loadMonthlyIncomeStats();
        loadAverageTicketStats();
        loadSalesTodayStats();
        loadProductIncomeStats();
    }

    @Override
    protected Label getResultsCountLabel() {

        return totalSalesCount;
    }

    @Override
    protected List<SaleInfoDTO> searchCatalog() {

        BigDecimal minPrice = NumberParser.parseTextToBigDecimal(minPriceTextfield.getText(), DEFAULT_DECIMAL);
        BigDecimal maxPrice = NumberParser.parseTextToBigDecimal(maxPriceTextfield.getText(), DEFAULT_DECIMAL);

        String paymentMethodSelected = paymentMethodFilter.getValue();
        String employeeSelected = employeeFilter.getValue();

        SaleCompositionFilter saleComposition = nullIfTodos(saleCompositionFilter.getValue(), SaleCompositionFilter.TODOS);

        return saleService.liveSearch(minPrice, maxPrice, paymentMethodSelected, employeeSelected, saleComposition);
    }

    @Override
    protected VBox getItemListContainer() {

        return saleListContainer;
    }

    @Override
    protected void loadItemsOnView(List<SaleInfoDTO> items) {

        loadItemsOnController(
                items,
                saleListContainer,
                SaleItemController.class,
                SALE_ITEM_VIEW_PATH,
                EMPTY_SALE_LIST_MESSAGE,
                SALE_ITEM_VIEW_LOADING_FAILED,
                itemController -> {

                    itemController.setOnSaleCancelCallBack(this::cancelSale);
                    itemController.setOnViewDetailsCallBack(this::viewSaleDetails);
                }
        );
    }

    @Override
    protected void clearFilterNodes() {

        setBlankTextfield(minPriceTextfield, maxPriceTextfield);
        resetComboBoxFilter(paymentMethodFilter, employeeFilter, saleCompositionFilter);
    }

    @Override
    protected void initializeListContent() {

        List<SaleInfoDTO> sales = saleService.getSales();
        List<String> paymentMethodNames = paymentMethodService.getNames();
        List<String> employeeNames = employeeService.getNames();

        loadItemsOnView(sales);

        setTextOnLabel(totalSalesCount, parseNumberValueToText(sales.size()));

        loadEnumsOnComboBox(saleCompositionFilter, SaleCompositionFilter.values());

        setStringConverter(saleCompositionFilter, SaleCompositionFilter.TODOS);

        loadGenericTypeListOnComboBox(paymentMethodFilter, paymentMethodNames);
        loadGenericTypeListOnComboBox(employeeFilter, employeeNames);
    }

    private void viewSaleDetails(SaleInfoDTO saleInfoDTO) {

        //TODO: pendiente de diseño e implementación
    }

    private void cancelSale(SaleInfoDTO saleInfoDTO) {

        //TODO: pendiente de diseño e implementación
    }

    private void registerNewSale() {

        //TODO: pendiente de diseño e implementación
    }

    private void loadMonthlyIncomeStats() {

        executeAsyncTask(
                saleService::getMonthlyIncomeStats,
                monthlyIncomeStatsDTO -> {
                    setTextOnLabel(monthtlyIncome, parseNumberValueToText(monthlyIncomeStatsDTO.getCurrentMonthTotal()));
                    setTextOnLabel(incomeTrendPercentageVsLastMonth, formatAsPercentage(monthlyIncomeStatsDTO.getPercentageTrendVsLastMonth()));
                }
        );
    }

    private void loadAverageTicketStats() {

        executeAsyncTask(
                saleService::getAverageTicketStats,
                averageTicketStatsDTO -> {
                    setTextOnLabel(averageTicketTotal, parseNumberValueToText(averageTicketStatsDTO.getAverageTicket()));
                    setTextOnLabel(extraProductsSoldCount, parseNumberValueToText(averageTicketStatsDTO.getExtraSoldUnits()));
                }
        );
    }

    private void loadSalesTodayStats() {

        executeAsyncTask(
                saleService::getSalesTodayStats,
                todaySalesDTO -> {
                    setTextOnLabel(salesTodayCount, parseNumberValueToText(todaySalesDTO.getSalesRegisteredToday()));
                    setTextOnLabel(mostPopularBarberService, todaySalesDTO.getMostPopularBarberService());
                }
        );
    }

    private void loadProductIncomeStats() {

        executeAsyncTask(
                saleService::getProductIncomeStats,
                productIncomeStatsDTO -> {
                    setTextOnLabel(productsIncome, parseNumberValueToText(productIncomeStatsDTO.getProductTotalIncome()));
                    setTextOnLabel(mostSoldProduct, productIncomeStatsDTO.getMostSoldProductName());
                }
        );
    }
}