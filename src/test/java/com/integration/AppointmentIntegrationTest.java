package com.integration;

import com.abstract_test_class.BaseIntegrationTest;
import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.stats.*;
import com.enums.AppointmentStatus;
import com.exceptions.appointment.AppointmentNotFoundException;
import com.exceptions.barberservice.BarberServiceNotFoundException;
import com.exceptions.client.ClientNotFoundException;
import com.exceptions.employee.EmployeeNotFoundException;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;
import com.repository.AppointmentRepository;
import com.repository.BarberServiceRepository;
import com.repository.ClientRepository;
import com.repository.EmployeeRepository;
import com.service.interfaces.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.factory.BarberServiceTestDataFactory.buildAnotherValidBarberService;
import static com.factory.BarberServiceTestDataFactory.buildValidBarberService;
import static com.factory.ClientTestDataFactory.buildValidClient;
import static com.factory.EmployeeTestDataFactory.buildAnotherValidEmployee;
import static com.factory.EmployeeTestDataFactory.buildValidEmployee;
import static com.utils.time.TimeCalculation.getCurrentDate;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentIntegrationTest extends BaseIntegrationTest<
        AppointmentRepository,
        AppointmentCreationDTO,
        AppointmentUpdateDTO,
        AppointmentInfoDTO,
        AppointmentService,
        Appointment> {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BarberServiceRepository barberServiceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final Client client = buildValidClient();
    private final Employee employee = buildValidEmployee();
    private final Employee employee2 = buildAnotherValidEmployee();
    private final BarberService barberService = buildValidBarberService();
    private final BarberService barberService2 = buildAnotherValidBarberService();

    @Override
    @BeforeEach
    protected void init() {

        preloadAll();

        LocalDateTime startTime = getCurrentDate().atTime(12, 30).plusDays(1);
        LocalDateTime endTime = startTime.plusMinutes(30);
        String notes = "Cliente frecuente";

        creationDTO = AppointmentCreationDTO.builder()
                .clientID(client.getClientID())
                .employeeID(employee.getEmployeeID())
                .barberserviceID(barberService.getBarbershopServiceID())
                .startDateTime(startTime)
                .endDateTime(endTime)
                .optionalNotes(notes)
                .build();

        updateDTO = AppointmentUpdateDTO.builder()
                .newEmployeeID(employee2.getEmployeeID())
                .newBarberserviceID(barberService2.getBarbershopServiceID())
                .newStartDateTime(startTime.plusDays(7))
                .newEndDateTime(endTime.plusDays(7))
                .optionalNotes("Sin notas adicionales")
                .build();
    }

    @Override
    protected void createDefaultEntity() {

        service.registerNewAppointment(creationDTO);
    }

    @Override
    protected void deleteDefaultEntity(Long id) {

        service.deleteAppointment(id);
    }

    @Override
    protected void updateEntity(Long id, AppointmentUpdateDTO dto) {

        service.updateAppointment(id, dto);
    }

    @Override
    protected Appointment getFirstEntityOnRepoList() {

        return repository.findAll().getFirst();
    }

    @Override
    protected Appointment reloadEntity(Long id) {

        return repository.findById(id).orElseThrow();
    }

    @Override
    protected AppointmentInfoDTO getInfoDTO(Long id) {

        return service.getAppointmentInfo(id);
    }

    @Override
    protected Integer countEntityOcurrencesOnRepo() {

        return repository.findAll().size();
    }

    @Test
    @DisplayName("Dado un turno con datos correctos, se persistirá correctamente en la DB")
    void givenCreationDTOWithValidDataThenIsPersisted() {

        createDefaultEntity();
        assertEquals(1, countEntityOcurrencesOnRepo());
    }

    @Test
    @DisplayName("Dado un turno con un cliente no registrado, no se persistirá en la DB")
    void givenNonExistingClientThenAppointmentIsNotPersisted() {

        clientRepository.deleteAll();

        assertThrows(ClientNotFoundException.class, this::createDefaultEntity);
    }

    @Test
    @DisplayName("Dado un turno con un empleado no registrado, no se persistirá en la DB")
    void givenNonExistingEmployeeThenAppointmentIsNotPersisted() {

        employeeRepository.deleteAll();

        assertThrows(EmployeeNotFoundException.class, this::createDefaultEntity);
    }

    @Test
    @DisplayName("Dado un turno con un servicio no registrado, no se persistirá en la DB")
    void givenNonExistingBarberServiceThenAppointmentIsNotPersisted() {

        barberServiceRepository.deleteAll();

        assertThrows(BarberServiceNotFoundException.class, this::createDefaultEntity);
    }

    @Test
    @DisplayName("Dado un turno existente, deberá poder eliminarse de la DB")
    void givenExistingAppointmentThenItShouldBeDeletedSuccessfully() {

        createDefaultEntity();

        Appointment appointment = getFirstEntityOnRepoList();

        deleteDefaultEntity(appointment.getAppointmentID());

        assertEntityTableIsEmpty();
    }

    @Test
    @DisplayName("Dado un turno inexistente, no modificará la DB")
    void givenNonExistingAppointmentWhenDeletingThenThrowsException() {

        assertThrows(AppointmentNotFoundException.class, () -> deleteDefaultEntity(1L));
    }

    @Test
    @DisplayName("Dado un turno existente, se podrá obtener su información")
    void givenExistingAppointmentThenShouldShowItsInformation() {

        createDefaultEntity();

        AppointmentInfoDTO infoDTO = getInfoDTO(getFirstEntityOnRepoList().getAppointmentID());

        assertAll(
                () -> assertEquals(infoDTO.getClientFirstName(), client.getFirstName()),
                () -> assertEquals(infoDTO.getClientLastName(), client.getLastName()),
                () -> assertEquals(infoDTO.getBarberServiceID(), barberService.getBarbershopServiceID()),
                () -> assertEquals(infoDTO.getEmployeeID(), employee.getEmployeeID()),
                () -> assertEquals(infoDTO.getStartDateTime(), getFirstEntityOnRepoList().getStartDateTime()),
                () -> assertEquals(infoDTO.getEndDateTime(), getFirstEntityOnRepoList().getEndDateTime())
        );
    }

    @Test
    @DisplayName("Dado un turno inexistente, no se obtiene información alguna")
    void givenNonExistingAppointmentWhenShowingInformationThenThrowsException() {

        assertThrows(AppointmentNotFoundException.class, () -> getInfoDTO(-1L));
    }

    @Test
    @DisplayName("Dado un turno existente, podrá ser actualizado")
    void givenExistingAppointmentThenShouldUpdateSuccessfully() {

        createDefaultEntity();

        Appointment appointment = getFirstEntityOnRepoList();

        updateEntity(appointment.getAppointmentID(), updateDTO);

        infoDTO = service.getAppointmentInfo(appointment.getAppointmentID());

        assertEquals(barberService2.getBarbershopServiceID(), infoDTO.getBarberServiceID());
        assertEquals(employee2.getEmployeeID(), infoDTO.getEmployeeID());
    }

    @Test
    @DisplayName("Dado N turnos registrados, al obtener todos se retorna una lista mediante mapeo por DTO informativo")
    void given_N_ExistingAppointments_WhenGettingAll_ThenReturnsAListOfDTO() {

        createDefaultEntity();

        Integer expectedListSize = 1;

        List<AppointmentInfoDTO> appointments = service.getAppointmentsList();

        assertEquals(expectedListSize, appointments.size());
    }

    @Test
    @DisplayName("Dado una búsqueda en vivo con filtros, se devuelven resultados correctos")
    void givenLiveSearchWithFilters_WhenSearching_ThenReturnsCorrectDTOs() {

        createDefaultEntity();

        List<AppointmentInfoDTO> results = service.liveSearch(null, null, null, null);

        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(1, results.size()),
                () -> assertEquals(employee.getEmployeeID(), results.getFirst().getEmployeeID()),
                () -> assertEquals(barberService.getBarbershopServiceID(), results.getFirst().getBarberServiceID())
        );
    }

    @Test
    @DisplayName("Dado un turno existente, se marca como completado correctamente")
    void givenExistingAppointment_WhenMarkingAsComplete_ThenIsMarkedSuccessfully() {

        createDefaultEntity();

        Appointment appointment = getFirstEntityOnRepoList();

        infoDTO = service.getAppointmentInfo(appointment.getAppointmentID());

        service.markAppointmentAsComplete(infoDTO);

        assertEquals(AppointmentStatus.FINALIZADO, service.getAppointmentInfo(appointment.getAppointmentID()).getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno existente, se marca como cancelado correctamente")
    void givenExistingAppointment_WhenMarkingAsCanceled_ThenIsMarkedSuccessfully() {

        createDefaultEntity();

        Appointment appointment = getFirstEntityOnRepoList();

        infoDTO = service.getAppointmentInfo(appointment.getAppointmentID());

        service.markAppointmentAsCanceled(infoDTO);

        assertEquals(AppointmentStatus.CANCELADO, service.getAppointmentInfo(appointment.getAppointmentID()).getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un servicio de barbería, se obtiene la lista de empleados")
    void givenService_WhenGettingEmployees_ThenReturnsEmployeeList() {

        List<EmployeeInfoDTO> employees = service.getEmployeesFromServiceInstance();

        assertAll(
                () -> assertNotNull(employees),
                () -> assertFalse(employees.isEmpty()),
                () -> assertEquals(2, employees.size())
        );
    }

    @Test
    @DisplayName("Dado un servicio de barbería, se obtiene la lista de servicios")
    void givenService_WhenGettingBarberServices_ThenReturnsBarberServiceList() {

        List<BarberServiceInfoDTO> catalog = service.getBarberServicesFromServiceInstance();

        assertAll(
                () -> assertNotNull(catalog),
                () -> assertFalse(catalog.isEmpty()),
                () -> assertEquals(2, catalog.size())
        );
    }

    @Test
    @DisplayName("Dado que hay turnos hoy, se devuelven estadísticas correctas")
    void givenTodayStats_WhenFetching_ThenReturnsCorrectDTO() {

        AppointmentTodayStatsDTO stats = service.getAppointmentsTodayStats();

        assertAll(
                "Verificación de estructura DTO",
                () -> assertNotNull(stats),
                () -> assertTrue(stats.getAppointmentCount() >= 0),
                () -> assertTrue(stats.getTotalAmountAsFinished() >= 0)
        );
    }

    @Test
    @DisplayName("Dado que hay turnos pendientes, se devuelven estadísticas correctas")
    void givenPendingStats_WhenFetching_ThenReturnsCorrectDTO() {

        AppointmentTomorrowStatsDTO stats = service.getPendingAppointmentsStats();

        assertAll(
                "Verificación de estructura DTO",
                () -> assertNotNull(stats),
                () -> assertTrue(stats.getTotalPendingAppointments() >= 0),
                () -> assertTrue(stats.getScheduledAppointmentsTomorrow() >= 0)
        );
    }

    @Test
    @DisplayName("Dado que hay turnos en el mes, se devuelven estadísticas de comparación mensual correctas")
    void givenMonthlyStats_WhenFetching_ThenReturnsCorrectDTO() {

        AppointmentMonthlyComparisonDTO stats = service.getMonthlyComparisonStats();

        assertAll(
                "Verificación de estructura DTO",
                () -> assertNotNull(stats),
                () -> assertTrue(stats.getCurrentMonthAppointments() >= 0),
                () -> assertTrue(stats.getPreviousMonthAppointments() >= 0)
        );
    }

    @Test
    @DisplayName("Dado que hay turnos cancelados, se devuelven estadísticas de cancelaciones correctas")
    void givenCanceledStats_WhenFetching_ThenReturnsCorrectDTO() {

        AppointmentCanceledStatsDTO stats = service.getCanceledStats();

        assertAll(
                "Verificación de estructura DTO",
                () -> assertNotNull(stats),
                () -> assertTrue(stats.getCanceledAppointmentThisMonth() >= 0),
                () -> assertTrue(stats.getTotalAppointmentsThisMonth() >= 0),
                () -> assertNotNull(stats.getCanceledAppointmentPercentage())
        );
    }

    @Test
    @DisplayName("Dado que hay turnos hoy, se devuelven estadísticas de ingreso esperado correctas")
    void givenExpectedIncomeStats_WhenFetching_ThenReturnsCorrectDTO() {

        ExpectedIncomeStatDTO stats = service.getExpectedIncomeToday();

        assertAll(
                "Verificación de estructura DTO",
                () -> assertNotNull(stats),
                () -> assertTrue(stats.getAppointmentsToday() >= 0),
                () -> assertTrue(stats.getExpectedIncomeSumForToday() >= 0),
                () -> assertTrue(stats.getAverageTicket() >= 0.0)
        );
    }

    @Test
    @DisplayName("Dado que hay turnos registrados, se retorna el conteo correcto")
    void givenExistingAppointments_WhenCounting_ThenReturnsCorrectCount() {

        createDefaultEntity();

        Integer expectedCount = 1;
        Integer currentCount = countEntityOcurrencesOnRepo();

        assertEquals(expectedCount, currentCount);
    }

    @Test
    @DisplayName("Dado una fecha futura y un empleado, se obtienen slots disponibles")
    void givenFutureDate_WhenGettingAvailableSlots_ThenReturnsList() {

        createDefaultEntity();

        LocalDateTime futureDateAndTime = getCurrentDate().atTime(12, 30).plusDays(2);

        List<LocalTime> availableSlots = service.getAvailableSlots(futureDateAndTime.toLocalDate(), employee.getEmployeeID());

        assertNotNull(availableSlots);
        assertFalse(availableSlots.isEmpty());
    }

    @Test
    @DisplayName("Dado un nombre de cliente, se realiza búsqueda en vivo y retorna lista de clientes")
    void givenClientSearchName_WhenSearching_ThenReturnsClientList() {

        List<ClientInfoDTO> clients = service.clientLiveSearchByName(client.getFirstName());

        assertAll(
                "Verificación de búsqueda",
                () -> assertNotNull(clients),
                () -> assertFalse(clients.isEmpty()),
                () -> assertEquals(1, clients.size())
        );
    }

    private void preloadClient() {

        client.setClientID(null);
        clientRepository.save(client);
    }

    private void preloadEmployee() {

        employee.setEmployeeID(null);
        employeeRepository.save(employee);

        employee2.setEmployeeID(null);
        employeeRepository.save(employee2);
    }

    private void preloadBarberService() {

        barberService.setBarbershopServiceID(null);
        barberServiceRepository.save(barberService);

        barberService2.setBarbershopServiceID(null);
        barberServiceRepository.save(barberService2);
    }

    private void preloadAll() {

        preloadClient();
        preloadEmployee();
        preloadBarberService();
    }
}
