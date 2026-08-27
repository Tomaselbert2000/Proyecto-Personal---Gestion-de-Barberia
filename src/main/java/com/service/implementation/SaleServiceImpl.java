package com.service.implementation;

import com.dto.product.ProductItemDTO;
import com.dto.sale.SaleCreationDTO;
import com.dto.sale.SaleInfoDTO;
import com.dto.stats.*;
import com.enums.SaleCompositionFilter;
import com.exceptions.barberservice.BarberServiceNotFoundException;
import com.exceptions.client.ClientNotFoundException;
import com.exceptions.employee.EmployeeNotFoundException;
import com.exceptions.paymentmethod.PaymentMethodNotFoundException;
import com.exceptions.product.ProductNotFoundException;
import com.exceptions.sale.InactiveEmployeeException;
import com.exceptions.sale.InactivePaymentMethodException;
import com.exceptions.sale.InsufficientProductStockException;
import com.exceptions.sale.SaleNotFoundException;
import com.mapper.interfaces.SaleMapper;
import com.model.*;
import com.repository.*;
import com.service.interfaces.SaleService;
import com.validation.sale.SaleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.dto.stats.EmptyStatDTOFactory.*;
import static com.presentation.constants.StringResource.DisplayString.NO_DATA;
import static com.utils.time.TimeCalculation.*;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BarberServiceRepository barberServiceRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final SaleValidator validator;
    private final SaleMapper mapper;

    @Override
    @Transactional
    public void registerNewSale(SaleCreationDTO saleDto) {

        validator.validateDTO(saleDto);

        Client client = loadClient(saleDto.getClientID());

        Employee employee = loadEmployee(saleDto.getEmployeeID());

        BarberService barberService = loadBarberService(saleDto.getBarberServiceID());

        PaymentMethod paymentMethod = loadPaymentMethod(saleDto.getPaymentMethodID());

        checkIfEmployeeIsNullAndBarberServiceNot(employee, barberService);

        checkIfEmployeeIsActive(employee);

        checkIfPaymentMethodIsActive(paymentMethod);

        List<Long> productIDs = generateProductIDsList(saleDto);

        List<Product> productList = loadProductListFromIdList(productIDs);

        validateProductListSizeAgainstIDsList(productIDs, productList);

        Map<Long, Product> productMap = generateProductMapFromList(productList);

        List<SaleItem> saleItemList = new ArrayList<>();

        loadSaleItemListFromDTO(saleItemList, saleDto, productMap);

        Sale newSale = generateSaleEntity(saleDto, client, employee, barberService, paymentMethod, saleItemList);

        saleRepository.save(newSale);
    }

    @Override
    @Transactional
    public void deleteSale(Long saleID) {

        Sale saleOnDB = saleRepository.findById(saleID).orElseThrow(SaleNotFoundException::new);

        restoreStockFromSaleItemList(saleOnDB);

        saleRepository.delete(saleOnDB);
    }

    @Override
    @Transactional
    public SaleInfoDTO getSale(Long saleID) {

        Sale saleOnDB = saleRepository.findById(saleID).orElseThrow(SaleNotFoundException::new);

        return mapper.mapSaleToInfoDTO(saleOnDB);
    }

    @Override
    @Transactional
    public List<SaleInfoDTO> getSaleList() {

        return mapper.mapSaleToInfoDTO(saleRepository.findAll());
    }

    @Override
    public PaymentMethodUsageStatsDTO getMostUsedPaymentMethod() {

        List<PaymentMethodUsageStatsDTO> paymentMethodUsageStatsDTOList = saleRepository.getpaymentMethodUsageStats();

        if (!paymentMethodUsageStatsDTOList.isEmpty()) {

            return paymentMethodUsageStatsDTOList.getFirst();

        } else {

            return emptyPaymentMethodUsageStatsDTO();
        }
    }

    @Override
    public PaymentMethodRevenueStatsDTO getHighestRevenuePaymentMethod() {

        List<PaymentMethodRevenueStatsDTO> paymentMethodRevenueStatsDTOS = saleRepository.getPaymentMethodRevenueStats();

        if (!paymentMethodRevenueStatsDTOS.isEmpty()) {

            return paymentMethodRevenueStatsDTOS.getFirst();

        } else {

            return emptyPaymentMethodRevenueStatsDTO();
        }
    }

    @Override
    public Double getModifierValueSumAcrossAllSales() {

        Double modifierValueSum = saleRepository.getSumOfModifierValueOfAllSales();

        return Objects.requireNonNullElse(modifierValueSum, 0.0);
    }

    @Override
    public EmployeeRevenueStatsDTO getEmployeeWithHighestRevenue() {

        List<EmployeeRevenueStatsDTO> employeeRevenueStatsDTOS = saleRepository.getEmployeeRevenueStats();

        if (!employeeRevenueStatsDTOS.isEmpty()) {

            return employeeRevenueStatsDTOS.getFirst();

        } else {

            return emptyEmployeeRevenueStatsDTO();
        }
    }

    @Override
    public EmployeeServicesCompletedStatsDTO getEmployeeWithMostServicesCompleted() {

        List<EmployeeServicesCompletedStatsDTO> employeeServicesCompletedStatsDTOS = saleRepository.getEmployeeServicesCompletedStats();

        if (!employeeServicesCompletedStatsDTOS.isEmpty()) {

            return employeeServicesCompletedStatsDTOS.getFirst();

        } else {

            return emptyEmployeeServicesCompletedStatsDTO();
        }
    }

    @Override
    public Double getActiveEmployeesAverageServices() {

        Long currentlyActiveEmployees = employeeRepository.getActiveEmployees();
        long registeredSales = saleRepository.count();

        if (currentlyActiveEmployees != 0L) {

            return ((double) registeredSales / currentlyActiveEmployees);

        } else {

            return 0.0;
        }
    }

    @Override
    public BarberServiceSalesStatsDTO getBarberServiceWithMostSales() {

        List<BarberServiceSalesStatsDTO> barberServiceSalesStatsDTOS = saleRepository.getBarberServiceSaleStats();

        if (!barberServiceSalesStatsDTOS.isEmpty()) {

            return barberServiceSalesStatsDTOS.getFirst();

        } else {

            return emptyBarberServiceSaleStatsDTO();
        }
    }

    @Override
    public BarberServiceRevenueStatsDTO getBarberServiceWithHighestRevenue() {

        List<BarberServiceRevenueStatsDTO> barberServiceRevenueStatsDTOS = saleRepository.getBarberServiceRevenueStats();

        if (!barberServiceRevenueStatsDTOS.isEmpty()) {

            return barberServiceRevenueStatsDTOS.getFirst();

        } else {

            return emptyBarberServiceRevenueStatsDTO();
        }
    }

    @Override
    public BarberServiceUsageStatsDTO getBarberServiceWithLowestUsage() {

        List<BarberServiceUsageStatsDTO> barberServiceUsageStatsDTOS = saleRepository.getBarbarberServiceUsageStats();

        if (!barberServiceUsageStatsDTOS.isEmpty()) {

            return barberServiceUsageStatsDTOS.getFirst();

        } else {

            return emptyBarberServiceUsageStatsDTO();
        }
    }

    @Override
    public MonthlyIncomeStatsDTO getMonthlyIncomeStats() {

        LocalDateTime startOfCurrentMonth = getStartOfCurrentMonth().atStartOfDay();
        LocalDateTime endOfCurrentMonth = getEndOfCurrentMonth().atTime(LAST_SECOND_OF_DAY);

        LocalDateTime startOfLastMonth = startOfCurrentMonth.minusMonths(1);
        LocalDateTime endOfLastMonth = endOfCurrentMonth.minusMonths(1);

        Double saleTotalThisMonth = saleRepository.getSaleTotalByDateRange(startOfCurrentMonth, endOfCurrentMonth);
        Double saleTotalTheLastMonth = saleRepository.getSaleTotalByDateRange(startOfLastMonth, endOfLastMonth);

        double percentageTrend;

        if (saleTotalTheLastMonth != 0.0) {

            percentageTrend = (((double) saleTotalThisMonth - saleTotalTheLastMonth) / saleTotalTheLastMonth) * 100;

        } else {

            percentageTrend = 0.0;
        }

        return MonthlyIncomeStatsDTO.builder()
                .currentMonthTotal(BigDecimal.valueOf(saleTotalThisMonth))
                .lastMonthTotal(BigDecimal.valueOf(saleTotalTheLastMonth))
                .percentageTrendVsLastMonth(percentageTrend)
                .build();
    }

    @Override
    public AverageSaleTicketStatDTO getAverageTicketStats() {

        Double averageSaleTotal = saleRepository.getSaleTotalAverage();
        Long extraSoldUnits = saleRepository.getSaleItemsTotalUnits();

        if (averageSaleTotal == 0.0) return emptyAverageSaleTicketStatDTO();

        return AverageSaleTicketStatDTO.builder()
                .averageTicket(BigDecimal.valueOf(averageSaleTotal))
                .extraSoldUnits(extraSoldUnits)
                .build();
    }

    @Override
    public SalesTodayStatDTO getSalesTodayStats() {

        LocalDateTime startOfToday = getStartOfToday();
        LocalDateTime startOfTomorrow = startOfToday.plusDays(1);
        Pageable firstResult = PageRequest.of(0, 1);

        Long salesTodayCount = saleRepository.countByDateAndTimeBetween(startOfToday, startOfTomorrow);
        List<String> popularServices = saleRepository.findMostPopularBarberServiceToday(startOfToday, startOfTomorrow, firstResult);

        return SalesTodayStatDTO.builder()
                .salesRegisteredToday(salesTodayCount)
                .mostPopularBarberService(popularServices.isEmpty() ? NO_DATA : popularServices.getFirst())
                .build();
    }

    @Override
    public ProductOnlyIncomeDTO getProductIncomeStats() {

        Pageable firstResult = PageRequest.of(0, 1);

        BigDecimal productIncome = BigDecimal.valueOf(saleRepository.getSaleItemsTotalUnits());

        List<String> popularProducts = saleRepository.findMostPopularProductToday(firstResult);

        return ProductOnlyIncomeDTO.builder()
                .productTotalIncome(productIncome)
                .mostSoldProductName(popularProducts.isEmpty() ? NO_DATA : popularProducts.getFirst())
                .build();
    }

    @Override
    @Transactional
    public List<SaleInfoDTO> liveSearch(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String paymentMethodSelected,
            String employeeSelected,
            SaleCompositionFilter saleComposition
    ) {

        List<Sale> sales = saleRepository.liveSearch(minPrice, maxPrice, paymentMethodSelected, employeeSelected, saleComposition);

        return mapper.mapSaleToInfoDTO(sales);
    }

    private Client loadClient(Long clientID) {

        return clientRepository.findById(clientID).orElseThrow(ClientNotFoundException::new);
    }

    private Employee loadEmployee(Long employeeID) {

        if (employeeID == null) return null;

        return employeeRepository.findById(employeeID).orElseThrow(EmployeeNotFoundException::new);
    }

    private BarberService loadBarberService(Long barberServiceID) {

        if (barberServiceID == null) return null;

        return barberServiceRepository.findById(barberServiceID).orElseThrow(BarberServiceNotFoundException::new);
    }

    private PaymentMethod loadPaymentMethod(Long paymentMethodID) {

        return paymentMethodRepository.findById(paymentMethodID).orElseThrow(PaymentMethodNotFoundException::new);
    }

    private void checkIfEmployeeIsNullAndBarberServiceNot(Employee employee, BarberService barberService) {

        if (employee == null && barberService != null) throw new EmployeeNotFoundException();
    }

    private void checkIfEmployeeIsActive(Employee employee) {

        if (employee != null) {

            if (!employee.isActive()) throw new InactiveEmployeeException();

        }
    }

    private void checkIfPaymentMethodIsActive(PaymentMethod paymentMethod) {

        if (!paymentMethod.getIsActive()) throw new InactivePaymentMethodException();
    }

    private List<Long> generateProductIDsList(SaleCreationDTO saleDto) {

        return saleDto.getProductsDetail().stream().map(ProductItemDTO::getProductID).distinct().toList();
    }

    private List<Product> loadProductListFromIdList(List<Long> productIDs) {

        return productRepository.findAllById(productIDs);
    }

    private void validateProductListSizeAgainstIDsList(List<Long> productIDs, List<Product> productList) {

        if (productIDs.size() != productList.size()) throw new ProductNotFoundException();
    }

    private Map<Long, Product> generateProductMapFromList(List<Product> productList) {

        return productList.stream().collect(Collectors.toMap(Product::getProductID, p -> p));
    }

    private void loadSaleItemListFromDTO(List<SaleItem> saleItemList, SaleCreationDTO saleDto, Map<Long, Product> productMap) {

        for (ProductItemDTO itemDTO : saleDto.getProductsDetail()) {

            Product product = productMap.get(itemDTO.getProductID());

            checkProductStock(product, itemDTO);

            saleItemList.add(
                    SaleItem.builder()
                            .product(product)
                            .quantity(itemDTO.getQuantity())
                            .unitPrice(product.getCurrentPrice())
                            .build()
            );

            updateStock(product, itemDTO.getQuantity(), false);
        }
    }

    private Sale generateSaleEntity(
            SaleCreationDTO saleDto,
            Client client,
            Employee employee,
            BarberService barberService,
            PaymentMethod paymentMethod,
            List<SaleItem> saleItemList
    ) {

        return mapper.mapSaleCreationDtoToSale(saleDto, client, employee, barberService, paymentMethod, saleItemList);

    }

    private void checkProductStock(Product product, ProductItemDTO itemDTO) {

        if (product.getCurrentStockLevel() < itemDTO.getQuantity()) throw new InsufficientProductStockException();
    }

    private void restoreStockFromSaleItemList(Sale saleOnDB) {

        if (saleOnDB.getItems() == null) return;

        for (SaleItem item : saleOnDB.getItems()) {

            updateStock(item.getProduct(), item.getQuantity(), true);
        }
    }

    private void updateStock(Product product, Integer quantity, boolean isRestoration) {

        Integer currentStock = product.getCurrentStockLevel();

        if (isRestoration) {

            product.setCurrentStockLevel(currentStock + quantity);

        } else {

            product.setCurrentStockLevel(currentStock - quantity);

        }
    }
}
