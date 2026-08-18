package com.repository;

import com.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio especializado para la gestión de empleados (Employees).
 * Proporciona operaciones de persistencia y consultas personalizadas para validar rangos de contratación,
 * obtener estadísticas de estado activo/inactivo y realizar búsquedas en vivo con filtros.
 *
 * <p>Extiende {@link JpaRepository} para heredar las funcionalidades básicas de CRUD.</p>
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Obtiene los 5 empleados más recientes ordenados por fecha de contratación descendente.
     * Proporciona una vista rápida de las últimas contrataciones en el sistema sin necesidad de cargar el historial.
     *
     * @return Una lista con un máximo de 5 elementos, ordenados por {@code hireDate} de mayor a menor.
     */
    List<Employee> findTop5ByOrderByHireDateDesc();

    /**
     * Obtiene los 5 empleados que tienen una fecha de terminación registrada, ordenados por dicha fecha descendente.
     * Útil para identificar rápidamente a los empleados que han dejado la empresa recientemente.
     *
     * @return Una lista con un máximo de 5 elementos, ordenados por {@code terminationDate} de mayor a menor.
     */
    List<Employee> findTop5ByTerminationDateIsNotNullOrderByTerminationDateDesc();

    /**
     * Obtiene el número total de empleados actualmente activos en el sistema.
     * Utiliza la bandera {@code isActive} para filtrar los registros válidos.
     *
     * @return La cantidad de empleados con estado activo.
     */
    @Query("SELECT COUNT(E.employeeID) FROM Employee AS E WHERE E.isActive = TRUE")
    Long getActiveEmployees();

    /**
     * Realiza una búsqueda en vivo (live search) de empleados aplicando múltiples filtros simultáneamente.
     * La consulta utiliza lógica de nulo seguro para permitir que los parámetros opcionales no filtren el resultado.
     *
     * <p>Los criterios de búsqueda son:</p>
     * <ul>
     *   <li><b>Nombre:</b> Filtra si el nombre completo (concatenado) coincide parcialmente con la cadena proporcionada, ignorando mayúsculas y minúsculas.</li>
     *   <li><b>Estado:</b> Filtra por estado exacto (activo/inactivo) si se proporciona; si es {@code null}, no aplica filtro.</li>
     *   <li><b>Rango de Fecha:</b> Filtra empleados cuya fecha de contratación cae dentro del rango especificado (inclusive).</li>
     * </ul>
     *
     * @param employeeName   El nombre del empleado a buscar (soporta coincidencia parcial). Si es {@code null}, no se filtra por nombre.
     * @param selectedStatus El estado actual del empleado a filtrar. Si es {@code null}, no se filtra por estado.
     * @param startDate      El inicio del rango temporal para filtrar las contrataciones.
     * @param endDate        El fin del rango temporal para filtrar las contrataciones.
     * @return Una lista de empleados que cumplen con todos los criterios de filtro aplicados.
     */
    @Query("""
            SELECT e FROM Employee e WHERE (:employeeName IS NULL OR LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :employeeName, '%'))) AND (:selectedStatus IS NULL OR e.isActive =:selectedStatus) AND (:startDate IS NULL OR :startDate <= e.hireDate) AND (:endDate IS NULL OR :endDate >= e.hireDate)""")
    List<Employee> liveSearchWithFilters(
            @Param("employeeName") String employeeName,
            @Param("selectedStatus") Boolean selectedStatus,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(a.appointmentID) FROM Appointment a WHERE a.employee.employeeID =:employeeID AND a.startDateTime BETWEEN :startDate AND :endDate")
    Long getMonthlyAppointmentsByEmployee(
            @Param("employeeID") Long employeeID,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}