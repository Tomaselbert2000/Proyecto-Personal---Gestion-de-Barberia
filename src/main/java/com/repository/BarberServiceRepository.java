package com.repository;

import com.dto.stats.BarberServiceActiveOnCatalogStatsDTO;
import com.enums.BarberServiceCategory;
import com.model.BarberService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio especializado para la gestión de servicios de barbería (BarberServices).
 * Proporciona operaciones de persistencia y consultas personalizadas para validar unicidad,
 * calcular estadísticas de precios (promedio, máximo, mínimo) y realizar búsquedas en vivo con filtros.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface BarberServiceRepository extends JpaRepository<BarberService, Long> {

    /**
     * Verifica la existencia de un servicio con el nombre proporcionado, ignorando mayúsculas y minúsculas.
     * Útil para validar la unicidad del nombre del servicio antes de crear uno nuevo.
     *
     * @param name El nombre del servicio a buscar (case-insensitive).
     * @return {@code true} si existe al menos un servicio con ese nombre; {@code false} en caso contrario.
     */
    Boolean existsByNameIgnoreCase(String name);

    /**
     * Verifica la existencia de un servicio con el nombre proporcionado, excluyendo explícitamente un servicio existente por su ID.
     * Esta operación es crítica durante la actualización de servicios para permitir renombrar un servicio existente
     * sin que la validación de unicidad falle contra sí mismo.
     *
     * @param name                El nombre del servicio a buscar (case-insensitive).
     * @param barbershopServiceID El ID del servicio actual que se está modificando y debe ser excluido de la búsqueda.
     * @return {@code true} si existe otro servicio con ese nombre (distinto al actual); {@code false} en caso contrario.
     */
    Boolean existsByNameAndBarbershopServiceIDNot(String name, Long barbershopServiceID);

    /**
     * Realiza una búsqueda en vivo (live search) de servicios aplicando múltiples filtros simultáneamente.
     * La consulta utiliza lógica de nulo seguro para permitir que los parámetros opcionales no filtren el resultado.
     *
     * <p>Los criterios de búsqueda son:</p>
     * <ul>
     *   <li><b>Nombre:</b> Filtra si el nombre del servicio coincide parcialmente con la cadena proporcionada.</li>
     *   <li><b>Categoría:</b> Filtra por categoría exacta si se proporciona; si es {@code null}, no aplica filtro.</li>
     *   <li><b>Rango de Precio:</b> Filtra servicios cuyo precio cae dentro del rango especificado (inclusive).</li>
     * </ul>
     *
     * @param name     El nombre del servicio a buscar (soporta coincidencia parcial). Si es {@code null}, no se filtra por nombre.
     * @param category La categoría del servicio a filtrar. Si es {@code null}, no se filtra por categoría.
     * @param minPrice El límite inferior del rango de precios. Si es {@code null}, no se filtra por precio mínimo.
     * @param maxPrice El límite superior del rango de precios. Si es {@code null}, no se filtra por precio máximo.
     * @return Una lista de servicios que cumplen con todos los criterios de filtro aplicados.
     */
    @Query("""
            SELECT b FROM BarberService b WHERE (:name IS NULL OR b.name LIKE CONCAT('%', :name, '%')) AND (:category IS NULL OR b.serviceCategory=:category) AND (:minPrice IS NULL OR b.price >=:minPrice) AND (:maxPrice IS NULL OR b.price <=:maxPrice)""")
    List<BarberService> liveSearchWithFilters(@Param("name") String name, @Param("category") BarberServiceCategory category, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    /**
     * Obtiene estadísticas sobre los servicios activos del catálogo.
     * La consulta agrupa los servicios activos (isCurrentlyActive = true) y calcula la cantidad total de servicios activos
     * junto con la cantidad de categorías distintas representadas en ese conjunto.
     *
     * @return Un objeto {@link BarberServiceActiveOnCatalogStatsDTO} conteniendo la cantidad de servicios activos
     * y la cantidad de categorías diferentes. Retorna {@code null} si no existen servicios activos.
     */
    @Query("""
            SELECT NEW com.dto.stats.BarberServiceActiveOnCatalogStatsDTO(
                COUNT(b),
                COUNT(DISTINCT b.serviceCategory)
            )
            FROM BarberService b
            WHERE b.isCurrentlyActive = true""")
    BarberServiceActiveOnCatalogStatsDTO getActiveBarberServicesStats();
}