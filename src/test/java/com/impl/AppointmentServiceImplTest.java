package com.impl;

import com.barbershop.dto.appointment.AppointmentCreationDTO;
import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.dto.appointment.AppointmentUpdateDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.exceptions.appointment.AppointmentNotFoundException;
import com.barbershop.exceptions.appointment.InvalidAppointmentStartDateException;
import com.barbershop.exceptions.appointment.InvalidAppointmentUpdateException;
import com.barbershop.exceptions.appointment.NullAppointmentInputDataException;
import com.barbershop.exceptions.barberservice.BarberServiceNotFoundException;
import com.barbershop.exceptions.client.ClientNotFoundException;
import com.barbershop.exceptions.common.EmployeeNotAvailableException;
import com.barbershop.exceptions.employee.EmployeeNotFoundException;
import com.barbershop.exceptions.sale.InactiveEmployeeException;
import com.barbershop.mapper.implementation.AppointmentMapperImpl;
import com.barbershop.mapper.interfaces.AppointmentMapper;
import com.barbershop.model.Appointment;
import com.barbershop.model.BarberService;
import com.barbershop.model.Client;
import com.barbershop.model.Employee;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.repository.BarberServiceRepository;
import com.barbershop.repository.ClientRepository;
import com.barbershop.repository.EmployeeRepository;
import com.barbershop.service.implementation.AppointmentServiceImpl;
import com.barbershop.validation.appointment.AppointmentValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

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
    private ArgumentCaptor<Appointment> appointmentCaptor;

    private Client client;
    private BarberService firstBarberService;
    private BarberService secondBarberService;
    private Employee firstEmployee;
    private Employee secondEmployee;
    private Appointment appointmentOnDB;

    private AppointmentCreationDTO creationDTO;
    private AppointmentUpdateDTO updateDTO;
    private AppointmentInfoDTO infoDTO;

    private static final Long CLIENT_ID = 1L;
    private static final Long FIRST_EMPLOYEE_ID = 10L;
    private static final Long SECOND_EMPLOYEE_ID = 15L;
    private static final Long FIRST_BARBER_SERVICE_ID = 100L;
    private static final Long SECOND_BARBER_SERVICE_ID = 200L;
    private static final Long APPOINTMENT_ID = 123L;
    private static final LocalDateTime FIXED_APPOINTMENT_START_DATETIME = LocalDateTime.of(2026, 1, 1, 18, 0);
    private static final LocalDateTime FIXED_APPOINTMENT_END_DATETIME = LocalDateTime.of(2026, 1, 1, 18, 30);
    private static final Long NON_EXISTING_SERVICE_ID = 999L;
    private static final Long NON_EXISTING_EMPLOYEE_ID = 250L;

    @BeforeEach
    void init() {

        setupClient();
        setupFirstBarberService();
        setupSecondBarberService();
        setupFirstEmployee();
        setupSecondEmployee();
        setupCreationDTO();
        setupAppointmentEntity();
        setupInfoDTO();
        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un nuevo turno con información válida, debe ser persistido correctamente")
    void givenNewAppointment_WhenCreating_ThenIsPersisted() {

        mockBasicSuccessfulScenario();

        registerAppointment();

        verifyValidatorCreationInteraction();
        verifyMapperCreationInteraction();
        verifyThatAppointmentWasRegistered();
    }

    @Test
    @DisplayName("Dado cualquier valor NULL ingresado dentro del DTO de creación de turno, arrojará NullAppointmentInputDataException")
    void givenAnyNullValue_WhenCreating_ThenThrows_NullAppoiuntmentInputDataException() {

        doThrow(NullAppointmentInputDataException.class).when(validator).validateForCreation(any());

        assertThrows(NullAppointmentInputDataException.class, this::registerAppointment);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatAppointmentWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un cliente no registrado previamente en el sistema, arrojará ClientNotFoundException")
    void givenNonExistingClient_WhenCreating_ThenThrows_ClientNotFoundException() {

        mockEmptyClient();

        assertThrows(ClientNotFoundException.class, this::registerAppointment);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatAppointmentWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un servicio de barbería no registrado previamente en el sistema, arrojará BarberServiceNotFoundException")
    void givenNonExistingBarberService_WhenCreating_ThenThrows_BarberServiceNotFoundException() {

        mockClient();
        mockEmptyFirstBarberService();

        assertThrows(BarberServiceNotFoundException.class, this::registerAppointment);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatAppointmentWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un empleado no registrado previamente en el sistema, arrojará EmployeeNotFoundException")
    void givenNonExistingEmployee_WhenCreating_ThenThrows_EmployeeNotFoundException() {

        mockClient();
        mockEmptyFirstEmployee();
        mockFirstBarberService();

        assertThrows(EmployeeNotFoundException.class, this::registerAppointment);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatAppointmentWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un turno ya registrado, deberá retornar su información mediante correspondiente mapeo por DTO informativo")
    void givenExistingAppointment_WhenSearching_ThenReturnsItsInfo() {

        mockAppointment();
        mockInfoDTO();

        AppointmentInfoDTO returnedInfo = getAppointmentInfoDTO();

        verify(appointmentRepository).findById(APPOINTMENT_ID);
        verify(mapper).mapAppointmentToInfoDto(appointmentOnDB);

        assertNotNull(returnedInfo);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(client.getFirstName(), returnedInfo.getClientFirstName()),
                () -> assertEquals(client.getLastName(), returnedInfo.getClientLastName()),
                () -> assertEquals(firstEmployee.getFirstName(), returnedInfo.getEmployeeFirstName()),
                () -> assertEquals(firstEmployee.getLastName(), returnedInfo.getEmployeeLastName()),
                () -> assertEquals(firstBarberService.getName(), returnedInfo.getServiceName()),
                () -> assertEquals(appointmentOnDB.getStartDateTime(), returnedInfo.getStartDateTime()),
                () -> assertEquals(appointmentOnDB.getEndDateTime(), returnedInfo.getEndDateTime())
        );
    }

    @Test
    @DisplayName("Dado un ID de turno inexistente al buscar un turno, arrojará AppointmentNotFoundException")
    void givenNonExistingAppointment_WhenSearching_ThenThrows_AppointmentNotFoundException() {

        mockEmptyAppointment();

        assertThrows(AppointmentNotFoundException.class, this::getAppointmentInfoDTO);

        verify(mapper, never()).mapAppointmentToInfoDto(appointmentOnDB);
    }

    @Test
    @DisplayName("Dado un turno existente en el sistema, deberá poder ser eliminado")
    void givenExistingAppointment_WhenDeleting_ThenIsErased() {

        mockAppointment();

        deleteAppointment();

        verifyThatAppointmentWasDeleted();
    }

    @Test
    @DisplayName("Dado un ID de turno inexistente, al intentar eliminarlo del sistema arrojará AppointmentNotFoundException")
    void givenNonExistingAppointment_WhenDeleting_ThenThrows_AppointmentNotFoundException() {

        mockEmptyAppointment();

        assertThrows(AppointmentNotFoundException.class, this::deleteAppointment);

        verifyThatAppointmentWasNotDeleted();
    }

    @Test
    @DisplayName("Dados N turnos en el sistema, se retornarán en forma de lista, aplicando el mismo procedimiento de mapeo a DTO informativo mostrado antes")
    void given_N_Appointments_WhenGettingAll_ThenAListIsReturned() {

        List<Appointment> appointmentList = List.of(appointmentOnDB);

        when(appointmentRepository.findAll()).thenReturn(appointmentList);

        List<AppointmentInfoDTO> returnedList = appointmentService.getAppointmentsList();
        AppointmentInfoDTO firstResult = returnedList.getFirst();

        verify(appointmentRepository).findAll();
        verify(mapper).mapAppointmentToInfoDto(appointmentList);

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(returnedList),
                () -> assertEquals(1, returnedList.size()),
                () -> assertEquals(appointmentOnDB.getStartDateTime(), firstResult.getStartDateTime()),
                () -> assertEquals(appointmentOnDB.getBarberservice().getName(), firstResult.getServiceName())
        );
    }

    @Test
    @DisplayName("Dado un turno existente y un objeto DTO de actualización con datos válidos, deberá persistir los cambios correctamente")
    void givenExistingAppointment_WhenUpdating_ThenIsPersisted() {

        mockAppointment();

        updateAppointment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatAppointmentWasUpdated();
    }

    @Test
    @DisplayName("Dado un ID de turno inexistente, al intentar actualizarlo arrojará AppointmentNotFoundException")
    void givenNonExistingAppointment_WhenUpdating_ThenThrows_AppointmentNotFoundException() {

        mockEmptyAppointment();

        assertThrows(AppointmentNotFoundException.class, this::updateAppointment);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un ID de servicio de barbería no registrado en el sistema, e ingresado como valor en el DTO de actualización; deberá arrojar BarberServiceNotFoundException")
    void givenNonExistingNewBarberService_WhenUpdating_ThenThrows_BarberServiceNotFoundException() {

        mockAppointment();
        when(barberServiceRepository.findById(NON_EXISTING_SERVICE_ID)).thenReturn(Optional.empty());

        updateDTO.setNewBarberserviceID(NON_EXISTING_SERVICE_ID);

        assertThrows(BarberServiceNotFoundException.class, this::updateAppointment);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un ID de empleado no registrado en el sistema, e ingresado como valor en el DTO de actualización; deberá arrojar EmployeeNotFoundException")
    void givenNonExistingNewEmployee_WhenUpdating_ThenThrows_EmployeeNotFoundException() {

        mockAppointment();
        when(employeeRepository.findById(NON_EXISTING_EMPLOYEE_ID)).thenReturn(Optional.empty());

        updateDTO.setNewEmployeeID(NON_EXISTING_EMPLOYEE_ID);

        assertThrows(EmployeeNotFoundException.class, this::updateAppointment);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotUpdated();
    }

    @Test
    @DisplayName("Dada una fecha de inicio de turno inválida al actualizar un turno existente, arrojará InvalidAppointmentStartDateException")
    void givenAnyInvalidDateTimeValue_WhenUpdating_ThenValidationFails() {

        doThrow(new InvalidAppointmentStartDateException()).when(validator).validateForUpdate(updateDTO);

        assertThrows(InvalidAppointmentStartDateException.class, this::updateAppointment);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un turno existente de estado CANCELADO, al intentar actualizarlo arrojará InvalidAppointmentUpdateException")
    void givenAppointmentStatus_CANCELADO_WhenUpdating_ThenThrows_InvalidAppointmentUpdateException() {

        appointmentOnDB.setCurrentStatus(AppointmentStatus.CANCELADO);

        mockAppointment();

        assertThrows(InvalidAppointmentUpdateException.class, this::updateAppointment);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un turno existente de estado FINALIZADO, al intentar actualizarlo arrojará InvalidAppointmentUpdateException")
    void givenAppointmentStatus_FINALIZADO_WhenUpdating_ThenThrows_InvalidAppointmentUpdateException() {

        appointmentOnDB.setCurrentStatus(AppointmentStatus.FINALIZADO);

        mockAppointment();

        assertThrows(InvalidAppointmentUpdateException.class, this::updateAppointment);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un turno existente de estado REPROGRAMADO, al intentar actualizarlo a estado PROGRAMADO arrojará InvalidAppointmentUpdateException")
    void givenAppointmentStatus_REPROGRAMADO_WhenUpdatingToStatus_PROGRAMADO_ThenThrows_InvalidAppointmentUpdateException() {

        appointmentOnDB.setCurrentStatus(AppointmentStatus.REPROGRAMADO);
        updateDTO.setNewStatus(AppointmentStatus.PROGRAMADO);

        mockAppointment();

        assertThrows(InvalidAppointmentUpdateException.class, this::updateAppointment);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un turno existente, si la fecha de inicio y/o fin es modificada, su estado pasa a ser REPROGRAMADO")
    void givenAppointmentStatus_PROGRAMADO_WhenUpdatingDateTimeValues_ThenNewStatusIs_REPROGRAMADO() {

        appointmentOnDB.setCurrentStatus(AppointmentStatus.PROGRAMADO);

        mockAppointment();

        updateAppointment();

        captureAppointment();

        Appointment capturedAppointment = appointmentCaptor.getValue();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatAppointmentWasUpdated();

        assertEquals(AppointmentStatus.REPROGRAMADO, capturedAppointment.getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno existente de estado REPROGRAMADO, si la fecha de inicio y/o fin es nuevamente modificada, su estado se mantiene como REPROGRAMADO")
    void givenAppointmentStatus_REPROGRAMADO_WhenUpdatingDateTimeValues_ThenNewStatusIs_REPROGRAMADO() {

        appointmentOnDB.setCurrentStatus(AppointmentStatus.REPROGRAMADO);

        mockAppointment();

        updateAppointment();

        captureAppointment();

        Appointment capturedAppointment = appointmentCaptor.getValue();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatAppointmentWasUpdated();

        assertEquals(AppointmentStatus.REPROGRAMADO, capturedAppointment.getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno de estado PROGRAMADO, si se lo actualiza como CANCELADO, el cambio se persiste correctamente")
    void givenAppointmentStatus_PROGRAMADO_WhenUpdatingStatusTo_CANCELADO_ThenIsSuccessfullyPersisted() {

        updateDTO.setNewStatus(AppointmentStatus.CANCELADO);

        mockAppointment();

        updateAppointment();

        captureAppointment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatAppointmentWasUpdated();

        assertEquals(AppointmentStatus.CANCELADO, appointmentCaptor.getValue().getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno de estado PROGRAMADO, si se lo actualiza como FINALIZADO, el cambio se persiste correctamente")
    void givenAppointmentStatus_PROGRAMADO_WhenUpdatingStatusTo_FINALIZADO_ThenIsSuccessfullyPersisted() {

        updateDTO.setNewStatus(AppointmentStatus.FINALIZADO);

        mockAppointment();

        updateAppointment();

        captureAppointment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatAppointmentWasUpdated();

        assertEquals(AppointmentStatus.FINALIZADO, appointmentCaptor.getValue().getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno de estado REPROGRAMADO, si se lo actualiza como CANCELADO, el cambio se persiste correctamente")
    void givenAppointmentStatus_REPROGRAMADO_WhenUpdatingStatusTo_CANCELADO_ThenIsSuccessfullyPersisted() {

        appointmentOnDB.setCurrentStatus(AppointmentStatus.REPROGRAMADO);
        updateDTO.setNewStatus(AppointmentStatus.CANCELADO);

        mockAppointment();

        updateAppointment();

        captureAppointment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatAppointmentWasUpdated();

        assertEquals(AppointmentStatus.CANCELADO, appointmentCaptor.getValue().getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno de estado REPROGRAMADO, si se lo actualiza como FINALIZADO el cambio se persiste correctamente")
    void givenAppointmentStatus_REPROGRAMADO_WhenUpdatingStatusTo_FINALIZADO_ThenIsSuccessfullyPersisted() {

        appointmentOnDB.setCurrentStatus(AppointmentStatus.REPROGRAMADO);
        updateDTO.setNewStatus(AppointmentStatus.FINALIZADO);

        mockAppointment();

        updateAppointment();

        captureAppointment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatAppointmentWasUpdated();

        assertEquals(AppointmentStatus.FINALIZADO, appointmentCaptor.getValue().getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un turno existente, al actualizar solamente el empleado asignado, los cambios se persisten correctamente")
    void givenExistingAppointment_WhenUpdatingOnlyEmployee_ThenIsSuccesufullyPersisted() {

        mockSecondEmployee();
        mockAppointment();

        updateDTO.setNewEmployeeID(SECOND_EMPLOYEE_ID);

        updateAppointment();

        captureAppointment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();


        Appointment capturedAppointment = appointmentCaptor.getValue();

        assertAll(
                "Verificacion de campos",
                () -> assertEquals(secondEmployee, capturedAppointment.getEmployee()),
                () -> assertEquals(firstBarberService, capturedAppointment.getBarberservice()),
                () -> assertEquals(appointmentOnDB.getClient(), capturedAppointment.getClient()),
                () -> assertEquals(appointmentOnDB.getStartDateTime(), capturedAppointment.getStartDateTime()),
                () -> assertEquals(appointmentOnDB.getEndDateTime(), capturedAppointment.getEndDateTime())
        );
    }

    @Test
    @DisplayName("Dado un turno existente, al actualizar solamente el servicio contratado, los cambios se persisten correctamtente")
    void givenExistingAppointment_WhenUpdatingOnlyBarberService_ThenIsSuccessfullyPersisted() {

        mockSecondBarberService();
        mockAppointment();

        updateDTO.setNewBarberserviceID(SECOND_BARBER_SERVICE_ID);

        updateAppointment();

        captureAppointment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();

        Appointment capturedAppointment = appointmentCaptor.getValue();

        assertAll(
                "Verificacion de campos",
                () -> assertEquals(firstEmployee, capturedAppointment.getEmployee()),
                () -> assertEquals(secondBarberService, capturedAppointment.getBarberservice()),
                () -> assertEquals(appointmentOnDB.getClient(), capturedAppointment.getClient()),
                () -> assertEquals(appointmentOnDB.getStartDateTime(), capturedAppointment.getStartDateTime()),
                () -> assertEquals(appointmentOnDB.getEndDateTime(), capturedAppointment.getEndDateTime())
        );
    }

    @Test
    @DisplayName("Dado un empleado registrado en el sistema, actualmente inactivo, al registrar un turno arrojará InactiveEmployeeException")
    void givenInactiveEmployee_WhenCreating_ThenThrows_InactiveEmployeeException() {

        firstEmployee.setActive(false);

        mockBasicSuccessfulScenario();

        creationDTO.setEmployeeID(FIRST_EMPLOYEE_ID);

        assertThrows(InactiveEmployeeException.class, this::registerAppointment);

        verifyValidatorCreationInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un turno existente con un empleado determinado, no es posible registrar un nuevo turno con ese empleado en horarios superpuestos")
    void givenEmployeeWithScheduledAppointment_WhenCreating_ThenThrows_EmployeeNotAvailableException() {

        mockBasicSuccessfulScenario();
        mockThatNewAppointmentWillCauseOverlap();

        LocalDateTime overlapedStartDateTime = FIXED_APPOINTMENT_START_DATETIME.plusMinutes(10);
        LocalDateTime overlapedEndDateTime = FIXED_APPOINTMENT_END_DATETIME.plusMinutes(20);

        creationDTO.setEmployeeID(FIRST_EMPLOYEE_ID);
        creationDTO.setStartDateTime(overlapedStartDateTime);
        creationDTO.setEndDateTime(overlapedEndDateTime);

        assertThrows(EmployeeNotAvailableException.class, this::registerAppointment);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatAppointmentWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un turno existente con un empleado determinado, no es posible actualizar un 2do turno con ese nuevo empleado en horarios superpuestos")
    void givenEmployeeWithScheduledAppointment_WhenUpdating_ThenThrows_EmployeeNotAvailableException() {

        mockAppointment();
        mockThatAppointmentUpdateWillCauseOverlap();

        LocalDateTime overlapedStartDateTime = FIXED_APPOINTMENT_START_DATETIME.plusMinutes(10);
        LocalDateTime overlapedEndDateTime = FIXED_APPOINTMENT_END_DATETIME.plusMinutes(20);

        updateDTO.setNewEmployeeID(FIRST_EMPLOYEE_ID);
        updateDTO.setNewStartDateTime(overlapedStartDateTime);
        updateDTO.setNewEndDateTime(overlapedEndDateTime);

        assertThrows(EmployeeNotAvailableException.class, this::updateAppointment);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatAppointmentWasNotUpdated();
    }

    private void setupClient() {

        client = Client.builder().clientID(CLIENT_ID).build();
    }

    private void setupFirstEmployee() {

        firstEmployee = Employee.builder().employeeID(FIRST_EMPLOYEE_ID).isActive(true).build();
    }

    private void setupSecondEmployee() {

        secondEmployee = Employee.builder().employeeID(SECOND_EMPLOYEE_ID).isActive(true).build();
    }

    private void setupFirstBarberService() {

        firstBarberService = BarberService.builder().barbershopServiceID(FIRST_BARBER_SERVICE_ID).build();
    }

    private void setupSecondBarberService() {

        secondBarberService = BarberService.builder().barbershopServiceID(SECOND_BARBER_SERVICE_ID).build();
    }

    private void setupAppointmentEntity() {

        appointmentOnDB = Appointment.builder()
                .appointmentID(APPOINTMENT_ID)
                .client(client)
                .barberservice(firstBarberService)
                .employee(firstEmployee)
                .startDateTime(FIXED_APPOINTMENT_START_DATETIME)
                .endDateTime(FIXED_APPOINTMENT_END_DATETIME)
                .currentStatus(AppointmentStatus.PROGRAMADO)
                .build();
    }

    private void setupUpdateDTO() {

        updateDTO = AppointmentUpdateDTO.builder()
                .newEmployeeID(firstEmployee.getEmployeeID())
                .newBarberserviceID(firstBarberService.getBarbershopServiceID())
                .newStartDateTime(appointmentOnDB.getStartDateTime().plusDays(1))
                .newEndDateTime(appointmentOnDB.getEndDateTime().plusDays(1))
                .newStatus(AppointmentStatus.REPROGRAMADO)
                .build();
    }

    private void setupInfoDTO() {

        infoDTO = AppointmentInfoDTO.builder()
                .clientFirstName(appointmentOnDB.getClient().getFirstName())
                .clientLastName(appointmentOnDB.getClient().getLastName())
                .serviceName(appointmentOnDB.getBarberservice().getName())
                .employeeFirstName(appointmentOnDB.getEmployee().getFirstName())
                .employeeLastName(appointmentOnDB.getEmployee().getLastName())
                .startDateTime(appointmentOnDB.getStartDateTime())
                .endDateTime(appointmentOnDB.getEndDateTime())
                .currentStatus(appointmentOnDB.getCurrentStatus())
                .build();
    }

    private void setupCreationDTO() {

        creationDTO = new AppointmentCreationDTO();
        creationDTO.setClientID(client.getClientID());
        creationDTO.setBarberserviceID(firstBarberService.getBarbershopServiceID());
        creationDTO.setEmployeeID(firstEmployee.getEmployeeID());
        creationDTO.setStartDateTime(FIXED_APPOINTMENT_START_DATETIME);
        creationDTO.setEndDateTime(FIXED_APPOINTMENT_END_DATETIME);
    }

    private void mockBasicSuccessfulScenario() {

        mockClient();
        mockFirstBarberService();
        mockFirstEmployee();
    }

    private void mockClient() {

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
    }

    private void mockEmptyClient() {

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());
    }

    private void mockFirstBarberService() {

        when(barberServiceRepository.findById(FIRST_BARBER_SERVICE_ID)).thenReturn(Optional.of(firstBarberService));
    }

    private void mockSecondBarberService() {

        when(barberServiceRepository.findById(SECOND_BARBER_SERVICE_ID)).thenReturn(Optional.of(secondBarberService));
    }

    private void mockEmptyFirstBarberService() {

        when(barberServiceRepository.findById(FIRST_BARBER_SERVICE_ID)).thenReturn(Optional.empty());
    }

    private void mockFirstEmployee() {

        when(employeeRepository.findById(FIRST_EMPLOYEE_ID)).thenReturn(Optional.of(firstEmployee));
    }

    private void mockSecondEmployee() {

        when(employeeRepository.findById(SECOND_EMPLOYEE_ID)).thenReturn(Optional.of(secondEmployee));
    }

    private void mockEmptyFirstEmployee() {

        when(employeeRepository.findById(FIRST_EMPLOYEE_ID)).thenReturn(Optional.empty());
    }

    private void mockAppointment() {

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointmentOnDB));
    }

    private void mockEmptyAppointment() {

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.empty());
    }

    private void mockInfoDTO() {

        when(mapper.mapAppointmentToInfoDto(appointmentOnDB)).thenReturn(infoDTO);
    }

    private void mockThatNewAppointmentWillCauseOverlap() {

        when(appointmentRepository.existsOverlappingAppointmentOnCreate(anyLong(), any(), any())).thenReturn(true);
    }

    private void mockThatAppointmentUpdateWillCauseOverlap() {

        when(appointmentRepository.existsOverlappingAppointmentOnUpdate(anyLong(), any(), any(), anyLong())).thenReturn(true);
    }

    private void registerAppointment() {

        appointmentService.registerNewAppointment(creationDTO);
    }

    private AppointmentInfoDTO getAppointmentInfoDTO() {

        return appointmentService.getAppointmentInfo(APPOINTMENT_ID);
    }

    private void updateAppointment() {

        appointmentService.updateAppointment(APPOINTMENT_ID, updateDTO);
    }

    private void deleteAppointment() {

        appointmentService.deleteAppointment(APPOINTMENT_ID);
    }

    private void verifyValidatorCreationInteraction() {

        verify(validator).validateForCreation(any());
    }

    private void verifyValidatorUpdateInteraction() {

        verify(validator).validateForUpdate(any());
    }

    private void verifyMapperCreationInteraction() {

        verify(mapper).mapAppointmentCreationDtoToAppointmentEntity(any(), any(), any(), any());
    }

    private void verifyMapperCreationNoInteractions() {

        verify(mapper, never()).mapAppointmentCreationDtoToAppointmentEntity(any(), any(), any(), any());
    }

    private void verifyMapperUpdateInteraction() {

        verify(mapper).mapAppointmentUpdateDtoToAppointmentEntity(any(), any(), any(), any());
    }

    private void verifyMapperUpdateNoInteractions() {

        verify(mapper, never()).mapAppointmentUpdateDtoToAppointmentEntity(any(), any(), any(), any());
    }

    private void verifyThatAppointmentWasRegistered() {

        verify(appointmentRepository).save(any());
    }

    private void verifyThatAppointmentWasNotRegistered() {

        verify(appointmentRepository, never()).save(any());
    }

    private void verifyThatAppointmentWasDeleted() {

        verify(appointmentRepository).delete(appointmentOnDB);
    }

    private void verifyThatAppointmentWasNotDeleted() {

        verify(appointmentRepository, never()).delete(appointmentOnDB);
    }

    private void verifyThatAppointmentWasUpdated() {

        verify(mapper).mapAppointmentUpdateDtoToAppointmentEntity(any(), any(), any(), any());
    }

    private void verifyThatAppointmentWasNotUpdated() {

        verify(mapper, never()).mapAppointmentUpdateDtoToAppointmentEntity(any(), any(), any(), any());
    }

    private void captureAppointment() {

        verify(appointmentRepository).save(appointmentCaptor.capture());
    }
}
