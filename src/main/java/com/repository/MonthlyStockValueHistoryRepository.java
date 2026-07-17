package com.repository;

import com.model.MonthlyStockValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyStockValueHistoryRepository extends JpaRepository<MonthlyStockValueHistory, Long> {

    MonthlyStockValueHistory findTop1ByOrderByLocalDateDesc();
}
