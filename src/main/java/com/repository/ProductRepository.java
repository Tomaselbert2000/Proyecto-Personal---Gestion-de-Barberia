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

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    /**
     * Verifica la existencia de un producto registrado con el nombre proporcionado, excluyendo explícitamente un producto existente por su ID.
     * Esta operación es crítica durante la actualización de productos para permitir renombrar un producto existente
     * sin que la validación de unicidad falle contra sí mismo.
     *
     * @param name      El nombre del producto a buscar.
     * @param productID El ID del producto actual que se está modificando y debe ser excluido de la búsqueda.
     * @return {@code true} si existe otro producto con ese nombre (distinto al actual); {@code false} en caso contrario.
     */
    boolean existsByNameAndProductIDNot(String name, Long productID);

    List<Product> findTop5ByOrderByCreationDateDesc();

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