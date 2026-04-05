package com.barbershop.repository;

import com.barbershop.model.MonthlyStockValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyStockValueHistoryRepository extends JpaRepository<MonthlyStockValueHistory, Long> {

    MonthlyStockValueHistory findTop1ByOrderByLocalDateDesc();
}
