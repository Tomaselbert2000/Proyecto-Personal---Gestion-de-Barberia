package com.repository;

import com.dto.stats.ClientNotesStatsDTO;
import com.dto.stats.ClientPhoneNumberStatsDTO;
import com.dto.stats.ClientRegistrationTrendStatDTO;
import com.dto.stats.TotalClientsStatsDTO;
import com.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Obtiene un cliente específico por su número de documento de identidad nacional.
     * Utilizado para recuperar la entidad completa del cliente cuando se conoce su ID único oficial.
     *
     * @param nationalIdentityCardNumber El número de documento de identidad nacional del cliente.
     * @return Un {@link Optional} conteniendo el {@link Client} si existe, o vacío en caso contrario.
     */
    Optional<Client> findByNationalIdentityCardNumber(String nationalIdentityCardNumber);

    boolean existsByNationalIdentityCardNumber(String nationalIdentityCardNumber);

    boolean existsByEmail(String email);

    /**
     * Verifica la existencia de un cliente que tenga al menos uno de los números de teléfono proporcionados en la lista.
     * La consulta realiza una unión interna con la colección de teléfonos del cliente para validar la pertenencia.
     *
     * @param phones Una lista de números de teléfono a buscar.
     * @return {@code true} si existe al menos un cliente que tenga uno o más de los números proporcionados; {@code false} en caso contrario.
     */
    @Query("""
            SELECT COUNT(c) > 0
            FROM Client c JOIN c.phoneNumbersList p WHERE p IN :phones
            """)
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
            SELECT COUNT(c) > 0
            FROM Client c JOIN c.phoneNumbersList p WHERE p IN :phones AND c.clientID != :idExclude
            """)
    boolean existsByAnyPhoneNumberInListAndClientIDNot(
            @Param("phones") List<String> phones,
            @Param("idExclude") Long clientID
    );

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
            SELECT c
            FROM Client c WHERE (:searchName IS NULL OR LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :searchName, '%')))
            """)
    List<Client> clientLiveSearchByName(@Param("searchName") String searchName);

    @Query("""
            SELECT COALESCE(COUNT(c.clientID), 0)
            FROM Client c WHERE c.registrationDate BETWEEN :minRange AND :maxRange
            """)
    Long getClientCountByRegistrationDateRange(
            @Param("minRange") LocalDate minRange,
            @Param("maxRange") LocalDate maxRange
    );

    @Query("""
            SELECT c FROM Client c
            WHERE (:clientName IS NULL OR LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :clientName, '%')))
              AND (:limitRegistrationDate IS NULL OR (c.registrationDate >= :limitRegistrationDate AND c.registrationDate <= CURRENT_DATE))
              AND (
                  (:hasPhone IS NULL) OR (:hasPhone = true AND EXISTS (SELECT 1 FROM c.phoneNumbersList p)) OR (:hasPhone = false AND NOT EXISTS (SELECT 1 FROM c.phoneNumbersList p))
              )
              AND (
                  (:hasNotes IS NULL) OR
                  (:hasNotes = true AND c.optionalNotes IS NOT NULL AND TRIM(c.optionalNotes) != '') OR
                  (:hasNotes = false AND (c.optionalNotes IS NULL OR TRIM(c.optionalNotes) = ''))
              )
            """)
    List<Client> liveSearch(
            @Param("clientName") String clientName,
            @Param("limitRegistrationDate") LocalDate limitRegistrationDate,
            @Param("hasPhone") Boolean hasPhone,
            @Param("hasNotes") Boolean hasNotes
    );

    @Query("""
            SELECT new com.dto.stats.TotalClientsStatsDTO(
                COUNT(c.clientID),
                COUNT(CASE WHEN c.registrationDate >= :currentMonthStart AND c.registrationDate < :currentMonthEnd THEN c.clientID ELSE NULL END)
            )
            FROM Client c
            """)
    TotalClientsStatsDTO getTotalClientsStats(
            @Param("currentMonthStart") LocalDate currentMonthStart,
            @Param("currentMonthEnd") LocalDate currentMonthEnd
    );

    @Query("""
            SELECT new com.dto.stats.ClientPhoneNumberStatsDTO(
                COUNT(DISTINCT(CASE WHEN p IS NOT NULL THEN c.clientID END)),
                COUNT(DISTINCT(CASE WHEN p IS NULL THEN c.clientID END))
            )
            FROM Client c LEFT JOIN c.phoneNumbersList p
            """)
    ClientPhoneNumberStatsDTO getPhoneNumberRegistrationStats();

    @Query("""
            SELECT new com.dto.stats.ClientRegistrationTrendStatDTO(
                COUNT(CASE WHEN c.registrationDate >= :currentMonthStart AND c.registrationDate < :currentMonthEnd THEN c.clientID ELSE NULL END),
                COUNT(CASE WHEN c.registrationDate >= :previousMonthStart AND c.registrationDate < :previousMonthEnd THEN c.clientID ELSE NULL END)
            )
            FROM Client c
            """)
    ClientRegistrationTrendStatDTO getClientRegistrationTrend(
            @Param("currentMonthStart") LocalDate currentMonthStart,
            @Param("currentMonthEnd") LocalDate currentMonthEnd,
            @Param("previousMonthStart") LocalDate previousMonthStart,
            @Param("previousMonthEnd") LocalDate previousMonthEnd
    );

    @Query("""
            SELECT new com.dto.stats.ClientNotesStatsDTO(
                COUNT(CASE WHEN c.optionalNotes IS NOT NULL AND TRIM(c.optionalNotes) != '' THEN c.clientID ELSE NULL END),
                COUNT(CASE WHEN c.optionalNotes IS NULL OR TRIM(c.optionalNotes) = '' THEN c.clientID ELSE NULL END)
            )
            FROM Client c
            """)
    ClientNotesStatsDTO getClientsNotesStats();
}