package com.repository;

import com.dto.stats.BarberServiceRevenueStatsDTO;
import com.dto.stats.BarberServiceSalesStatsDTO;
import com.dto.stats.BarberServiceUsageStatsDTO;
import com.dto.stats.EmployeeRevenueStatsDTO;
import com.dto.stats.EmployeeServicesCompletedStatsDTO;
import com.dto.stats.PaymentMethodRevenueStatsDTO;
import com.dto.stats.PaymentMethodUsageStatsDTO;
import com.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("""
            SELECT NEW com.dto.stats.PaymentMethodUsageStatsDTO(pm.name, COUNT(s)) FROM Sale s JOIN s.paymentMethodUsed pm GROUP BY pm.name ORDER BY COUNT(s) DESC""")
    List<PaymentMethodUsageStatsDTO> getpaymentMethodUsageStats();

    @Query("""
            SELECT NEW com.dto.stats.PaymentMethodRevenueStatsDTO(pm.name, SUM(s.total)) FROM Sale  s JOIN s.paymentMethodUsed pm GROUP BY pm.name ORDER BY SUM(s.total) DESC""")
    List<PaymentMethodRevenueStatsDTO> getPaymentMethodRevenueStats();

    @Query("""
            SELECT SUM(s.modifierValue) FROM Sale s""")
    Double getSumOfModifierValueOfAllSales();

    @Query("""
            SELECT NEW com.dto.stats.EmployeeRevenueStatsDTO(e.firstName, e.lastName, SUM(s.total)) FROM Employee e JOIN Sale s GROUP BY e.firstName, e.lastName ORDER BY COUNT(s) DESC""")
    List<EmployeeRevenueStatsDTO> getEmployeeRevenueStats();


    @Query("""
            SELECT NEW com.dto.stats.EmployeeServicesCompletedStatsDTO(e.firstName, e.lastName, COUNT(s)) \
            FROM Employee e JOIN Sale s ON s.employee = e \
            GROUP BY e.firstName, e.lastName \
            ORDER BY COUNT(s) DESC""")
    List<EmployeeServicesCompletedStatsDTO> getEmployeeServicesCompletedStats();

    @Query("""
            SELECT NEW com.dto.stats.BarberServiceSalesStatsDTO (b.name, COUNT(s)) FROM BarberService b JOIN Sale s ON s.barberService.barbershopServiceID = b.barbershopServiceID GROUP BY b.name ORDER BY COUNT(s) DESC""")
    List<BarberServiceSalesStatsDTO> getBarberServiceSaleStats();

    @Query("""
            SELECT NEW com.dto.stats.BarberServiceRevenueStatsDTO (b.name, SUM(s.total)) FROM BarberService b JOIN Sale s ON s.barberService.barbershopServiceID = b.barbershopServiceID GROUP BY b.name ORDER BY SUM(s.total) DESC""")
    List<BarberServiceRevenueStatsDTO> getBarberServiceRevenueStats();

    @Query("""
            SELECT NEW com.dto.stats.BarberServiceUsageStatsDTO (b.name, COUNT(s)) FROM BarberService b JOIN Sale s ON s.barberService.barbershopServiceID = b.barbershopServiceID GROUP BY b.name ORDER BY COUNT(s) ASC""")
    List<BarberServiceUsageStatsDTO> getBarbarberServiceUsageStats();
}
