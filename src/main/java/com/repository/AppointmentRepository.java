package com.repository;

import com.enums.AppointmentStatus;
import com.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio especializado para la gestión de citas (Appointments).
 * Proporciona operaciones de persistencia y consultas personalizadas para validar conflictos,
 * contar citas por rango de tiempo y realizar búsquedas en vivo con filtros.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Verifica la existencia de una cita superpuesta para un empleado específico en el rango de tiempo proporcionado.
     * Esta operación es crítica durante la creación de nuevas citas para evitar conflictos de horario.
     *
     * @param employeeID    El identificador único del empleado al que se le asignará la cita.
     * @param startDateTime La hora de inicio propuesta para la nueva cita.
     * @param endDateTime   La hora de fin propuesta para la nueva cita.
     * @return {@code true} si existe al menos una cita existente que solapa con el rango proporcionado; {@code false} en caso contrario.
     */
    @Query(
            "SELECT " +
                    "COUNT(A) > 0 " +
                    "FROM Appointment A " +
                    "WHERE " +
                    "A.employee.employeeID=:employeeID " +
                    "AND (:startDateTime < A.endDateTime AND :endDateTime > A.startDateTime)"
    )
    boolean existsOverlappingAppointmentOnCreate(
            @Param("employeeID") Long employeeID,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    /**
     * Verifica la existencia de una cita superpuesta para un empleado específico en el rango de tiempo proporcionado,
     * excluyendo la propia cita que se está actualizando (para permitir reprogramar una cita existente).
     * Esta operación es esencial durante la actualización de citas para validar cambios de horario.
     *
     * @param employeeID             El identificador único del empleado al que pertenece la cita.
     * @param startDateTime          La hora de inicio propuesta para la nueva cita.
     * @param endDateTime            La hora de fin propuesta para la nueva cita.
     * @param appointmentIDToExclude El identificador de la cita actual que se está modificando y debe ser excluida de la validación.
     * @return {@code true} si existe al menos una cita existente (distinta a la actual) que solapa con el rango proporcionado; {@code false} en caso contrario.
     */
    @Query(
            """
                    SELECT \
                    COUNT(A) > 0 \
                    FROM Appointment A \
                    WHERE \
                    A.employee.employeeID=:employeeID \
                    AND (:startDateTime < A.endDateTime AND :endDateTime > A.startDateTime)\
                    AND A.appointmentID <> :appointmentIDToExclude"""
    )
    boolean existsOverlappingAppointmentOnUpdate(
            @Param("employeeID") Long employeeID,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("appointmentIDToExclude") Long appointmentIDToExclude
    );

    /**
     * Cuenta el número total de citas programadas dentro de un rango específico de tiempo.
     * Útil para generar reportes de ocupación o estadísticas de uso por periodo.
     *
     * @param startDateTimeAfter  El límite inferior del rango de tiempo (inclusive).
     * @param startDateTimeBefore El límite superior del rango de tiempo (exclusive).
     * @return La cantidad total de citas encontradas en el rango especificado.
     */
    Long countByStartDateTimeBetween(LocalDateTime startDateTimeAfter, LocalDateTime startDateTimeBefore);

    /**
     * Cuenta las citas programadas dentro de un rango de tiempo específico que tengan un estado actual dado.
     * Permite filtrar estadísticas por estado (ej: confirmadas, pendientes) en un periodo determinado.
     *
     * @param startDateTime  El límite inferior del rango de tiempo (inclusive).
     * @param startDateTime2 El límite superior del rango de tiempo (exclusive).
     * @param currentStatus  El estado actual que deben tener las citas para ser contadas.
     * @return La cantidad de citas que cumplen con los criterios de tiempo y estado.
     */
    Long countByStartDateTimeBetweenAndCurrentStatus(LocalDateTime startDateTime, LocalDateTime startDateTime2, AppointmentStatus currentStatus);

    /**
     * Cuenta el número total de citas que tienen un estado actual específico en el sistema.
     * Útil para obtener un resumen rápido del estado global de las reservas (ej: cuántas están pendientes).
     *
     * @param currentStatus El estado actual a filtrar.
     * @return La cantidad total de citas con el estado especificado.
     */
    Long countByCurrentStatus(AppointmentStatus currentStatus);

    /**
     * Cuenta las citas registradas después de un timestamp específico.
     * Útil para analizar la actividad reciente o citas pendientes de registro.
     *
     * @param registrationTimestampAfter El límite inferior del rango de tiempo de registro (inclusive).
     * @return La cantidad de citas registradas después del momento especificado.
     */
    Long countByRegistrationTimestampAfter(LocalDateTime registrationTimestampAfter);

    /**
     * Cuenta las citas registradas dentro de un rango específico de tiempo.
     * Permite analizar la distribución temporal de los registros de nuevas citas.
     *
     * @param registrationTimestampAfter  El límite inferior del rango de tiempo de registro (inclusive).
     * @param registrationTimestampBefore El límite superior del rango de tiempo de registro (exclusive).
     * @return La cantidad de citas registradas en el periodo especificado.
     */
    Long countByRegistrationTimestampBetween(LocalDateTime registrationTimestampAfter, LocalDateTime registrationTimestampBefore);

    /**
     * Cuenta las citas que tienen un estado actual específico y fueron registradas dentro de un rango de tiempo dado.
     * Combina filtros de estado y temporalidad para reportes granulares.
     *
     * @param currentStatus               El estado actual que deben tener las citas.
     * @param registrationTimestampAfter  El límite inferior del rango de tiempo de registro (inclusive).
     * @param registrationTimestampBefore El límite superior del rango de tiempo de registro (exclusive).
     * @return La cantidad de citas que cumplen con ambos criterios.
     */
    Long countByCurrentStatusAndRegistrationTimestampBetween(AppointmentStatus currentStatus, LocalDateTime registrationTimestampAfter, LocalDateTime registrationTimestampBefore);

    /**
     * Obtiene las 5 citas más recientes ordenadas por fecha y hora de registro descendente.
     * Proporciona una vista rápida de las últimas actividades en el sistema sin necesidad de cargar el historial.
     *
     * @return Una lista con un máximo de 5 elementos, ordenados por {@code registrationTimestamp} de mayor a menor.
     */
    List<Appointment> findTop5ByOrderByRegistrationTimestampDesc();

    /**
     * Obtiene las 5 citas más recientes que tengan un estado actual específico, ordenadas por fecha y hora de registro descendente.
     * Útil para mostrar rápidamente las últimas actividades relacionadas con un estado particular (ej: últimas confirmaciones).
     *
     * @param currentStatus El estado actual que deben tener las citas.
     * @return Una lista con un máximo de 5 elementos, ordenados por {@code registrationTimestamp} de mayor a menor.
     */
    List<Appointment> findTop5ByCurrentStatusOrderByRegistrationTimestampDesc(AppointmentStatus currentStatus);

    /**
     * Realiza una búsqueda en vivo (live search) de citas aplicando múltiples filtros simultáneamente.
     * La consulta utiliza lógica de nulo seguro para permitir que los parámetros opcionales no filtren el resultado.
     *
     * <p>Los criterios de búsqueda son:</p>
     * <ul>
     *   <li><b>Nombre del Cliente:</b> Filtra si el nombre completo (concatenado) coincide parcialmente con la cadena proporcionada.</li>
     *   <li><b>Nombre del Empleado:</b> Filtra si el nombre completo del empleado coincide parcialmente con la cadena proporcionada.</li>
     *   <li><b>Estado:</b> Filtra por estado exacto si se proporciona; si es nulo, no aplica filtro.</li>
     *   <li><b>Rango de Tiempo:</b> Filtra citas cuyo horario de inicio cae dentro del rango especificado.</li>
     * </ul>
     *
     * @param clientName                El nombre del cliente a buscar (soporta coincidencia parcial). Si es {@code null}, no se filtra por nombre.
     * @param selectedAppointmentStatus El estado actual de las citas a filtrar. Si es {@code null}, no se filtra por estado.
     * @param employeeName              El nombre del empleado a buscar (soporta coincidencia parcial). Si es {@code null}, no se filtra por nombre.
     * @param startDateTime             El inicio del rango temporal para filtrar las citas.
     * @param endDateTime               El fin del rango temporal para filtrar las citas.
     * @return Una lista de citas que cumplen con todos los criterios de filtro aplicados.
     */
    @Query("""
            SELECT a FROM Appointment  a WHERE (:clientName IS NULL OR CONCAT(a.client.firstName, ' ', a.client.lastName) LIKE CONCAT('%', :clientName, '%')) AND (:employeeName IS NULL OR CONCAT(a.employee.firstName, ' ', a.employee.lastName) LIKE CONCAT('%', :employeeName, '%')) AND (:status IS NULL OR :status = a.currentStatus) AND (:startDateTime IS NULL OR a.startDateTime BETWEEN :startDateTime AND :endDateTime)""")
    List<Appointment> liveSearchWithFilters(
            @Param("clientName") String clientName,
            @Param("status") AppointmentStatus selectedAppointmentStatus,
            @Param("employeeName") String employeeName,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}