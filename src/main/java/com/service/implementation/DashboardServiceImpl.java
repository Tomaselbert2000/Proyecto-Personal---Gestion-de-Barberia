package com.service.implementation;

import com.dto.activity.RecentActivityDTO;
import com.enums.AppointmentStatus;
import com.enums.EventType;
import com.model.Appointment;
import com.model.Client;
import com.model.Employee;
import com.model.Product;
import com.repository.AppointmentRepository;
import com.repository.ClientRepository;
import com.repository.EmployeeRepository;
import com.repository.ProductRepository;
import com.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.presentation.constants.ControllerConstants.DashboardControllerConstants.*;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<RecentActivityDTO> getRecentActivityLog() {

        List<Client> latestFiveRegisteredClients = clientRepository.findTop5ByOrderByRegistrationDateDesc();
        List<Employee> latestFiveHiredEmployees = employeeRepository.findTop5ByOrderByHireDateDesc();
        List<Product> latestFiveRegisteredProducts = productRepository.findTop5ByOrderByCreationDateDesc();
        List<Appointment> latestFiveRegistratedAppointments = appointmentRepository.findTop5ByOrderByRegistrationTimestampDesc();
        List<Employee> latestFiveTerminatedEmployees = employeeRepository.findTop5ByTerminationDateIsNotNullOrderByTerminationDateDesc();
        List<Appointment> latesFivetCompletedAppointments = appointmentRepository.findTop5ByCurrentStatusOrderByRegistrationTimestampDesc(AppointmentStatus.FINALIZADO);
        List<Appointment> latestFiveCanceledAppointments = appointmentRepository.findTop5ByCurrentStatusOrderByRegistrationTimestampDesc(AppointmentStatus.CANCELADO);

        return Stream.of(
                        latestFiveRegisteredClients.stream().map(this::mapNewClientToRecentActivityDTO),
                        latestFiveHiredEmployees.stream().map(this::mapNewEmployeeToRecentActivityDTO),
                        latestFiveRegisteredProducts.stream().map(this::mapNewProductToRecentActivityDTO),
                        latestFiveRegistratedAppointments.stream().map(this::mapNewAppointmentToRecentActivityDTO),
                        latestFiveTerminatedEmployees.stream().map(this::mapTerminatedEmployeeToRecentActivityDTO),
                        latesFivetCompletedAppointments.stream().map(this::mapCompletedAppointmentToRecentActivityDTO),
                        latestFiveCanceledAppointments.stream().map(this::mapCanceledAppointmentToRecentActivityDTO)
                ).flatMap(recentActivityDTOStream -> recentActivityDTOStream)
                .sorted(Comparator.comparing(RecentActivityDTO::getTimestamp).reversed())
                .limit(TOP_RESULTS_VALUE)
                .toList();
    }

    private RecentActivityDTO mapNewClientToRecentActivityDTO(Client clientToMap) {

        if (clientToMap == null) return null;

        String textToAttach = String.join(" ", NEW_CLIENT_REGISTERED_STRING, clientToMap.getFirstName(), clientToMap.getLastName());
        EventType eventType = EventType.NUEVO_CLIENTE;
        LocalDateTime timestamp = clientToMap.getRegistrationDate().atStartOfDay();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapNewEmployeeToRecentActivityDTO(Employee employeeToMap) {

        if (employeeToMap == null) return null;

        String textToAttach = String.join(" ", NEW_EMPLOYEE_REGISTERED_STRING, employeeToMap.getFirstName(), employeeToMap.getLastName());
        EventType eventType = EventType.NUEVO_EMPLEADO;
        LocalDateTime timestamp = employeeToMap.getHireDate().atStartOfDay();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapNewProductToRecentActivityDTO(Product productToMap) {

        if (productToMap == null) return null;

        String textToAttach = String.join(" ", NEW_PRODUCT_ON_STOCK, productToMap.getName());
        EventType eventType = EventType.NUEVO_PRODUCTO;
        LocalDateTime timestamp = productToMap.getCreationDate();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapNewAppointmentToRecentActivityDTO(Appointment appointmentToMap) {

        if (appointmentToMap == null) return null;

        String textToAttach = String.join(" ", NEW_APPOINTMENT_REGISTERED_STRING, appointmentToMap.getClient().getFirstName(), appointmentToMap.getEmployee().getFirstName());
        EventType eventType = EventType.NUEVO_TURNO;
        LocalDateTime timestamp = appointmentToMap.getModifiedDate();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapTerminatedEmployeeToRecentActivityDTO(Employee terminatedEmployee) {

        if (terminatedEmployee == null) return null;

        String textToAttach = String.join(" ", TERMINATED_EMPLOYEE_STRING, terminatedEmployee.getFirstName(), terminatedEmployee.getLastName());
        EventType eventType = EventType.EMPLEADO_DESVINCULADO;
        LocalDateTime timestamp = terminatedEmployee.getTerminationDate().atStartOfDay();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapCompletedAppointmentToRecentActivityDTO(Appointment completedAppointment) {

        if (completedAppointment == null) return null;

        String textToAttach = String.join(" ", COMPLETED_APPOINTMENT_STRING, completedAppointment.getClient().getFirstName(), completedAppointment.getEmployee().getFirstName());
        EventType eventType = EventType.TURNO_FINALIZADO;
        LocalDateTime timestamp = completedAppointment.getModifiedDate();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapCanceledAppointmentToRecentActivityDTO(Appointment canceledAppointment) {

        if (canceledAppointment == null) return null;

        String textToAttach = String.join(" ", CANCELED_APPOINTMENT_STRING, canceledAppointment.getClient().getFirstName(), canceledAppointment.getEmployee().getFirstName());
        EventType eventType = EventType.TURNO_CANCELADO;
        LocalDateTime timestamp = canceledAppointment.getModifiedDate();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO buildDTOFromParameters(String textToAttach, EventType eventType, LocalDateTime timestamp) {

        return RecentActivityDTO.builder()
                .eventType(eventType)
                .text(textToAttach)
                .timestamp(timestamp)
                .build();
    }
}
