package com.service.interfaces;

import com.dto.stats.BarberServiceRevenueStatsDTO;
import com.dto.stats.BarberServiceSalesStatsDTO;
import com.dto.stats.BarberServiceUsageStatsDTO;
import com.dto.stats.EmployeeRevenueStatsDTO;
import com.dto.stats.EmployeeServicesCompletedStatsDTO;
import com.dto.stats.PaymentMethodRevenueStatsDTO;
import com.dto.stats.PaymentMethodUsageStatsDTO;
import com.dto.sale.SaleCreationDTO;

public interface SaleService {

    void registerNewSale(SaleCreationDTO saleDto);

    void deleteSale(Long saleID);

    PaymentMethodUsageStatsDTO getMostUsedPaymentMethod();

    PaymentMethodRevenueStatsDTO getHighestRevenuePaymentMethod();

    Double getModifierValueSumAcrossAllSales();

    EmployeeRevenueStatsDTO getEmployeeWithHighestRevenue();

    EmployeeServicesCompletedStatsDTO getEmployeeWithMostServicesCompleted();

    Double getActiveEmployeesAverageServices();

    BarberServiceSalesStatsDTO getBarberServiceWithMostSales();

    BarberServiceRevenueStatsDTO getBarberServiceWithHighestRevenue();

    BarberServiceUsageStatsDTO getBarberServiceWithLowestUsage();
}
