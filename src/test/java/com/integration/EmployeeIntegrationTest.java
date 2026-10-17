package com.integration;

import com.abstract_test_class.BaseIntegrationTest;
import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.dto.stats.MonthlyAppointmentCountDTO;
import com.model.Employee;
import com.repository.EmployeeRepository;
import com.service.interfaces.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.enums.EmployeeStatus.ACTIVO;
import static com.enums.HireDateRange.ESTE_AÑO;
import static com.factory.EmployeeTestDataFactory.*;
import static com.presentation.constants.StringResource.DisplayString.EMPLOYEE_COMBOBOX_NO_FILTER;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
public class EmployeeIntegrationTest extends BaseIntegrationTest<
        EmployeeRepository,
        EmployeeCreationDTO,
        EmployeeUpdateDTO,
        EmployeeInfoDTO,
        EmployeeService,
        Employee
        > {

    @Override
    @BeforeEach
    protected void init() {

        creationDTO = buildValidEmployeeCreationDTO();
        updateDTO = buildValidEmployeeUpdateDTO();
    }

    @Override
    protected void createDefaultEntity() {

        service.registerNewEmployee(creationDTO);
    }

    @Override
    protected void deleteDefaultEntity(Long id) {

        service.deleteEmployee(id);
    }

    @Override
    protected void updateEntity(Long id, EmployeeUpdateDTO dto) {

        service.updateEmployee(id, dto);
    }

    @Override
    protected EmployeeInfoDTO getInfoDTO(Long id) {

        return service.getEmployeeInfo(id);
    }

    @Nested
    @DisplayName("Conteos de Empleados")
    class EmployeeCount {

        @Test
        @DisplayName("Dado repositorio con empleados activos, al obtener conteo de empleados activos, deberá retornar cantidad correcta desde BD")
        void givenRepository_WhenGettingActiveEmployeesCount_ThenReturnsCorrectCountFromDb() {

            insertEmployeesForActiveCountTest();

            Long activeCount = service.getActiveEmployees();

            assertEquals(2L, activeCount);
        }

        @Test
        @DisplayName("Dado repositorio con empleados activos e inactivos, al obtener conteo total de empleados, deberá retornar cantidad correcta desde BD")
        void givenRepository_WhenGettingTotalEmployeesCount_ThenReturnsCorrectCountFromDb() {

            insertEmployeesForTotalCountTest();

            Long totalCount = service.getEmployeeCount();

            assertEquals(3L, totalCount);
        }
    }

    @Nested
    @DisplayName("Búsqueda en Vivo")
    class LiveSearch {

        @Test
        @DisplayName("Dado filtros de estado y rango de fecha, al buscar en vivo, deberá retornar resultados filtrados correctamente")
        @SuppressWarnings("Non")
        void givenFilters_WhenLiveSearchingWithStatusAndDateRange_ThenReturnsFilteredResults() {

            insertEmployeesForLiveSearchTest();

            List<EmployeeInfoDTO> results = service.liveSearch("Juan%", ACTIVO, ESTE_AÑO);

            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals("Juan", results.getFirst().getFirstName());
            assertEquals("Perez", results.getFirst().getLastName());
        }
    }

    @Nested
    @DisplayName("Cambio de Estado")
    class StateChange {

        @Test
        @DisplayName("Dado empleado activo existente, al cambiar su estado a inactivo, deberá persistir el cambio en BD")
        void givenExistingEmployee_WhenChangingIsActiveValueFromActiveToInactive_ThenPersistsChange() {

            insertActiveEmployeeForChangeTest();

            Employee activeEmployee = repository.findAll().getFirst();

            service.changeEmployeeIsActiveValue(activeEmployee.getEmployeeID());

            Employee updatedEmployee = verifyEmployeeIsInactive(activeEmployee.getEmployeeID());
            assertFalse(updatedEmployee.isActive());
        }

        @Test
        @DisplayName("Dado empleado inactivo existente, al cambiar su estado a activo, deberá persistir el cambio en BD")
        void givenExistingEmployee_WhenChangingIsActiveValueFromInactiveToActive_ThenPersistsChange() {

            insertInactiveEmployeeForChangeTest();

            Employee inactiveEmployee = getFirstEntityOnRepoList();

            if (!inactiveEmployee.getFirstName().equals("Ana")) {
                throw new AssertionError("El primer empleado no es el empleado inactivo 'Ana'");
            }

            service.changeEmployeeIsActiveValue(inactiveEmployee.getEmployeeID());

            Employee updatedEmployee = verifyEmployeeIsActive(inactiveEmployee.getEmployeeID());
            assertTrue(updatedEmployee.isActive());
        }
    }

    @Nested
    @DisplayName("Otros Métodos")
    class OtherMethods {

        @Test
        @DisplayName("Dado repositorio vacío, al obtener lista de nombres para combobox, deberá retornar solo la opción de filtro")
        void givenEmptyRepository_WhenGettingNames_ThenReturnsOnlyFilterOption() {

            repository.deleteAll();

            List<String> names = service.getNames();

            assertNotNull(names);
            assertEquals(1, names.size());
            assertEquals(EMPLOYEE_COMBOBOX_NO_FILTER, names.getFirst());
        }

        @Test
        @DisplayName("Dado repositorio con empleados sin citas, al obtener lista de empleados, deberá retornar counts de citas mensuales en cero")
        void givenRepository_WhenGettingEmployeeListWithNoAppointments_ThenReturnsZeroCounts() {

            Employee employee = buildValidEmployee();
            employee.setEmployeeID(null);
            employee.setFirstName("Carlos");
            employee.setLastName("Perez");
            employee.setActive(true);
            employee.setHireDate(LocalDate.of(2026, 1, 1));
            employee.setCommissionPercentage(0.50);
            repository.save(employee);

            List<MonthlyAppointmentCountDTO> monthlyCounts = repository.countMonthlyAppointmentsBatch(
                    List.of(employee.getEmployeeID()),
                    LocalDate.now().withDayOfMonth(1).atStartOfDay(),
                    LocalDate.now().withDayOfMonth(LocalDate.now().getDayOfMonth()).atTime(java.time.LocalTime.MAX)
            );

            assertNotNull(monthlyCounts);

            if (monthlyCounts.isEmpty()) {

                Long count = repository.getMonthlyAppointmentsByEmployee(
                        employee.getEmployeeID(),
                        LocalDate.now().withDayOfMonth(1).atStartOfDay(),
                        LocalDate.now().withDayOfMonth(LocalDate.now().getDayOfMonth()).atTime(java.time.LocalTime.MAX)
                );
                assertEquals(0L, count);
            } else {
                assertEquals(1, monthlyCounts.size());
                assertEquals(0, monthlyCounts.getFirst().getAppointmentCount());
            }
        }
    }

    private void insertEmployeesForActiveCountTest() {

        insertActiveEmployee("Carlos", "Martinez");
        insertActiveEmployee("Ana", "Lopez");
        insertInactiveEmployee();
    }

    private void insertEmployeesForTotalCountTest() {

        insertActiveEmployee("Carlos", "Martinez");
        insertActiveEmployee("Ana", "Lopez");
        insertInactiveEmployee();
    }

    private void insertEmployeesForLiveSearchTest() {

        Employee activeEmployee1 = buildValidEmployee();
        activeEmployee1.setEmployeeID(null);
        activeEmployee1.setFirstName("Juan");
        activeEmployee1.setLastName("Perez");
        activeEmployee1.setActive(true);
        activeEmployee1.setHireDate(LocalDate.of(2026, 1, 1));
        repository.save(activeEmployee1);

        Employee activeEmployee2 = buildValidEmployee();
        activeEmployee2.setEmployeeID(null);
        activeEmployee2.setFirstName("Maria");
        activeEmployee2.setLastName("Gomez");
        activeEmployee2.setActive(true);
        activeEmployee2.setHireDate(LocalDate.of(2026, 6, 1));
        repository.save(activeEmployee2);

        Employee inactiveEmployee = buildValidEmployee();
        inactiveEmployee.setEmployeeID(null);
        inactiveEmployee.setFirstName("Carlos");
        inactiveEmployee.setLastName("Silva");
        inactiveEmployee.setActive(false);
        inactiveEmployee.setHireDate(LocalDate.of(2025, 12, 1));
        repository.save(inactiveEmployee);
    }

    private void insertActiveEmployeeForChangeTest() {

        Employee activeEmployee = buildValidEmployee();
        activeEmployee.setEmployeeID(null);
        activeEmployee.setFirstName("Juan");
        activeEmployee.setLastName("Perez");
        activeEmployee.setActive(true);
        activeEmployee.setHireDate(LocalDate.of(2026, 1, 1));
        repository.save(activeEmployee);
    }

    private Employee verifyEmployeeIsInactive(Long employeeID) {

        return reloadEntity(employeeID);
    }

    private Employee verifyEmployeeIsActive(Long employeeID) {

        return reloadEntity(employeeID);
    }

    private void insertInactiveEmployeeForChangeTest() {

        Employee inactiveEmployee = buildValidEmployee();
        inactiveEmployee.setEmployeeID(null);
        inactiveEmployee.setFirstName("Ana");
        inactiveEmployee.setLastName("Lopez");
        inactiveEmployee.setActive(false);
        inactiveEmployee.setHireDate(LocalDate.of(2026, 1, 1));
        inactiveEmployee.setCommissionPercentage(0.50);
        inactiveEmployee.setTerminationDate(null);
        repository.save(inactiveEmployee);

        createDefaultEntity();
    }

    private void insertActiveEmployee(String firstName, String lastName) {

        Employee employee = buildValidEmployee();
        employee.setEmployeeID(null);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setActive(true);
        repository.save(employee);
    }

    private void insertInactiveEmployee() {

        Employee employee = buildValidEmployee();
        employee.setEmployeeID(null);
        employee.setFirstName("Pedro");
        employee.setLastName("Garcia");
        employee.setActive(false);
        repository.save(employee);
    }
}
