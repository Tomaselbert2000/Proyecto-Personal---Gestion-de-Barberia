package com.barbershop.repository;

import com.barbershop.model.ServicePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ServicePriceHistoryRepository extends JpaRepository<ServicePriceHistory, Long> {

    @Query("SELECT AVG(sph.priceAtMoment) \n" +
            "FROM ServicePriceHistory sph \n" +
            "WHERE sph.timestamp = (\n" +
            "    SELECT MAX(sub.timestamp) \n" +
            "    FROM ServicePriceHistory sub \n" +
            "    WHERE sub.barberService = sph.barberService \n" +
            "      AND sub.timestamp <= :limitDate\n" +
            ")")
    Double averageBarberServicePriceUntilDate(@Param("limitDate") LocalDateTime limitDate);
}
