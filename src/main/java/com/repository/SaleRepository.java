package com.repository;

import com.dto.employee.EmployeeRevenueStatsDTO;
import com.dto.employee.EmployeeServicesCompletedStatsDTO;
import com.dto.payment.PaymentMethodRevenueStatsDTO;
import com.dto.payment.PaymentMethodUsageStatsDTO;
import com.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("""
            SELECT NEW com.dto.payment.PaymentMethodUsageStatsDTO(pm.name, COUNT(s)) FROM Sale s JOIN s.paymentMethodUsed pm GROUP BY pm.name ORDER BY COUNT(s) DESC""")
    List<PaymentMethodUsageStatsDTO> getpaymentMethodUsageStats();

    @Query("""
            SELECT NEW com.dto.payment.PaymentMethodRevenueStatsDTO(pm.name, SUM(s.total)) FROM Sale  s JOIN s.paymentMethodUsed pm GROUP BY pm.name ORDER BY SUM(s.total) DESC""")
    List<PaymentMethodRevenueStatsDTO> getPaymentMethodRevenueStats();

    @Query("""
            SELECT SUM(s.modifierValue) FROM Sale s""")
    Double getSumOfModifierValueOfAllSales();

    @Query("""
            SELECT NEW com.dto.employee.EmployeeRevenueStatsDTO(e.firstName, e.lastName, SUM(s.total)) FROM Employee e JOIN Sale s GROUP BY e.firstName, e.lastName ORDER BY COUNT(s) DESC""")
    List<EmployeeRevenueStatsDTO> getEmployeeRevenueStats();


    @Query("""
            SELECT NEW com.dto.employee.EmployeeServicesCompletedStatsDTO(e.firstName, e.lastName, COUNT(s)) \
            FROM Employee e JOIN Sale s ON s.employee = e \
            GROUP BY e.firstName, e.lastName \
            ORDER BY COUNT(s) DESC""")
    List<EmployeeServicesCompletedStatsDTO> getEmployeeServicesCompletedStats();
}
