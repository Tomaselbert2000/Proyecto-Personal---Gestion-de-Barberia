package com.repository;

import com.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio especializado para la gestión de clientes (Clients).
 * Proporciona operaciones de persistencia y consultas personalizadas para validar unicidad de identificadores,
 * buscar clientes por nombre y analizar estadísticas de registro temporal.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Obtiene un cliente específico por su número de documento de identidad nacional.
     * Utilizado para recuperar la entidad completa del cliente cuando se conoce su ID único oficial.
     *
     * @param nationalIdentityCardNumber El número de documento de identidad nacional del cliente.
     * @return Un {@link Optional} conteniendo el {@link Client} si existe, o vacío en caso contrario.
     */
    Optional<Client> findByNationalIdentityCardNumber(String nationalIdentityCardNumber);

    /**
     * Verifica la existencia de un cliente registrado con un número de documento de identidad nacional específico.
     * Útil para validar la unicidad del documento antes de crear un nuevo registro.
     *
     * @param nationalIdentityCardNumber El número de documento de identidad nacional a buscar.
     * @return {@code true} si existe al menos un cliente con ese documento; {@code false} en caso contrario.
     */
    boolean existsByNationalIdentityCardNumber(String nationalIdentityCardNumber);

    /**
     * Verifica la existencia de un cliente registrado con una dirección de correo electrónico específica.
     * Útil para validar la unicidad del email antes de crear un nuevo registro.
     *
     * @param email La dirección de correo electrónico a buscar.
     * @return {@code true} si existe al menos un cliente con ese email; {@code false} en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica la existencia de un cliente que tenga al menos uno de los números de teléfono proporcionados en la lista.
     * La consulta realiza una unión interna con la colección de teléfonos del cliente para validar la pertenencia.
     *
     * @param phones Una lista de números de teléfono a buscar.
     * @return {@code true} si existe al menos un cliente que tenga uno o más de los números proporcionados; {@code false} en caso contrario.
     */
    @Query("SELECT COUNT(c) > 0 FROM Client c JOIN c.phoneNumbersList p WHERE p IN :phones")
    boolean existsByAnyPhoneNumberInList(@Param("phones") List<String> phones);

    /**
     * Verifica la existencia de un cliente con un número de documento específico, excluyendo explícitamente al cliente actual por su ID.
     * Esta operación es crítica durante la actualización de clientes para permitir cambiar el documento de identidad
     * sin que la validación de unicidad falle contra sí mismo.
     *
     * @param nationalIdentityCardNumber El número de documento de identidad nacional a buscar.
     * @param clientID                   El ID del cliente actual que se está modificando y debe ser excluido de la búsqueda.
     * @return {@code true} si existe otro cliente con ese documento (distinto al actual); {@code false} en caso contrario.
     */
    boolean existsByNationalIdentityCardNumberAndClientIDNot(String nationalIdentityCardNumber, Long clientID);

    /**
     * Verifica la existencia de un cliente con una dirección de correo electrónico específica, excluyendo explícitamente al cliente actual por su ID.
     * Esta operación es esencial durante la actualización de clientes para permitir cambiar el email sin que la validación falle contra sí mismo.
     *
     * @param email    La dirección de correo electrónico a buscar.
     * @param clientID El ID del cliente actual que se está modificando y debe ser excluido de la búsqueda.
     * @return {@code true} si existe otro cliente con ese email (distinto al actual); {@code false} en caso contrario.
     */
    boolean existsByEmailAndClientIDNot(String email, Long clientID);

    /**
     * Verifica la existencia de un cliente que tenga al menos uno de los números de teléfono proporcionados, excluyendo explícitamente al cliente actual por su ID.
     * Permite cambiar el número de teléfono de un cliente existente sin conflictos de unicidad contra sí mismo.
     *
     * @param phones   Una lista de números de teléfono a buscar.
     * @param clientID El ID del cliente actual que se está modificando y debe ser excluido de la búsqueda.
     * @return {@code true} si existe otro cliente con uno de los teléfonos proporcionados (distinto al actual); {@code false} en caso contrario.
     */
    @Query("""
            SELECT COUNT(c) > 0 FROM Client c JOIN c.phoneNumbersList p WHERE p IN :phones AND c.clientID != :idExclude""")
    boolean existsByAnyPhoneNumberInListAndClientIDNot(@Param("phones") List<String> phones, @Param("idExclude") Long clientID);

    /**
     * Cuenta el número total de clientes registrados dentro de un rango específico de fechas.
     * Útil para generar reportes de crecimiento de la base de usuarios por periodo.
     *
     * @param registrationDateAfter  El límite inferior del rango de fechas (inclusive).
     * @param registrationDateBefore El límite superior del rango de fechas (exclusive).
     * @return La cantidad total de clientes registrados en el rango especificado.
     */
    Long countByRegistrationDateBetween(LocalDate registrationDateAfter, LocalDate registrationDateBefore);

    /**
     * Obtiene los 5 clientes más recientes ordenados por fecha y hora de registro descendente.
     * Proporciona una vista rápida de los últimos registros en el sistema sin necesidad de cargar el historial.
     *
     * @return Una lista con un máximo de 5 elementos, ordenados por {@code registrationDate} de mayor a menor.
     */
    List<Client> findTop5ByOrderByRegistrationDateDesc();

    /**
     * Realiza una búsqueda en vivo (live search) de clientes aplicando un filtro por nombre.
     * La consulta utiliza lógica de nulo seguro y comparación case-insensitive para permitir coincidencias parciales.
     *
     * <p>El criterio de búsqueda es:</p>
     * <ul>
     *   <li><b>Nombre:</b> Filtra si el nombre completo (concatenado) coincide parcialmente con la cadena proporcionada, ignorando mayúsculas y minúsculas.</li>
     * </ul>
     *
     * @param searchName El nombre del cliente a buscar (soporta coincidencia parcial). Si es {@code null}, no se filtra por nombre.
     * @return Una lista de clientes que cumplen con el criterio de búsqueda aplicado.
     */
    @Query("""
            SELECT c FROM Client c WHERE (:searchName IS NULL OR LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :searchName, '%')))""")
    List<Client> clientLiveSearchByName(@Param("searchName") String searchName);
}