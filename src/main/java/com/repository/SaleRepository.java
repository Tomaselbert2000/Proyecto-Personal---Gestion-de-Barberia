package com.repository;

import com.dto.stats.*;
import com.enums.SaleCompositionFilter;
import com.model.Sale;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            SELECT NEW com.dto.stats.EmployeeServicesCompletedStatsDTO(e.firstName, e.lastName, COUNT(s))
            FROM Employee e JOIN Sale s ON s.employee = e
            GROUP BY e.firstName, e.lastName
            ORDER BY COUNT(s) DESC
            """)
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

    @Query("SELECT COALESCE(SUM(s.total), 0.0) FROM Sale s WHERE s.dateAndTime BETWEEN :minRange AND :maxRange")
    Double getSaleTotalByDateRange(@Param("minRange") LocalDateTime minRange, @Param("maxRange") LocalDateTime maxRange);

    @Query("SELECT COUNT(s.saleID) FROM Sale s WHERE s.dateAndTime BETWEEN :todayStart AND :tomorrowStart")
    Long countByDateAndTimeBetween(@Param("todayStart") LocalDateTime todayStart,@Param("tomorrowStart") LocalDateTime tomorrowStart);

    @Query("""
                SELECT bs.name
                FROM Sale s JOIN s.barberService bs
                WHERE s.dateAndTime >= :todayStart AND s.dateAndTime < :tomorrowStart
                GROUP BY bs.name
                ORDER BY COUNT(s) DESC
            """)
    List<String> findMostPopularBarberServiceToday(
            @Param("todayStart") LocalDateTime todayStart,
            @Param("tomorrowStart") LocalDateTime tomorrowStart,
            Pageable pageable
    );

    @Query("""
            SELECT p.name
            FROM SaleItem si JOIN si.product p
            GROUP BY p.name
            ORDER BY SUM(si.quantity) DESC
            """)
    List<String> findMostPopularProductToday(Pageable pageable);

    @Query("""
                SELECT s FROM Sale s
                    WHERE (:minPrice IS NULL OR s.total >= :minPrice)
                      AND (:maxPrice IS NULL OR s.total <= :maxPrice)
                      AND (:paymentMethodSelected IS NULL OR UPPER(s.paymentMethodUsed.name) LIKE '%' || UPPER(:paymentMethodSelected) || '%')
                      AND (:employeeName IS NULL OR UPPER(s.employee.firstName || ' ' || s.employee.lastName) LIKE '%' || UPPER(:employeeName) || '%')
                      AND (:saleCompositionType IS NULL OR s.saleComposition = :saleCompositionType)
                ORDER BY s.dateAndTime DESC
            """)
    List<Sale> liveSearch(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("paymentMethodSelected") String paymentMethodSelected,
            @Param("employeeName") String employeeName,
            @Param("saleCompositionType") SaleCompositionFilter saleCompositionType
    );

    @Query("SELECT COALESCE(AVG(s.total), 0.0) FROM Sale s")
    Double getSaleTotalAverage();

    @Query("SELECT COALESCE(SUM(si.quantity), 0L) FROM SaleItem si JOIN si.product p")
    Long getSaleItemsTotalUnits();
}