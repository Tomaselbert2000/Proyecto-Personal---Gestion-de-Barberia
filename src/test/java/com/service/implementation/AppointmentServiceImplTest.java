package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.enums.AppointmentStatus;
import com.exceptions.appointment.AppointmentNotFoundException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import com.exceptions.appointment.InvalidAppointmentUpdateException;
import com.exceptions.appointment.NullAppointmentInputDataException;
import com.exceptions.barberservice.BarberServiceNotFoundException;
import com.exceptions.client.ClientNotFoundException;
import com.exceptions.common.EmployeeNotAvailableException;
import com.exceptions.employee.EmployeeNotFoundException;
import com.exceptions.sale.InactiveEmployeeException;
import com.mapper.implementation.AppointmentMapperImpl;
import com.mapper.interfaces.AppointmentMapper;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;
import com.repository.AppointmentRepository;
import com.repository.BarberServiceRepository;
import com.repository.ClientRepository;
import com.repository.EmployeeRepository;
import com.validation.appointment.AppointmentValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.factory.AppointmentTestDataFactory.*;
import static com.factory.BarberServiceTestDataFactory.buildValidBarberService;
import static com.factory.ClientTestDataFactory.buildValidClient;
import static com.factory.EmployeeTestDataFactory.buildValidEmployee;
import static com.service.helper.AppointmentServiceTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceImplTest extends BaseServiceTest<Appointment, AppointmentRepository> {

    private static final Long NON_EXISTING_ID = 999L;
    private static final Long ANOTHER_EMPLOYEE_ID = 1000L;
    private static final Long ANOTHER_BARBER_SERVICE_ID = 555L;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BarberServiceRepository barberServiceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private AppointmentMapper mapper = new AppointmentMapperImpl();

    @Mock
    private AppointmentValidator validator;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Captor
    private ArgumentCaptor<Appointment> captor;

    private final Client client = buildValidClient();
    private final BarberService barberService = buildValidBarberService();
    private final BarberService anotherBarberService = buildValidBarberService();
    private final Employee employee = buildValidEmployee();
    private final Employee anotherEmployee = buildValidEmployee();
    private final Appointment appointment = buildValidAppointment();
    private final LocalDateTime overlapedStartDateTime = appointment.getStartDateTime().plusMinutes(10);
    private final LocalDateTime overlapedEndDateTime = appointment.getEndDateTime().plusMinutes(20);
    private final AppointmentCreationDTO creationDTO = buildValidAppointmentCreationDTO();
    private final AppointmentUpdateDTO updateDTO = buildValidAppointmentUpdateDTO();

    @Test
    @DisplayName("Dado un nuevo turno con información válida, debe ser persistido correctamente")
    void givenNewAppointment_WhenCreating_ThenIsPersisted() {

        mockBasicSuccessfulScenario(clientRepository, client, barberServiceRepository, barberService, employeeRepository, employee);

        registerAppointment(appointmentService, creationDTO);

        verifyRegisterProcessSuccess();
    }

    @Test
    @DisplayName("Dado cualquier valor NULL ingresado dentro del DTO de creación de turno, arrojará NullAppointmentInputDataException")
    void givenAnyNullValue_WhenCreating_ThenThrows_NullAppoiuntmentInputDataException() {

        doThrow(NullAppointmentInputDataException.class).when(validator).validateForCreation(any());

        assertThrows(NullAppointmentInputDataException.class, () -> registerAppointment(appointmentService, creationDTO));

        verifyRegisterProcessFailure();
    }

    @Test
    @DisplayName("Dado un cliente no registrado previamente en el sistema, arrojará ClientNotFoundException")
    void givenNonExistingClient_WhenCreating_ThenThrows_ClientNotFoundException() {

        mockEmptyClient(clientRepository, client);

        assertThrows(ClientNotFoundException.class, () -> registerAppointment(appointmentService, creationDTO));

        verifyRegisterProcessFailure();
    }

    @Test
    @DisplayName("Dado un servicio de barbería no registrado previamente en el sistema, arrojará BarberServiceNotFoundException")
    void givenNonExistingBarberService_WhenCreating_ThenThrows_BarberServiceNotFoundException() {

        mockClient(clientRepository, client);
        mockEmptyBarberService(barberServiceRepository, barberService);

        assertThrows(BarberServiceNotFoundException.class, () -> registerAppointment(appointmentService, creationDTO));

        verifyRegisterProcessFailure();
    }

    @Test
    @DisplayName("Dado un empleado no registrado previamente en el sistema, arrojará EmployeeNotFoundException")
    void givenNonExistingEmployee_WhenCreating_ThenThrows_EmployeeNotFoundException() {

        mockClient(clientRepository, client);
        mockEmptyEmployee(employeeRepository, employee);
        mockBarberService(barberServiceRepository, barberService);

        assertThrows(EmployeeNotFoundException.class, () -> registerAppointment(appointmentService, creationDTO));

        verifyRegisterProcessFailure();
    }

    @Test
    @DisplayName("Dado un turno ya registrado, deberá retornar su información mediante correspondiente mapeo por DTO informativo")
    void givenExistingAppointment_WhenSearching_ThenReturnsItsInfo() {

        mockAppointment(appointmentRepository, appointment);

        AppointmentInfoDTO returnedInfo = getAppointmentInfoDTO(appointmentService, appointment);

        verify(appointmentRepository).findById(appointment.getAppointmentID());
        verify(mapper).mapAppointmentToInfoDto(appointment);

        verifyInfoDTOAssertions(returnedInfo);
    }

    @Test
    @DisplayName("Dado un ID de turno inexistente al buscar un turno, arrojará AppointmentNotFoundException")
    void givenNonExistingAppointment_WhenSearching_ThenThrows_AppointmentNotFoundException() {

        mockEmptyAppointment(appointmentRepository, appointment);

        assertThrows(AppointmentNotFoundException.class, () -> getAppointmentInfoDTO(appointmentService, appointment));

        verify(mapper, never()).mapAppointmentToInfoDto(appointment);
    }

    @Test
    @DisplayName("Dado un turno existente en el sistema, deberá poder ser eliminado")
    void givenExistingAppointment_WhenDeleting_ThenIsErased() {

        mockAppointment(appointmentRepository, appointment);

        deleteAppointment(appointmentService, appointment);

        verifyThatEntityWasDeleted(appointment);
    }

    @Test
    @DisplayName("Dado un ID de turno inexistente, al intentar eliminarlo del sistema arrojará AppointmentNotFoundException")
    void givenNonExistingAppointment_WhenDeleting_ThenThrows_AppointmentNotFoundException() {

        mockEmptyAppointment(appointmentRepository, appointment);

        assertThrows(AppointmentNotFoundException.class, () -> deleteAppointment(appointmentService, appointment));

        verifyThatEntityWasNotDeleted(appointment);
    }

    @Test
    @DisplayName("Dados N turnos en el sistema, se retornarán en forma de lista, aplicando el mismo procedimiento de mapeo a DTO informativo mostrado antes")
    void given_N_Appointments_WhenGettingAll_ThenAListIsReturned() {

        List<Appointment> appointmentList = List.of(appointment);

        when(appointmentRepository.findAll()).thenReturn(appointmentList);

        List<AppointmentInfoDTO> returnedList = appointmentService.getAppointmentsList();
        AppointmentInfoDTO firstResult = returnedList.getFirst();

        verify(appointmentRepository).findAll();
        verify(mapper).mapAppointmentToInfoDto(appointmentList);

        verifyInfoDTOListAssertions(returnedList, firstResult);
    }

    @Test
    @DisplayName("Dado un turno existente y un objeto DTO de actualización con datos válidos, deberá persistir los cambios correctamente")
    void givenExistingAppointment_WhenUpdating_ThenIsPersisted() {

        mockAppointment(appointmentRepository, appointment);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyUpdateProcessSuccess();
    }

    @Test
    @DisplayName("Dado un ID de turno inexistente, al intentar actualizarlo arrojará AppointmentNotFoundException")
    void givenNonExistingAppointment_WhenUpdating_ThenThrows_AppointmentNotFoundException() {

        mockEmptyAppointment(appointmentRepository, appointment);

        assertThrows(AppointmentNotFoundException.class, () -> updateAppointment(appointmentService, appointment, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un ID de servicio de barbería no registrado en el sistema, e ingresado como valor en el DTO de actualización; deberá arrojar BarberServiceNotFoundException")
    void givenNonExistingNewBarberService_WhenUpdating_ThenThrows_BarberServiceNotFoundException() {

        mockAppointment(appointmentRepository, appointment);

        when(barberServiceRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        updateDTO.setNewBarberserviceID(NON_EXISTING_ID);

        assertThrows(BarberServiceNotFoundException.class, () -> updateAppointment(appointmentService, appointment, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un ID de empleado no registrado en el sistema, e ingresado como valor en el DTO de actualización; deberá arrojar EmployeeNotFoundException")
    void givenNonExistingNewEmployee_WhenUpdating_ThenThrows_EmployeeNotFoundException() {

        mockAppointment(appointmentRepository, appointment);

        when(employeeRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        updateDTO.setNewEmployeeID(NON_EXISTING_ID);

        assertThrows(EmployeeNotFoundException.class, () -> updateAppointment(appointmentService, appointment, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dada una fecha de inicio de turno inválida al actualizar un turno existente, arrojará InvalidAppointmentStartDateException")
    void givenAnyInvalidDateTimeValue_WhenUpdating_ThenValidationFails() {

        doThrow(new InvalidAppointmentStartDateException()).when(validator).validateForUpdate(updateDTO);

        assertThrows(InvalidAppointmentStartDateException.class, () -> updateAppointment(appointmentService, appointment, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un turno existente de estado CANCELADO, al intentar actualizarlo arrojará InvalidAppointmentUpdateException")
    void givenAppointmentStatus_CANCELADO_WhenUpdating_ThenThrows_InvalidAppointmentUpdateException() {

        appointment.setCurrentStatus(AppointmentStatus.CANCELADO);

        mockAppointment(appointmentRepository, appointment);

        assertThrows(InvalidAppointmentUpdateException.class, () -> updateAppointment(appointmentService, appointment, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un turno existente de estado FINALIZADO, al intentar actualizarlo arrojará InvalidAppointmentUpdateException")
    void givenAppointmentStatus_FINALIZADO_WhenUpdating_ThenThrows_InvalidAppointmentUpdateException() {

        appointment.setCurrentStatus(AppointmentStatus.FINALIZADO);

        mockAppointment(appointmentRepository, appointment);

        assertThrows(InvalidAppointmentUpdateException.class, () -> updateAppointment(appointmentService, appointment, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un turno existente de estado REPROGRAMADO, al intentar actualizarlo a estado PROGRAMADO arrojará InvalidAppointmentUpdateException")
    void givenAppointmentStatus_REPROGRAMADO_WhenUpdatingToStatus_PROGRAMADO_ThenThrows_InvalidAppointmentUpdateException() {

        appointment.setCurrentStatus(AppointmentStatus.REPROGRAMADO);
        updateDTO.setNewStatus(AppointmentStatus.PROGRAMADO);

        mockAppointment(appointmentRepository, appointment);

        assertThrows(InvalidAppointmentUpdateException.class, () -> updateAppointment(appointmentService, appointment, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un turno existente, si la fecha de inicio y/o fin es modificada, su estado pasa a ser REPROGRAMADO")
    void givenAppointmentStatus_PROGRAMADO_WhenUpdatingDateTimeValues_ThenNewStatusIs_REPROGRAMADO() {

        appointment.setCurrentStatus(AppointmentStatus.PROGRAMADO);

        mockAppointment(appointmentRepository, appointment);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyThatEntityWasCaptured(appointmentRepository, captor);

        Appointment capturedAppointment = captor.getValue();

        verifyUpdateProcessSuccess();

        assertEquals(AppointmentStatus.REPROGRAMADO, capturedAppointment.getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno existente de estado REPROGRAMADO, si la fecha de inicio y/o fin es nuevamente modificada, su estado se mantiene como REPROGRAMADO")
    void givenAppointmentStatus_REPROGRAMADO_WhenUpdatingDateTimeValues_ThenNewStatusIs_REPROGRAMADO() {

        appointment.setCurrentStatus(AppointmentStatus.REPROGRAMADO);

        mockAppointment(appointmentRepository, appointment);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyThatEntityWasCaptured(appointmentRepository, captor);

        Appointment capturedAppointment = captor.getValue();

        verifyUpdateProcessSuccess();

        assertEquals(AppointmentStatus.REPROGRAMADO, capturedAppointment.getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno de estado PROGRAMADO, si se lo actualiza como CANCELADO, el cambio se persiste correctamente")
    void givenAppointmentStatus_PROGRAMADO_WhenUpdatingStatusTo_CANCELADO_ThenIsSuccessfullyPersisted() {

        updateDTO.setNewStatus(AppointmentStatus.CANCELADO);

        mockAppointment(appointmentRepository, appointment);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyThatEntityWasCaptured(appointmentRepository, captor);

        verifyUpdateProcessSuccess();

        assertEquals(AppointmentStatus.CANCELADO, captor.getValue().getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno de estado PROGRAMADO, si se lo actualiza como FINALIZADO, el cambio se persiste correctamente")
    void givenAppointmentStatus_PROGRAMADO_WhenUpdatingStatusTo_FINALIZADO_ThenIsSuccessfullyPersisted() {

        updateDTO.setNewStatus(AppointmentStatus.FINALIZADO);

        mockAppointment(appointmentRepository, appointment);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyThatEntityWasCaptured(appointmentRepository, captor);

        verifyUpdateProcessSuccess();

        assertEquals(AppointmentStatus.FINALIZADO, captor.getValue().getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno de estado REPROGRAMADO, si se lo actualiza como CANCELADO, el cambio se persiste correctamente")
    void givenAppointmentStatus_REPROGRAMADO_WhenUpdatingStatusTo_CANCELADO_ThenIsSuccessfullyPersisted() {

        appointment.setCurrentStatus(AppointmentStatus.REPROGRAMADO);
        updateDTO.setNewStatus(AppointmentStatus.CANCELADO);

        mockAppointment(appointmentRepository, appointment);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyThatEntityWasCaptured(appointmentRepository, captor);

        verifyUpdateProcessSuccess();

        assertEquals(AppointmentStatus.CANCELADO, captor.getValue().getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno de estado REPROGRAMADO, si se lo actualiza como FINALIZADO el cambio se persiste correctamente")
    void givenAppointmentStatus_REPROGRAMADO_WhenUpdatingStatusTo_FINALIZADO_ThenIsSuccessfullyPersisted() {

        appointment.setCurrentStatus(AppointmentStatus.REPROGRAMADO);
        updateDTO.setNewStatus(AppointmentStatus.FINALIZADO);

        mockAppointment(appointmentRepository, appointment);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyThatEntityWasCaptured(appointmentRepository, captor);

        verifyUpdateProcessSuccess();

        assertEquals(AppointmentStatus.FINALIZADO, captor.getValue().getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno existente, al actualizar solamente el empleado asignado, los cambios se persisten correctamente")
    void givenExistingAppointment_WhenUpdatingOnlyEmployee_ThenIsSuccesufullyPersisted() {

        mockAppointment(appointmentRepository, appointment);
        mockEmployee(employeeRepository, anotherEmployee);

        updateDTO.setNewEmployeeID(ANOTHER_EMPLOYEE_ID);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyThatEntityWasCaptured(appointmentRepository, captor);

        verifyValidatorUpdateInteraction(validator);
        verifyMapperUpdateInteraction(mapper);

        Appointment capturedAppointment = captor.getValue();

        verifyUpdateAssertions(anotherEmployee, capturedAppointment, barberService);
    }

    @Test
    @DisplayName("Dado un turno existente, al actualizar solamente el servicio contratado, los cambios se persisten correctamtente")
    void givenExistingAppointment_WhenUpdatingOnlyBarberService_ThenIsSuccessfullyPersisted() {

        mockAppointment(appointmentRepository, appointment);
        mockBarberService(barberServiceRepository, anotherBarberService);

        updateDTO.setNewBarberserviceID(ANOTHER_BARBER_SERVICE_ID);

        updateAppointment(appointmentService, appointment, updateDTO);

        verifyThatEntityWasCaptured(appointmentRepository, captor);

        verifyValidatorUpdateInteraction(validator);
        verifyMapperUpdateInteraction(mapper);

        Appointment capturedAppointment = captor.getValue();

        verifyUpdateAssertions(employee, capturedAppointment, anotherBarberService);
    }

    @Test
    @DisplayName("Dado un empleado registrado en el sistema, actualmente inactivo, al registrar un turno arrojará InactiveEmployeeException")
    void givenInactiveEmployee_WhenCreating_ThenThrows_InactiveEmployeeException() {

        employee.setActive(false);

        mockBasicSuccessfulScenario(clientRepository, client, barberServiceRepository, barberService, employeeRepository, employee);

        creationDTO.setEmployeeID(employee.getEmployeeID());

        assertThrows(InactiveEmployeeException.class, () -> registerAppointment(appointmentService, creationDTO));

        verifyValidatorCreationInteraction(validator);
        verifyMapperUpdateNoInteractions(mapper);
        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Dado un turno existente con un empleado determinado, no es posible registrar un nuevo turno con ese empleado en horarios superpuestos")
    void givenEmployeeWithScheduledAppointment_WhenCreating_ThenThrows_EmployeeNotAvailableException() {

        mockBasicSuccessfulScenario(clientRepository, client, barberServiceRepository, barberService, employeeRepository, employee);
        mockThatNewAppointmentWillCauseOverlap(appointmentRepository);

        creationDTO.setEmployeeID(employee.getEmployeeID());
        creationDTO.setStartDateTime(overlapedStartDateTime);
        creationDTO.setEndDateTime(overlapedEndDateTime);

        assertThrows(EmployeeNotAvailableException.class, () -> registerAppointment(appointmentService, creationDTO));

        verifyRegisterProcessFailure();
    }

    @Test
    @DisplayName("Dado un turno existente con un empleado determinado, no es posible actualizar un 2do turno con ese nuevo empleado en horarios superpuestos")
    void givenEmployeeWithScheduledAppointment_WhenUpdating_ThenThrows_EmployeeNotAvailableException() {

        mockAppointment(appointmentRepository, appointment);
        mockThatAppointmentUpdateWillCauseOverlap(appointmentRepository);

        updateDTO.setNewEmployeeID(employee.getEmployeeID());
        updateDTO.setNewStartDateTime(overlapedStartDateTime);
        updateDTO.setNewEndDateTime(overlapedEndDateTime);

        assertThrows(EmployeeNotAvailableException.class, () -> updateAppointment(appointmentService, appointment, updateDTO));

        verifyUpdateProcessFailure();
    }

    private void syncDTOsIDs() {

        creationDTO.setClientID(client.getClientID());
        creationDTO.setBarberserviceID(barberService.getBarbershopServiceID());
        creationDTO.setEmployeeID(employee.getEmployeeID());

        updateDTO.setNewBarberserviceID(barberService.getBarbershopServiceID());
        updateDTO.setNewEmployeeID(employee.getEmployeeID());
    }

    private void setSecondaryEntityID() {

        anotherEmployee.setEmployeeID(ANOTHER_EMPLOYEE_ID);
        anotherBarberService.setBarbershopServiceID(ANOTHER_BARBER_SERVICE_ID);
    }

    private void setAppointmentWithFactoryData() {

        appointment.setClient(client);
        appointment.setBarberservice(barberService);
        appointment.setEmployee(employee);
    }

    private void verifyRegisterProcessSuccess() {
        verifyValidatorCreationInteraction(validator);
        verifyMapperCreationInteraction(mapper);
        verifyThatEntityWasSaved();
    }

    private void verifyRegisterProcessFailure() {

        verifyValidatorCreationInteraction(validator);
        verifyMapperCreationNoInteractions(mapper);
        verifyThatEntityWasNotSaved();
    }

    private void verifyUpdateProcessSuccess() {
        verifyValidatorUpdateInteraction(validator);
        verifyMapperUpdateInteraction(mapper);
        verifyThatAppointmentWasUpdated(mapper);
    }

    private void verifyUpdateProcessFailure() {
        verifyValidatorUpdateInteraction(validator);
        verifyMapperUpdateNoInteractions(mapper);
        verifyThatAppointmentWasNotUpdated(mapper);
    }

    private void verifyInfoDTOAssertions(AppointmentInfoDTO returnedInfo) {

        assertNotNull(returnedInfo);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(client.getFirstName(), returnedInfo.getClientFirstName()),
                () -> assertEquals(client.getLastName(), returnedInfo.getClientLastName()),
                () -> assertEquals(employee.getFirstName(), returnedInfo.getEmployeeFirstName()),
                () -> assertEquals(employee.getLastName(), returnedInfo.getEmployeeLastName()),
                () -> assertEquals(barberService.getName(), returnedInfo.getServiceName()),
                () -> assertEquals(appointment.getStartDateTime(), returnedInfo.getStartDateTime()),
                () -> assertEquals(appointment.getEndDateTime(), returnedInfo.getEndDateTime())
        );
    }

    private void verifyUpdateAssertions(Employee employee, Appointment capturedAppointment, BarberService anotherBarberService) {
        assertAll(
                "Verificacion de campos",
                () -> assertEquals(employee, capturedAppointment.getEmployee()),
                () -> assertEquals(anotherBarberService, capturedAppointment.getBarberservice()),
                () -> assertEquals(appointment.getClient(), capturedAppointment.getClient()),
                () -> assertEquals(appointment.getStartDateTime(), capturedAppointment.getStartDateTime()),
                () -> assertEquals(appointment.getEndDateTime(), capturedAppointment.getEndDateTime())
        );
    }

    private void verifyInfoDTOListAssertions(List<AppointmentInfoDTO> returnedList, AppointmentInfoDTO firstResult) {
        assertAll("Verificación de campos",
                () -> assertNotNull(returnedList),
                () -> assertEquals(1, returnedList.size()),
                () -> assertEquals(appointment.getStartDateTime(), firstResult.getStartDateTime()),
                () -> assertEquals(appointment.getBarberservice().getName(), firstResult.getServiceName())
        );
    }

    @Override
    protected void init() {

        syncDTOsIDs();
        setAppointmentWithFactoryData();
        setSecondaryEntityID();
    }

    @Override
    protected AppointmentRepository getPrimaryRepository() {

        return appointmentRepository;
    }
}
