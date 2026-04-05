package com.barbershop.repository;

import com.barbershop.enums.ProductCategory;
import com.barbershop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Boolean existsByName(String name);

    Boolean existsByNameAndProductIDNot(String name, Long productID);

    List<Product> findTop5ByOrderByCreationDateDesc();

    @Query("SELECT p FROM Product p WHERE p.currentStockLevel <= p.safetyStockLevel")
    List<Product> getLowStockProducts();

    Long countByCreationDateBetween(LocalDateTime creationDateAfter, LocalDateTime creationDateBefore);

    @Query("SELECT SUM(p.productCost * p.currentStockLevel) FROM Product p")
    Double getTotalStockValue();

    @Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) AND (:category IS NULL OR p.category=:category)")
    List<Product> liveSearchWithFilters(@Param("name") String productName, @Param("category") ProductCategory selectedCategory);
}
