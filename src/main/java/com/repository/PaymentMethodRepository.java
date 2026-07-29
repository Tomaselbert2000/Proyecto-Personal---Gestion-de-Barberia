package com.repository;

import com.enums.PaymentMethodModifierType;
import com.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio especializado para la gestión de métodos de pago (PaymentMethods).
 * Proporciona operaciones de persistencia y consultas personalizadas para validar unicidad,
 * obtener estadísticas de estado activo y realizar búsquedas en vivo con filtros.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    /**
     * Verifica la existencia de un metodo de pago registrado con el nombre proporcionado.
     * Útil para validar la unicidad del nombre antes de crear un nuevo registro.
     *
     * @param name El nombre del metodo de pago a buscar.
     * @return {@code true} si existe al menos un metodo de pago con ese nombre; {@code false} en caso contrario.
     */
    boolean existsByName(String name);

    /**
     * Verifica la existencia de un metodo de pago registrado con el nombre proporcionado, excluyendo explícitamente un metodo existente por su ID.
     * Esta operación es crítica durante la actualización de métodos de pago para permitir renombrar un metodo existente
     * sin que la validación de unicidad falle contra sí mismo.
     *
     * @param name            El nombre del metodo de pago a buscar.
     * @param paymentMethodID El ID del metodo de pago actual que se está modificando y debe ser excluido de la búsqueda.
     * @return {@code true} si existe otro metodo de pago con ese nombre (distinto al actual); {@code false} en caso contrario.
     */
    boolean existsByNameAndPaymentMethodIDNot(String name, Long paymentMethodID);

    /**
     * Realiza una búsqueda en vivo (live search) de métodos de pago aplicando múltiples filtros simultáneamente.
     * La consulta utiliza lógica de nulo seguro para permitir que los parámetros opcionales no filtren el resultado.
     *
     * <p>Los criterios de búsqueda son:</p>
     * <ul>
     *   <li><b>Nombre:</b> Filtra si el nombre del metodo coincide parcialmente con la cadena proporcionada, ignorando mayúsculas y minúsculas.</li>
     *   <li><b>Estado Activo:</b> Filtra por estado activo si se proporciona; si es {@code null}, no aplica filtro.</li>
     *   <li><b>Tipo de Modificador:</b> Filtra por tipo de modificador exacto si se proporciona; si es {@code null}, no aplica filtro.</li>
     * </ul>
     *
     * @param paymentName  El nombre del metodo de pago a buscar (soporta coincidencia parcial). Si es {@code null}, no se filtra por nombre.
     * @param isActive     El estado activo que deben tener los métodos de pago. Si es {@code null}, no se filtra por estado.
     * @param modifierType El tipo de modificador que deben tener los métodos de pago. Si es {@code null}, no se filtra por tipo.
     * @return Una lista de métodos de pago que cumplen con todos los criterios de filtro aplicados.
     */
    @Query("""
            SELECT pm FROM PaymentMethod pm WHERE LOWER(pm.name) LIKE LOWER(CONCAT('%', :paymentName, '%')) AND (pm.isActive = :isActive OR :isActive IS NULL) AND (pm.modifierType = :modifierType OR :modifierType IS NULL)""")
    List<PaymentMethod> paymentMethodLiveSearch(
            @Param("paymentName") String paymentName,
            @Param("isActive") Boolean isActive,
            @Param("modifierType") PaymentMethodModifierType modifierType
    );

    /**
     * Obtiene el número total de métodos de pago marcados como activos en el sistema.
     * Útil para generar reportes de uso o estadísticas de disponibilidad de medios de pago.
     *
     * @return La cantidad total de métodos de pago con estado activo.
     */
    @Query("SELECT COUNT(*) FROM PaymentMethod WHERE isActive = TRUE")
    Long getCountMarkedAsActive();

    /**
     * Obtiene el metodo de pago registrado con el nombre proporcionado.
     * Utilizado para recuperar la entidad completa del metodo de pago cuando se conoce su nombre exacto.
     *
     * @param name El nombre exacto del metodo de pago a buscar.
     * @return El {@link PaymentMethod} encontrado, o {@code null} si no existe un registro con ese nombre.
     */
    PaymentMethod findPaymentMethodByName(String name);
}