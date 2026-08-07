package com.repository;

import com.dto.stats.InventoryAlertStatsDTO;
import com.dto.stats.ProductStockValueStatsDTO;
import com.dto.stats.ProductTotalStockStatsDTO;
import com.enums.ProductCategory;
import com.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio especializado para la gestión de productos (Products).
 * Proporciona operaciones de persistencia y consultas personalizadas para validar unicidad,
 * identificar productos con stock bajo, calcular el valor total del inventario y realizar búsquedas en vivo con filtros.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Verifica la existencia de un producto registrado con el nombre proporcionado.
     * Útil para validar la unicidad del nombre antes de crear un nuevo registro.
     *
     * @param name El nombre del producto a buscar.
     * @return {@code true} si existe al menos un producto con ese nombre; {@code false} en caso contrario.
     */
    Boolean existsByName(String name);

    /**
     * Verifica la existencia de un producto registrado con el nombre proporcionado, excluyendo explícitamente un producto existente por su ID.
     * Esta operación es crítica durante la actualización de productos para permitir renombrar un producto existente
     * sin que la validación de unicidad falle contra sí mismo.
     *
     * @param name      El nombre del producto a buscar.
     * @param productID El ID del producto actual que se está modificando y debe ser excluido de la búsqueda.
     * @return {@code true} si existe otro producto con ese nombre (distinto al actual); {@code false} en caso contrario.
     */
    Boolean existsByNameAndProductIDNot(String name, Long productID);

    /**
     * Obtiene los 5 productos más recientes ordenados por fecha y hora de creación descendente.
     * Proporciona una vista rápida de las últimas entradas en el inventario sin necesidad de cargar el historial.
     *
     * @return Una lista con un máximo de 5 elementos, ordenados por {@code creationDate} de mayor a menor.
     */
    List<Product> findTop5ByOrderByCreationDateDesc();

    /**
     * Obtiene una lista de productos cuyo nivel de stock actual es menor o igual al nivel de seguridad definido.
     * La consulta utiliza una comparación directa en la base de datos para identificar rápidamente los artículos que requieren reposición.
     *
     * @return Una lista de productos con stock bajo (donde {@code currentStockLevel <= safetyStockLevel}).
     */
    @Query("""
            SELECT p
            FROM Product p
            WHERE p.currentStockLevel <= p.safetyStockLevel
            """)
    List<Product> getLowStockProducts();

    /**
     * Realiza una búsqueda en vivo (live search) de productos aplicando múltiples filtros simultáneamente.
     * La consulta utiliza lógica de nulo seguro para permitir que los parámetros opcionales no filtren el resultado.
     *
     * <p>Los criterios de búsqueda son:</p>
     * <ul>
     *   <li><b>Nombre:</b> Filtra si el nombre del producto coincide parcialmente con la cadena proporcionada.</li>
     *   <li><b>Categoría:</b> Filtra por categoría exacta si se proporciona; si es {@code null}, no aplica filtro.</li>
     * </ul>
     *
     * @param productName      El nombre del producto a buscar (soporta coincidencia parcial). Si es {@code null}, no se filtra por nombre.
     * @param selectedCategory La categoría del producto a filtrar. Si es {@code null}, no se filtra por categoría.
     * @return Una lista de productos que cumplen con todos los criterios de filtro aplicados.
     */
    @Query("""
            SELECT p
            FROM Product p
            WHERE (:name IS NULL OR p.name
            LIKE CONCAT('%', :name, '%'))
            AND (:category IS NULL OR p.category=:category)
            """)
    List<Product> liveSearchWithFilters(@Param("name") String productName, @Param("category") ProductCategory selectedCategory);

    /**
     * Obtiene estadísticas sobre el estado del inventario.
     * La consulta calcula la cantidad total de productos registrados y la cantidad acumulada de productos que se encuentran en estado de stock bajo o crítico.
     *
     * @return Un objeto {@link ProductTotalStockStatsDTO} conteniendo la cantidad total de productos y la cantidad de productos con stock bajo o crítico.
     */
    @Query("""
            SELECT new com.dto.stats.ProductTotalStockStatsDTO(COUNT(p.productID), SUM(CASE WHEN p.stockStatus = StockStatus.BAJO OR p.stockStatus = StockStatus.CRITICO THEN 1 ELSE 0 END)) FROM Product p""")
    ProductTotalStockStatsDTO getProductCountAndStockLevelStats();

    @Query("""
            SELECT new com.dto.stats.ProductStockValueStatsDTO(SUM(p.productCost * p.currentStockLevel), SUM(p.currentStockLevel)) FROM Product p""")
    ProductStockValueStatsDTO getTotalStockValue();

    @Query("""
            SELECT new com.dto.stats.InventoryAlertStatsDTO(
            COUNT(p.productID),
            SUM(CASE WHEN p.stockStatus = StockStatus.BAJO OR p.stockStatus = StockStatus.CRITICO THEN 1 ELSE 0 END))
            FROM Product p
            WHERE p.stockStatus IN (StockStatus.BAJO, StockStatus.CRITICO) AND p.currentStockLevel = 0
            """)
    List<InventoryAlertStatsDTO> getInventoryAlertStats();
}