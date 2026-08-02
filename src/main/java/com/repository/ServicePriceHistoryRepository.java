package com.repository;

import com.model.ServicePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio especializado para la gestión del historial de precios de servicios (ServicePriceHistory).
 * Proporciona operaciones de persistencia y consultas personalizadas para calcular promedios de precios históricos
 * hasta una fecha límite específica, útil para análisis de tendencias de precios o comparativas temporales.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface ServicePriceHistoryRepository extends JpaRepository<ServicePriceHistory, Long> {

}