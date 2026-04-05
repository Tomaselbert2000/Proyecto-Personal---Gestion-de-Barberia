package com.barbershop.service.implementation;

import com.barbershop.dto.dashboard.RecentActivityDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.EventType;
import com.barbershop.model.Appointment;
import com.barbershop.model.Client;
import com.barbershop.model.Employee;
import com.barbershop.model.Product;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.repository.ClientRepository;
import com.barbershop.repository.EmployeeRepository;
import com.barbershop.repository.ProductRepository;
import com.barbershop.service.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final AppointmentRepository appointmentRepository;

    private static final String NEW_CLIENT_REGISTERED_STRING = "Nuevo cliente registrado en el sistema";
    private static final String NEW_EMPLOYEE_REGISTERED_STRING = "Contratación de nuevo empleado";
    private static final String NEW_PRODUCT_ON_STOCK = "Un nuevo producto fue ingresado en stock";
    private static final String NEW_APPOINTMENT_REGISTERED_STRING = "Se ha agendado un nuevo turno";
    private static final String TERMINATED_EMPLOYEE_STRING = "Un empleado fue desvinculado recientemente";
    private static final String COMPLETED_APPOINTMENT_STRING = "Turno completado recientemente";
    private static final String CANCELED_APPOINTMENT_STRING = "Turno cancelado recientemente";
    private static final String LOW_STOCK_PRODUCT_STRING = "Atención requerida - Stock bajo y/o crítico";

    private static final Integer TOP_RESULTS_VALUE = 25;

    @Override
    public List<RecentActivityDTO> getRecentActivityLog() {

        List<Client> latestFiveRegisteredClients = clientRepository.findTop5ByOrderByRegistrationDateDesc();
        List<Employee> latestFiveHiredEmployees = employeeRepository.findTop5ByOrderByHireDateDesc();
        List<Product> latestFiveRegisteredProducts = productRepository.findTop5ByOrderByCreationDateDesc();
        List<Appointment> latestFiveRegistratedAppointments = appointmentRepository.findTop5ByOrderByRegistrationTimestampDesc();
        List<Employee> latestFiveTerminatedEmployees = employeeRepository.findTop5ByTerminationDateIsNotNullOrderByTerminationDateDesc();
        List<Appointment> latesFivetCompletedAppointments = appointmentRepository.findTop5ByCurrentStatusOrderByRegistrationTimestampDesc(AppointmentStatus.FINALIZADO);
        List<Appointment> latestFiveCanceledAppointments = appointmentRepository.findTop5ByCurrentStatusOrderByRegistrationTimestampDesc(AppointmentStatus.CANCELADO);

        List<RecentActivityDTO> resultList = new ArrayList<>();

        addAllDTOsToResultList(
                resultList,
                latestFiveRegisteredClients.stream().map(this::mapNewClientToRecentActivityDTO).toList(),
                latestFiveHiredEmployees.stream().map(this::mapNewEmployeeToRecentActivityDTO).toList(),
                latestFiveRegisteredProducts.stream().map(this::mapNewProductToRecentActivityDTO).toList(),
                latestFiveRegistratedAppointments.stream().map(this::mapNewAppointmentToRecentActivityDTO).toList(),
                latestFiveTerminatedEmployees.stream().map(this::mapTerminatedEmployeeToRecentActivityDTO).toList(),
                latesFivetCompletedAppointments.stream().map(this::mapCompletedAppointmentToRecentActivityDTO).toList(),
                latestFiveCanceledAppointments.stream().map(this::mapCanceledAppointmentToRecentActivityDTO).toList()
        );

        orderDTOListByTimeStamp(resultList);

        resultList = extractLast10ResultsFromList(resultList);

        return resultList;
    }

    @Override
    public List<RecentActivityDTO> getLowStockProductsLog() {

        List<Product> lowStockLevelProducts = productRepository.getLowStockProducts();

        return lowStockLevelProducts.stream().map(this::mapLowStockProductToRecentActivityDTO).toList();
    }

    private void addAllDTOsToResultList(
            List<RecentActivityDTO> resultList,
            List<RecentActivityDTO> recentClientsActivity,
            List<RecentActivityDTO> recentEmployeesActivity,
            List<RecentActivityDTO> recentProductsActivity,
            List<RecentActivityDTO> recentAppointmentsActivity,
            List<RecentActivityDTO> recentTerminatedEmployees,
            List<RecentActivityDTO> recentCompletedAppointments,
            List<RecentActivityDTO> recentCanceledAppointments) {

        resultList.addAll(recentClientsActivity);
        resultList.addAll(recentEmployeesActivity);
        resultList.addAll(recentProductsActivity);
        resultList.addAll(recentAppointmentsActivity);
        resultList.addAll(recentTerminatedEmployees);
        resultList.addAll(recentCompletedAppointments);
        resultList.addAll(recentCanceledAppointments);
    }

    private void orderDTOListByTimeStamp(List<RecentActivityDTO> resultList) {

        resultList.sort(Comparator.comparing(RecentActivityDTO::getTimestamp).reversed());
    }

    private List<RecentActivityDTO> extractLast10ResultsFromList(List<RecentActivityDTO> resultList) {

        int maxValueForSize = Math.min(resultList.size(), TOP_RESULTS_VALUE);

        return resultList.subList(0, maxValueForSize);
    }

    private RecentActivityDTO mapNewClientToRecentActivityDTO(Client clientToMap) {

        if (clientToMap == null) return null;

        List<String> stringList = List.of(NEW_CLIENT_REGISTERED_STRING, clientToMap.getFirstName(), clientToMap.getLastName());

        String textToAttach = concatStringsFromList(stringList);
        EventType eventType = EventType.NUEVO_CLIENTE;
        LocalDateTime timestamp = clientToMap.getRegistrationDate().atStartOfDay();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapNewEmployeeToRecentActivityDTO(Employee employeeToMap) {

        if (employeeToMap == null) return null;

        List<String> stringList = List.of(NEW_EMPLOYEE_REGISTERED_STRING, employeeToMap.getFirstName(), employeeToMap.getLastName());

        String textToAttach = concatStringsFromList(stringList);
        EventType eventType = EventType.NUEVO_EMPLEADO;
        LocalDateTime timestamp = employeeToMap.getHireDate().atStartOfDay();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapNewProductToRecentActivityDTO(Product productToMap) {

        if (productToMap == null) return null;

        List<String> stringList = List.of(NEW_PRODUCT_ON_STOCK, productToMap.getName());

        String textToAttach = concatStringsFromList(stringList);
        EventType eventType = EventType.NUEVO_PRODUCTO;
        LocalDateTime timestamp = productToMap.getCreationDate();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapNewAppointmentToRecentActivityDTO(Appointment appointmentToMap) {

        if (appointmentToMap == null) return null;

        List<String> stringList = List.of(NEW_APPOINTMENT_REGISTERED_STRING, appointmentToMap.getClient().getFirstName(), appointmentToMap.getEmployee().getFirstName());

        String textToAttach = concatStringsFromList(stringList);
        EventType eventType = EventType.NUEVO_TURNO;
        LocalDateTime timestamp = appointmentToMap.getModifiedDate();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapTerminatedEmployeeToRecentActivityDTO(Employee terminatedEmployee) {

        if (terminatedEmployee == null) return null;

        List<String> stringList = List.of(TERMINATED_EMPLOYEE_STRING, terminatedEmployee.getFirstName(), terminatedEmployee.getLastName());

        String textToAttach = concatStringsFromList(stringList);
        EventType eventType = EventType.EMPLEADO_DESVINCULADO;
        LocalDateTime timestamp = terminatedEmployee.getTerminationDate().atStartOfDay();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapCompletedAppointmentToRecentActivityDTO(Appointment completedAppointment) {

        if (completedAppointment == null) return null;

        List<String> stringList = List.of(COMPLETED_APPOINTMENT_STRING, completedAppointment.getClient().getFirstName(), completedAppointment.getEmployee().getFirstName());

        String textToAttach = concatStringsFromList(stringList);
        EventType eventType = EventType.TURNO_FINALIZADO;
        LocalDateTime timestamp = completedAppointment.getModifiedDate();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapCanceledAppointmentToRecentActivityDTO(Appointment canceledAppointment) {

        if (canceledAppointment == null) return null;

        List<String> stringList = List.of(CANCELED_APPOINTMENT_STRING, canceledAppointment.getClient().getFirstName(), canceledAppointment.getEmployee().getFirstName());

        String textToAttach = concatStringsFromList(stringList);
        EventType eventType = EventType.TURNO_CANCELADO;
        LocalDateTime timestamp = canceledAppointment.getModifiedDate();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO mapLowStockProductToRecentActivityDTO(Product productWithLowStock) {

        if (productWithLowStock == null) return null;

        List<String> stringList = List.of(LOW_STOCK_PRODUCT_STRING, productWithLowStock.getName(), "Stock actual: ", productWithLowStock.getCurrentStockLevel().toString());

        String textToAttach = concatStringsFromList(stringList);
        EventType eventType = EventType.ALERTA_STOCK_BAJO;
        LocalDateTime timestamp = LocalDateTime.now();

        return buildDTOFromParameters(textToAttach, eventType, timestamp);
    }

    private RecentActivityDTO buildDTOFromParameters(String textToAttach, EventType eventType, LocalDateTime timestamp) {

        return RecentActivityDTO.builder()
                .eventType(eventType)
                .text(textToAttach)
                .timestamp(timestamp)
                .build();
    }

    private String concatStringsFromList(List<String> stringList) {

        StringBuilder concatenatedStringIncludingSpaces = new StringBuilder();

        for (String str : stringList) {

            concatenatedStringIncludingSpaces.append(str);
            concatenatedStringIncludingSpaces.append(" ");
        }

        return concatenatedStringIncludingSpaces.toString();
    }
}
