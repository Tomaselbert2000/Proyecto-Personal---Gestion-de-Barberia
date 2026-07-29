package com.repository;

import com.model.ServicePriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * Repositorio especializado para la gestión del historial de precios de servicios (ServicePriceHistory).
 * Proporciona operaciones de persistencia y consultas personalizadas para calcular promedios de precios históricos
 * hasta una fecha límite específica, útil para análisis de tendencias de precios o comparativas temporales.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface ServicePriceHistoryRepository extends JpaRepository<ServicePriceHistory, Long> {

    /**
     * Calcula el precio promedio de los servicios registrados hasta una fecha límite específica.
     * La consulta identifica el último registro disponible para cada servicio (basado en la fecha y hora)
     * que sea menor o igual a la fecha proporcionada, y luego promedia esos precios.
     *
     * <p>Esta lógica asegura que se utilice siempre el precio más reciente disponible para cada servicio
     * dentro del rango temporal solicitado, evitando duplicaciones o registros obsoletos en el cálculo.</p>
     *
     * @param limitDate La fecha límite hasta la cual se deben considerar los precios históricos (inclusive).
     * @return El valor promedio calculado sobre los precios de los servicios hasta la fecha límite.
     * Retorna {@code null} si no existen registros válidos dentro del rango especificado.
     */
    @Query("""
            SELECT AVG(sph.priceAtMoment)
            FROM ServicePriceHistory sph
            WHERE sph.timestamp = (
                SELECT MAX(sub.timestamp)
                FROM ServicePriceHistory sub
                WHERE sub.barberService = sph.barberService
                  AND sub.timestamp <= :limitDate
            )""")
    Double averageBarberServicePriceUntilDate(@Param("limitDate") LocalDateTime limitDate);
}