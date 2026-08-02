package com.repository;

import com.dto.stats.ProductHighestRevenueStatsDTO;
import com.dto.stats.ProductMostSoldStatsDTO;
import com.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    @Query("""
            SELECT new com.dto.stats.ProductMostSoldStatsDTO(p.name, SUM(si.quantity))
                        FROM SaleItem si
                        JOIN si.product p
                        GROUP BY p.productID
                        ORDER BY SUM(si.quantity) DESC
            """)
    List<ProductMostSoldStatsDTO> getProductsSaleStats();

    @Query("""
            SELECT new com.dto.stats.ProductHighestRevenueStatsDTO(p.name, SUM(si.unitPrice * si.quantity))
                        FROM SaleItem si
                        JOIN si.product p
                        GROUP BY p.productID
                        ORDER BY SUM(si.unitPrice * si.quantity) DESC
            """)
    List<ProductHighestRevenueStatsDTO> getProductRevenueStats();
}
