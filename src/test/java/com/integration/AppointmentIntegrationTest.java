package com.integration;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.enums.AppointmentStatus;
import com.exceptions.appointment.AppointmentNotFoundException;
import com.exceptions.appointment.InvalidAppointmentUpdateException;
import com.exceptions.barberservice.BarberServiceNotFoundException;
import com.exceptions.client.ClientNotFoundException;
import com.exceptions.common.EmployeeNotAvailableException;
import com.exceptions.employee.EmployeeNotFoundException;
import com.factory.BarberServiceTestDataFactory;
import com.factory.ClientTestDataFactory;
import com.factory.EmployeeTestDataFactory;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;
import com.repository.AppointmentRepository;
import com.repository.BarberServiceRepository;
import com.repository.ClientRepository;
import com.repository.EmployeeRepository;
import com.service.interfaces.AppointmentService;
import javafx.application.HostServices;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.factory.AppointmentTestDataFactory.buildAppointmentCreationDTO;
import static com.factory.AppointmentTestDataFactory.buildAppointmentUpdateDTO;
import static com.integration.IntegrationTestConstants.AppointmentInfoAssertions.*;
import static com.integration.IntegrationTestConstants.AppointmentIntegrationTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AppointmentIntegrationTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BarberServiceRepository barberServiceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AppointmentService service;

    @MockitoBean
    private HostServices hostServices;

    Client mockClient1;
    Client mockClient2;

    Client savedClient1;
    Client savedClient2;

    Employee mockEmployee1;
    Employee mockEmployee2;

    Employee savedEmployee1;
    Employee savedEmployee2;

    BarberService mockBarberService1;
    BarberService mockBarberService2;

    BarberService savedBarberService1;
    BarberService savedBarberService2;

    AppointmentCreationDTO creationDTO;
    AppointmentCreationDTO creationDTO2;
    AppointmentCreationDTO overlapedAppointment;

    @BeforeEach
    public void init() {

        mockClient1 = ClientTestDataFactory.buildValidClient();
        mockEmployee1 = EmployeeTestDataFactory.buildValidEmployee();
        mockBarberService1 = BarberServiceTestDataFactory.buildValidBarberService();

        mockClient2 = ClientTestDataFactory.buildAnotherValidClient();
        mockEmployee2 = EmployeeTestDataFactory.buildAnotherValidEmployee();
        mockBarberService2 = BarberServiceTestDataFactory.buildAnotherValidBarberService();

        setAllEntityIDsAsNull(mockClient1, mockEmployee1, mockBarberService1);
        setAllEntityIDsAsNull(mockClient2, mockEmployee2, mockBarberService2);

        savedClient1 = clientRepository.save(mockClient1);
        savedEmployee1 = employeeRepository.save(mockEmployee1);
        savedBarberService1 = barberServiceRepository.save(mockBarberService1);

        savedClient2 = clientRepository.save(mockClient2);
        savedEmployee2 = employeeRepository.save(mockEmployee2);
        savedBarberService2 = barberServiceRepository.save(mockBarberService2);

        creationDTO = buildAppointmentCreationDTO(
                savedClient1.getClientID(),
                savedEmployee1.getEmployeeID(),
                savedBarberService1.getBarbershopServiceID(),
                startDateTime,
                endDateTime,
                "Importante"
        );

        creationDTO2 = buildAppointmentCreationDTO(
                savedClient2.getClientID(),
                savedEmployee2.getEmployeeID(),
                savedBarberService2.getBarbershopServiceID(),
                startDateTime2,
                endDateTime2,
                "Nota turno 2"
        );

        overlapedAppointment = buildAppointmentCreationDTO(
                savedClient1.getClientID(),
                savedEmployee1.getEmployeeID(),
                savedBarberService1.getBarbershopServiceID(),
                startDateTime,
                endDateTime,
                "Segundo turno solapado"
        );
    }

    @Test
    @DisplayName("Dado un turno con datos válidos, deberá persistirse correctamente en la base de datos")
    void givenAppointmentWithValidDataThenIsPersisted() {

        service.registerNewAppointment(creationDTO);
        assertEquals(1L, appointmentRepository.findAll().size());
    }

    @Test
    @DisplayName("No debería crear una cita si el empleado ya tiene una asignada en el mismo horario")
    void shouldFailToCreateAppointment_WhenEmployeeIsNotAvailable() {

        service.registerNewAppointment(creationDTO);

        assertThrows(EmployeeNotAvailableException.class, () -> service.registerNewAppointment(overlapedAppointment));
        assertEquals(1L, appointmentRepository.findAll().size());
    }

    @Test
    @DisplayName("No debería crear una cita con un cliente inexistente")
    void shouldFailToCreateAppointment_WhenClientDoesNotExist() {

        creationDTO.setClientID(-1L);
        assertThrows(ClientNotFoundException.class, () -> service.registerNewAppointment(creationDTO));
    }

    @Test
    @DisplayName("No debería crear una cita agendando un servicio inexistente")
    void shouldNotCreateAppointment_WhenBarberServiceDoesNotExist() {

        creationDTO.setBarberserviceID(-1L);
        assertThrows(BarberServiceNotFoundException.class, () -> service.registerNewAppointment(creationDTO));
    }

    @Test
    @DisplayName("No debería crear una cita agendando un empleado inexistente")
    void shouldNotCreateAppointment_WhenEmployeeDoesNotExist() {

        creationDTO.setEmployeeID(-1L);
        assertThrows(EmployeeNotFoundException.class, () -> service.registerNewAppointment(creationDTO));
    }

    @Test
    @DisplayName("Dado un turno existente, deberá poder ser eliminado correctamente")
    void shouldDeleteAnExistingAppointment() {

        service.registerNewAppointment(creationDTO);

        long appointmentCountBefore = appointmentRepository.findAll().size();

        service.deleteAppointment(getFirstAppointmentOnList().getAppointmentID());

        Long appointmentCountAfter = (long) appointmentRepository.findAll().size();

        assertEquals(appointmentCountBefore - 1L, appointmentCountAfter);
    }

    @Test
    @DisplayName("No debería eliminar un turno inexistente")
    void shouldFailToDeleteNonExistentAppointment() {

        assertThrows(AppointmentNotFoundException.class, () -> service.deleteAppointment(-1L));
    }

    @Test
    @DisplayName("Dado un turno registrado, deberá obtener su información completa a través del DTO")
    void shouldRetrieveAppointmentInfo() {

        service.registerNewAppointment(creationDTO);

        AppointmentInfoDTO infoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());

        assertDtoAttributes(infoDTO, getFirstAppointmentOnList(), savedClient1, savedEmployee1, savedBarberService1);
    }

    @Test
    @DisplayName("Dado N turnos registrados, su información deberá poder ser obtenida de forma exitosa desde el Service a través de una lista de DTOs")
    void givenAppointmentsRegisteredThenTheirInfoIsRetrievedAsList() {

        service.registerNewAppointment(creationDTO);
        service.registerNewAppointment(creationDTO2);

        List<AppointmentInfoDTO> appointmentsList = service.getAppointmentsList();

        assertEquals(2L, appointmentsList.size(), "Deben obtenerse exactamente 2 turnos");

        AppointmentInfoDTO firstDTO = appointmentsList.getFirst();
        Appointment firstEntity = getFirstAppointmentOnList();
        assertDtoAttributes(firstDTO, firstEntity, savedClient1, savedEmployee1, savedBarberService1);

        AppointmentInfoDTO secondDTO = appointmentsList.get(1);
        Appointment secondEntity = appointmentRepository.findAll().get(1);
        assertDtoAttributes(secondDTO, secondEntity, savedClient2, savedEmployee2, savedBarberService2);
    }

    @Test
    @DisplayName("Debería poder acotar la búsqueda de turnos usando filtrado dinámico")
    void shouldFilterAppointmentLiveSearch() {

        service.registerNewAppointment(creationDTO);
        service.registerNewAppointment(creationDTO2);

        List<AppointmentInfoDTO> allAppointments = service.getAppointmentsList();
        assertEquals(2L, allAppointments.size(), "Deben existir exactamente 2 turnos registrados");

        // Test 1: Búsqueda con filtro por nombre de cliente
        List<AppointmentInfoDTO> filteredByClient = service.liveSearch(savedClient1.getFirstName(), null, null, null);
        assertEquals(1L, filteredByClient.size(), "Debería filtrarse por nombre de cliente");
        assertEquals(savedClient1.getFirstName(), filteredByClient.getFirst().getClientFirstName());

        // Test 2: Búsqueda con filtro por estado
        List<AppointmentInfoDTO> filteredByStatus = service.liveSearch(null, null, AppointmentStatus.PROGRAMADO, null);
        assertEquals(2L, filteredByStatus.size(), "Debería filtrarse por estado PROGRAMADO");

        // Test 3: Búsqueda con filtro por nombre de empleado
        List<AppointmentInfoDTO> filteredByEmployee = service.liveSearch(null, null, null, savedEmployee1.getFirstName());
        assertEquals(1L, filteredByEmployee.size(), "Debería filtrarse por nombre de empleado");
        assertEquals(savedEmployee1.getFirstName(), filteredByEmployee.getFirst().getEmployeeFirstName());

        // Test 4: Búsqueda con múltiples filtros (cliente + estado)
        List<AppointmentInfoDTO> filteredByClientAndStatus = service.liveSearch(savedClient1.getFirstName(), null, AppointmentStatus.PROGRAMADO, null);
        assertEquals(1L, filteredByClientAndStatus.size(), "Debería filtrarse por cliente y estado");

        // Test 5: Búsqueda con rango de tiempo (fecha de creación de los turnos)
        LocalDate searchDate = now.toLocalDate().plusDays(1);
        List<AppointmentInfoDTO> filteredByDate = service.liveSearch(null, searchDate, null, null);
        assertEquals(2L, filteredByDate.size(), "Debería filtrarse por fecha");

        // Test 6: Búsqueda sin filtros (debería devolver todos)
        List<AppointmentInfoDTO> allAppointmentsViaLiveSearch = service.liveSearch(null, null, null, null);
        assertEquals(2L, allAppointmentsViaLiveSearch.size(), "Sin filtros debe devolver todos los turnos");
    }

    @Test
    @DisplayName("Dado un turno registrado, deberá poder marcarse como completado exitosamente")
    void shouldMarkAnAppointmentAsCompleted() {

        service.registerNewAppointment(creationDTO);

        AppointmentInfoDTO appointmentInfoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());

        assertEquals(AppointmentStatus.PROGRAMADO, appointmentInfoDTO.getCurrentStatus(), "El turno debe estar en estado PROGRAMADO tras la creación");

        service.markAppointmentAsComplete(appointmentInfoDTO);

        AppointmentInfoDTO updatedAppointmentInfoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());

        assertEquals(AppointmentStatus.FINALIZADO, updatedAppointmentInfoDTO.getCurrentStatus(), "El turno debe estar en estado FINALIZADO después de marcarlo como completado");
    }

    @Test
    @DisplayName("Dado un turno registrado, deberá poder marcarse como cancelado exitosamente")
    void shouldMarkAnAppointmentAsCanceled() {

        service.registerNewAppointment(creationDTO);

        AppointmentInfoDTO appointmentInfoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());

        assertEquals(AppointmentStatus.PROGRAMADO, appointmentInfoDTO.getCurrentStatus(), "El turno debe estar en estado PROGRAMADO tras la creación");

        service.markAppointmentAsCanceled(appointmentInfoDTO);

        AppointmentInfoDTO updatedAppointmentInfoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());

        assertEquals(AppointmentStatus.CANCELADO, updatedAppointmentInfoDTO.getCurrentStatus(), "El turno debe estar en estado CANCELADO después de marcarlo como cancelado");
    }

    @Test
    @DisplayName("Dado un turno con fecha de inicio modificada, su estado debe cambiarse a REPROGRAMADO")
    void shouldMarkAnAppointmentAsREPROGRAMADO_WhenUpdatingStartDatetime() {

        service.registerNewAppointment(creationDTO);

        AppointmentInfoDTO appointmentInfoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());

        assertEquals(AppointmentStatus.PROGRAMADO, appointmentInfoDTO.getCurrentStatus(), "El turno debe estar en estado PROGRAMADO tras la creación");

        AppointmentUpdateDTO updateDTO = buildAppointmentUpdateDTO(
                startDateTime.plusMinutes(30),
                endDateTime.plusMinutes(30),
                null,
                null,
                null,
                null
        );

        service.updateAppointment(getFirstAppointmentOnList().getAppointmentID(), updateDTO);

        AppointmentInfoDTO updatedAppointmentInfoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());

        assertEquals(AppointmentStatus.REPROGRAMADO, updatedAppointmentInfoDTO.getCurrentStatus(), "El turno debe estar en estado REPROGRAMADO después de modificar su fecha de inicio");
    }

    @Test
    @DisplayName("Dado un turno en estado REPROGRAMADO, no debería poder cambiarse a PROGRAMADO")
    void shouldNotMarkAnAppointmentAsPROGRAMADO_WhenCurrentStatusIsREPROGRAMADO() {

        service.registerNewAppointment(creationDTO);

        AppointmentInfoDTO initialInfoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());

        assertEquals(AppointmentStatus.PROGRAMADO, initialInfoDTO.getCurrentStatus(), "El turno debe estar en estado PROGRAMADO inicialmente");

        AppointmentUpdateDTO reprogramDTO = buildAppointmentUpdateDTO(
                startDateTime.plusMinutes(30),
                endDateTime.plusMinutes(30),
                null,
                null,
                null,
                null
        );

        service.updateAppointment(getFirstAppointmentOnList().getAppointmentID(), reprogramDTO);

        AppointmentInfoDTO reprogrammedInfoDTO = service.getAppointmentInfo(getFirstAppointmentOnList().getAppointmentID());
        assertEquals(AppointmentStatus.REPROGRAMADO, reprogrammedInfoDTO.getCurrentStatus(), "El turno debe estar en estado REPROGRAMADO después de modificar su horario");

        AppointmentUpdateDTO updateToProgramadoDTO = buildAppointmentUpdateDTO(
                startDateTime,
                endDateTime,
                AppointmentStatus.PROGRAMADO,
                null,
                null,
                null
        );

        assertThrows(InvalidAppointmentUpdateException.class,
                () -> service.updateAppointment(getFirstAppointmentOnList().getAppointmentID(), updateToProgramadoDTO),
                "El sistema no debería permitir cambiar un turno de estado REPROGRAMADO a PROGRAMADO"
        );
    }

    @Test
    @DisplayName("Dado un turno existente, deberá poder actualizarse exitosamente todos sus campos permitidos")
    void shouldUpdateSuccessfullyAnExistingAppointment() {

        service.registerNewAppointment(creationDTO);
        Long appointmentID = getFirstAppointmentOnList().getAppointmentID();

        AppointmentUpdateDTO updateDTO = buildAppointmentUpdateDTO(
                startDateTime.plusHours(1),
                endDateTime.plusHours(1),
                AppointmentStatus.REPROGRAMADO,
                "Nota actualizada - Cambio de horario",
                savedEmployee2.getEmployeeID(),
                savedBarberService2.getBarbershopServiceID()
        );

        service.updateAppointment(appointmentID, updateDTO);

        AppointmentInfoDTO updatedInfoDTO = service.getAppointmentInfo(appointmentID);

        assertAll(
                () -> assertEquals(appointmentID, updatedInfoDTO.getId(), MESSAGE_ID_MATCH),
                () -> assertEquals(savedEmployee2.getEmployeeID(), updatedInfoDTO.getEmployeeID(), MESSAGE_EMPLOYEE_ID_MATCH),
                () -> assertEquals(savedBarberService2.getBarbershopServiceID(), updatedInfoDTO.getBarberServiceID(), MESSAGE_SERVICE_ID_MATCH),
                () -> assertEquals(startDateTime.plusHours(1), updatedInfoDTO.getStartDateTime(), MESSAGE_START_DATETIME_MATCH),
                () -> assertEquals(endDateTime.plusHours(1), updatedInfoDTO.getEndDateTime(), MESSAGE_END_DATETIME_MATCH),
                () -> assertEquals(AppointmentStatus.REPROGRAMADO, updatedInfoDTO.getCurrentStatus(), MESSAGE_CURRENT_STATUS_MATCH),
                () -> assertEquals("Nota actualizada - Cambio de horario", updatedInfoDTO.getOptionalNotes(), MESSAGE_OPTIONAL_NOTES_MATCH)
        );

        Appointment updatedEntity = appointmentRepository.findById(appointmentID).orElseThrow();
        assertNotNull(updatedEntity.getModifiedDate(), "La fecha de modificación no debe ser null");
    }

    @Test
    @DisplayName("Dado un turno existente y otro turno del mismo empleado, no debería poder actualizarse la fecha de inicio si esto solapara con el otro turno")
    void shouldNotUpdateAnExistingAppointment_WhenNewTimeSlotsOverlapWithAnotherAppointment() {

        service.registerNewAppointment(creationDTO);

        AppointmentCreationDTO sameEmployeeSecondAppointment = buildAppointmentCreationDTO(
                savedClient2.getClientID(),
                savedEmployee1.getEmployeeID(),
                savedBarberService1.getBarbershopServiceID(),
                startDateTime.plusHours(2),
                endDateTime.plusHours(2),
                "Segundo turno mismo empleado"
        );

        service.registerNewAppointment(sameEmployeeSecondAppointment);

        Long firstAppointmentID = getFirstAppointmentOnList().getAppointmentID();

        AppointmentInfoDTO firstAppointmentInfoDTO = service.getAppointmentInfo(firstAppointmentID);

        AppointmentUpdateDTO overlappingUpdateDTO = buildAppointmentUpdateDTO(
                sameEmployeeSecondAppointment.getStartDateTime(),
                sameEmployeeSecondAppointment.getEndDateTime(),
                null,
                null,
                firstAppointmentInfoDTO.getEmployeeID(),
                firstAppointmentInfoDTO.getBarberServiceID()
        );

        assertThrows(EmployeeNotAvailableException.class,
                () -> service.updateAppointment(firstAppointmentID, overlappingUpdateDTO),
                "El sistema no debería permitir actualizar el turno a un horario que solape con otro turno del mismo empleado"
        );
    }

    @Test
    @DisplayName("Dado un turno existente, no debería poder actualizarse con un servicio de barbería inexistente")
    void shouldNotUpdateAnExistingAppointment_WithNonExistingBarberService() {

        service.registerNewAppointment(creationDTO);

        Long appointmentID = getFirstAppointmentOnList().getAppointmentID();

        AppointmentUpdateDTO updateDTO = buildAppointmentUpdateDTO(
                startDateTime,
                endDateTime,
                null,
                null,
                savedEmployee1.getEmployeeID(),
                -1L
        );

        assertThrows(BarberServiceNotFoundException.class,
                () -> service.updateAppointment(appointmentID, updateDTO),
                "El sistema no debería permitir actualizar el turno con un servicio de barbería inexistente"
        );
    }

    private void setAllEntityIDsAsNull(Client mockClient, Employee mockEmployee, BarberService mockBarberService) {

        mockClient.setClientID(null);
        mockEmployee.setEmployeeID(null);
        mockBarberService.setBarbershopServiceID(null);
    }

    private void assertDtoAttributes(
            AppointmentInfoDTO infoDTO,
            Appointment appointment,
            Client savedClient,
            Employee savedEmployee,
            BarberService savedBarberService
    ) {

        assertAll(
                () -> assertEquals(appointment.getAppointmentID(), infoDTO.getId(), MESSAGE_ID_MATCH),
                () -> assertEquals(savedEmployee.getEmployeeID(), infoDTO.getEmployeeID(), MESSAGE_EMPLOYEE_ID_MATCH),
                () -> assertEquals(savedBarberService.getBarbershopServiceID(), infoDTO.getBarberServiceID(), MESSAGE_SERVICE_ID_MATCH),
                () -> assertEquals(savedClient.getFirstName(), infoDTO.getClientFirstName(), MESSAGE_CLIENT_FIRST_NAME_MATCH),
                () -> assertEquals(savedClient.getLastName(), infoDTO.getClientLastName(), MESSAGE_CLIENT_LAST_NAME_MATCH),
                () -> assertEquals(savedBarberService.getName(), infoDTO.getServiceName(), MESSAGE_SERVICE_NAME_MATCH),
                () -> assertEquals(savedBarberService.getPrice(), infoDTO.getServicePrice(), MESSAGE_SERVICE_PRICE_MATCH),
                () -> assertEquals(savedEmployee.getFirstName(), infoDTO.getEmployeeFirstName(), MESSAGE_EMPLOYEE_FIRST_NAME_MATCH),
                () -> assertEquals(savedEmployee.getLastName(), infoDTO.getEmployeeLastName(), MESSAGE_EMPLOYEE_LAST_NAME_MATCH),
                () -> assertEquals(appointment.getRegistrationTimestamp(), infoDTO.getRegistrationTimestamp(), MESSAGE_REGISTRATION_TIMESTAMP_MATCH),
                () -> assertEquals(appointment.getStartDateTime(), infoDTO.getStartDateTime(), MESSAGE_START_DATETIME_MATCH),
                () -> assertEquals(appointment.getEndDateTime(), infoDTO.getEndDateTime(), MESSAGE_END_DATETIME_MATCH),
                () -> assertEquals(appointment.getCurrentStatus(), infoDTO.getCurrentStatus(), MESSAGE_CURRENT_STATUS_MATCH),
                () -> assertEquals(appointment.getOptionalNotes(), infoDTO.getOptionalNotes(), MESSAGE_OPTIONAL_NOTES_MATCH));
    }

    private @NonNull Appointment getFirstAppointmentOnList() {

        return appointmentRepository.findAll().getFirst();
    }
}
