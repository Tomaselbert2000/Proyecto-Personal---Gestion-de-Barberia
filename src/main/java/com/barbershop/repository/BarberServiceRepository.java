package com.barbershop.repository;

import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.model.BarberService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BarberServiceRepository extends JpaRepository<BarberService, Long> {

    Boolean existsByNameIgnoreCase(String name);

    Boolean existsByNameAndBarbershopServiceIDNot(String name, Long barbershopServiceID);

    Long countByRegistrationTimestampBetween(LocalDateTime registrationTimestampAfter, LocalDateTime registrationTimestampBefore);

    Long countDistinctByServiceCategoryIsNotNull();

    @Query("SELECT AVG (b.price) FROM BarberService b")
    Double getPriceAverage();

    @Query("SELECT MAX(b.price) FROM BarberService b")
    Double getHighestPrice();

    @Query("SELECT MIN(b.price) FROM BarberService b")
    Double getLowestPrice();

    @Query("SELECT b FROM BarberService b WHERE (:name IS NULL OR b.name LIKE CONCAT('%', :name, '%')) AND (:category IS NULL OR b.serviceCategory=:category) AND (:minPrice IS NULL OR b.price >=:minPrice) AND (:maxPrice IS NULL OR b.price <=:maxPrice)")
    List<BarberService> liveSearchWithFilters(@Param("name") String name, @Param("category")BarberServiceCategory category, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}
