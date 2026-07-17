package com.service.helper;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.mapper.interfaces.AppointmentMapper;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;
import com.repository.AppointmentRepository;
import com.repository.BarberServiceRepository;
import com.repository.ClientRepository;
import com.repository.EmployeeRepository;
import com.service.interfaces.AppointmentService;
import com.validation.appointment.AppointmentValidator;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AppointmentServiceTestHelper {

    public static void mockBasicSuccessfulScenario(
            ClientRepository clientRepository,
            Client client,
            BarberServiceRepository barberServiceRepository,
            BarberService barberService,
            EmployeeRepository employeeRepository,
            Employee employee
    ) {

        mockClient(clientRepository, client);
        mockBarberService(barberServiceRepository, barberService);
        mockEmployee(employeeRepository, employee);
    }

    public static void mockClient(ClientRepository clientRepository, Client client) {

        when(clientRepository.findById(client.getClientID())).thenReturn(Optional.of(client));
    }

    public static void mockEmptyClient(ClientRepository clientRepository, Client client) {

        when(clientRepository.findById(client.getClientID())).thenReturn(Optional.empty());
    }

    public static void mockBarberService(BarberServiceRepository barberServiceRepository, BarberService barberService) {

        when(barberServiceRepository.findById(barberService.getBarbershopServiceID())).thenReturn(Optional.of(barberService));
    }

    public static void mockEmptyBarberService(BarberServiceRepository barberServiceRepository, BarberService barberService) {

        when(barberServiceRepository.findById(barberService.getBarbershopServiceID())).thenReturn(Optional.empty());
    }

    public static void mockEmployee(EmployeeRepository employeeRepository, Employee employee) {

        when(employeeRepository.findById(employee.getEmployeeID())).thenReturn(Optional.of(employee));
    }

    public static void mockEmptyEmployee(EmployeeRepository employeeRepository, Employee employee) {

        when(employeeRepository.findById(employee.getEmployeeID())).thenReturn(Optional.empty());
    }

    public static void mockAppointment(AppointmentRepository appointmentRepository, Appointment appointment) {

        when(appointmentRepository.findById(appointment.getAppointmentID())).thenReturn(Optional.of(appointment));
    }

    public static void mockEmptyAppointment(AppointmentRepository appointmentRepository, Appointment appointment) {

        when(appointmentRepository.findById(appointment.getAppointmentID())).thenReturn(Optional.empty());
    }

    public static void mockThatNewAppointmentWillCauseOverlap(AppointmentRepository appointmentRepository) {

        when(appointmentRepository.existsOverlappingAppointmentOnCreate(anyLong(), any(), any())).thenReturn(true);
    }

    public static void mockThatAppointmentUpdateWillCauseOverlap(AppointmentRepository appointmentRepository) {

        when(appointmentRepository.existsOverlappingAppointmentOnUpdate(anyLong(), any(), any(), anyLong())).thenReturn(true);
    }

    public static void registerAppointment(AppointmentService appointmentService, AppointmentCreationDTO creationDTO) {

        appointmentService.registerNewAppointment(creationDTO);
    }

    public static void deleteAppointment(AppointmentService appointmentService, Appointment appointment) {

        appointmentService.deleteAppointment(appointment.getAppointmentID());
    }

    public static void updateAppointment(AppointmentService appointmentService, Appointment appointment, AppointmentUpdateDTO updateDTO) {

        appointmentService.updateAppointment(appointment.getAppointmentID(), updateDTO);
    }

    public static AppointmentInfoDTO getAppointmentInfoDTO(AppointmentService appointmentService, Appointment appointment) {

        return appointmentService.getAppointmentInfo(appointment.getAppointmentID());
    }

    public static void verifyValidatorCreationInteraction(AppointmentValidator validator) {

        verify(validator).validateForCreation(any());
    }

    public static void verifyValidatorUpdateInteraction(AppointmentValidator validator) {

        verify(validator).validateForUpdate(any());
    }

    public static void verifyMapperCreationInteraction(AppointmentMapper mapper) {

        verify(mapper).mapAppointmentCreationDtoToAppointmentEntity(any(), any(), any(), any());
    }

    public static void verifyMapperCreationNoInteractions(AppointmentMapper mapper) {

        verify(mapper, never()).mapAppointmentCreationDtoToAppointmentEntity(any(), any(), any(), any());
    }

    public static void verifyMapperUpdateInteraction(AppointmentMapper mapper) {

        verify(mapper).mapAppointmentUpdateDtoToAppointmentEntity(any(), any(), any(), any());
    }

    public static void verifyMapperUpdateNoInteractions(AppointmentMapper mapper) {

        verify(mapper, never()).mapAppointmentUpdateDtoToAppointmentEntity(any(), any(), any(), any());
    }

    public static void verifyThatAppointmentWasUpdated(AppointmentMapper mapper) {

        verify(mapper).mapAppointmentUpdateDtoToAppointmentEntity(any(), any(), any(), any());
    }

    public static void verifyThatAppointmentWasNotUpdated(AppointmentMapper mapper) {

        verify(mapper, never()).mapAppointmentUpdateDtoToAppointmentEntity(any(), any(), any(), any());
    }
}
