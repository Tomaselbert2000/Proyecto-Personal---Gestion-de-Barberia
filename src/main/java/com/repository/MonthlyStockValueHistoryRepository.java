package com.repository;

import com.model.MonthlyStockValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio especializado para la gestión del historial de valor de inventario mensual (MonthlyStockValueHistory).
 * Proporciona operaciones de persistencia y consultas personalizadas para obtener el último registro disponible.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface MonthlyStockValueHistoryRepository extends JpaRepository<MonthlyStockValueHistory, Long> {

    /**
     * Obtiene el único registro más reciente del historial de valor de inventario mensual.
     * La consulta ordena los resultados por fecha local descendente para recuperar la entrada más actualizada.
     *
     * @return El {@link MonthlyStockValueHistory} más reciente encontrado en la base de datos, o {@code null} si no existen registros.
     */
    MonthlyStockValueHistory findTop1ByOrderByLocalDateDesc();
}