package com.repository;

import com.model.ServicePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ServicePriceHistoryRepository extends JpaRepository<ServicePriceHistory, Long> {

    @Query("""
            SELECT AVG(sph.priceAtMoment)\s
            FROM ServicePriceHistory sph\s
            WHERE sph.timestamp = (
                SELECT MAX(sub.timestamp)\s
                FROM ServicePriceHistory sub\s
                WHERE sub.barberService = sph.barberService\s
                  AND sub.timestamp <= :limitDate
            )""")
    Double averageBarberServicePriceUntilDate(@Param("limitDate") LocalDateTime limitDate);
}
