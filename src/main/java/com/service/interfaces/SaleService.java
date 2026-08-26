package com.service.interfaces;

import com.dto.sale.SaleCreationDTO;
import com.dto.sale.SaleInfoDTO;
import com.dto.stats.*;
import com.enums.SaleCompositionFilter;

import java.math.BigDecimal;
import java.util.List;

public interface SaleService {

    void registerNewSale(SaleCreationDTO saleDto);

    void deleteSale(Long saleID);

    SaleInfoDTO getSale(Long saleID);

    List<SaleInfoDTO> getSaleList();

    PaymentMethodUsageStatsDTO getMostUsedPaymentMethod();

    PaymentMethodRevenueStatsDTO getHighestRevenuePaymentMethod();

    Double getModifierValueSumAcrossAllSales();

    EmployeeRevenueStatsDTO getEmployeeWithHighestRevenue();

    EmployeeServicesCompletedStatsDTO getEmployeeWithMostServicesCompleted();

    Double getActiveEmployeesAverageServices();

    BarberServiceSalesStatsDTO getBarberServiceWithMostSales();

    BarberServiceRevenueStatsDTO getBarberServiceWithHighestRevenue();

    BarberServiceUsageStatsDTO getBarberServiceWithLowestUsage();

    MonthlyIncomeStatsDTO getMonthlyIncomeStats();

    AverageSaleTicketStatDTO getAverageTicketStats();

    SalesTodayStatDTO getSalesTodayStats();

    ProductOnlyIncomeDTO getProductIncomeStats();

    List<SaleInfoDTO> liveSearch(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String paymentMethodSelected,
            String employeeSelected,
            SaleCompositionFilter saleComposition
    );
}
