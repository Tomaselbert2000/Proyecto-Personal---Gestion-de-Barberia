package com.service.implementation;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.stats.AppointmentCanceledStatsDTO;
import com.dto.stats.AppointmentMonthlyComparisonDTO;
import com.dto.stats.AppointmentTodayStatsDTO;
import com.dto.stats.AppointmentTomorrowStatsDTO;
import com.enums.AppointmentStatus;
import com.exceptions.appointment.AppointmentNotFoundException;
import com.exceptions.barberservice.BarberServiceNotFoundException;
import com.exceptions.client.ClientNotFoundException;
import com.exceptions.common.EmployeeNotAvailableException;
import com.exceptions.employee.EmployeeNotFoundException;
import com.exceptions.sale.InactiveEmployeeException;
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
import com.service.interfaces.BarberserviceService;
import com.service.interfaces.ClientService;
import com.service.interfaces.EmployeeService;
import com.validation.appointment.AppointmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.utils.time.TimeCalculation.*;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final LocalTime LAST_SECOND_OF_DAY = LocalTime.MAX;
    private static final String EMPLOYEE_SELECTOR_FIRST_ITEM = "Todos los empleados";
    private final ClientRepository clientRepository;
    private final BarberServiceRepository barberServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentValidator validator;
    private final AppointmentMapper appointmentMapper;
    private final ClientService clientService;
    private final BarberserviceService barberserviceService;
    private final EmployeeService employeeService;

    @Override
    @Transactional
    public void registerNewAppointment(AppointmentCreationDTO creationDTO) {

        validator.validateForCreation(creationDTO);

        Client client = loadClient(creationDTO.getClientID());
        BarberService service = loadBarberService(creationDTO.getBarberserviceID());
        Employee employee = loadEmployee(creationDTO.getEmployeeID());

        checkIfEmployeeIsActive(employee);

        checkEmployeeAvailabilityForCreation(employee, creationDTO.getStartDateTime(), creationDTO.getEndDateTime());

        Appointment newAppointment = appointmentMapper.mapAppointmentCreationDtoToAppointmentEntity(creationDTO, client, employee, service);

        appointmentRepository.save(newAppointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(Long appointmentID) {

        Appointment appointmentOnDB = loadAppointment(appointmentID);

        appointmentRepository.delete(appointmentOnDB);
    }

    @Override
    public AppointmentInfoDTO getAppointmentInfo(Long appointmentID) {

        Appointment appointmentOnDB = loadAppointment(appointmentID);

        return appointmentMapper.mapAppointmentToInfoDto(appointmentOnDB);
    }

    @Override
    public List<AppointmentInfoDTO> getAppointmentsList() {

        return appointmentMapper.mapAppointmentToInfoDto(appointmentRepository.findAll());
    }

    @Override
    public Long appointmentsToday() {

        LocalDateTime startDateTimeAfter = getStartOfToday();
        LocalDateTime startDateTimeBefore = getEndOfToday();

        return appointmentRepository.countByStartDateTimeBetween(startDateTimeAfter, startDateTimeBefore);
    }

    @Override
    public Long completedAppointmentsToday() {

        LocalDateTime startOfToday = getStartOfToday();
        LocalDateTime endOfToday = getEndOfToday();

        return appointmentRepository.countByStartDateTimeBetweenAndCurrentStatus(startOfToday, endOfToday, AppointmentStatus.FINALIZADO);
    }

    @Override
    public List<AppointmentInfoDTO> liveSearch(String clientName, LocalDate date, AppointmentStatus selectedAppointmentStatus, String employeeName) {

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (date != null) {

            startDateTime = date.atStartOfDay();
            endDateTime = date.atTime(LAST_SECOND_OF_DAY);
        }

        if (employeeName.equals(EMPLOYEE_SELECTOR_FIRST_ITEM)) employeeName = null;

        return appointmentMapper.mapAppointmentToInfoDto(appointmentRepository.liveSearchWithFilters(clientName, selectedAppointmentStatus, employeeName, startDateTime, endDateTime));
    }

    @Override
    public List<EmployeeInfoDTO> getEmployeesFromServiceInstance() {

        return employeeService.getEmployeeList();
    }

    @Override
    @Transactional
    public void markAppointmentAsComplete(AppointmentInfoDTO dto) {

        Appointment appointmentOnDB = loadAppointment(dto.getId());

        applyStatusChangeIfPresent(appointmentOnDB, AppointmentStatus.FINALIZADO);

        appointmentRepository.save(appointmentOnDB);
    }

    @Override
    @Transactional
    public void markAppointmentAsCanceled(AppointmentInfoDTO dto) {

        Appointment appointmentOnDB = loadAppointment(dto.getId());

        applyStatusChangeIfPresent(appointmentOnDB, AppointmentStatus.CANCELADO);

        appointmentRepository.save(appointmentOnDB);
    }

    @Override
    public List<BarberServiceInfoDTO> getBarberServicesFromServiceInstance() {

        return barberserviceService.getServicesList();
    }

    @Override
    public List<ClientInfoDTO> clientLiveSearchByName(String searchName) {

        return clientService.clientLiveSearchByName(searchName);
    }

    @Override
    public AppointmentTodayStatsDTO getAppointmentsTodayStats() {

        AppointmentTodayStatsDTO appointmentTodayStatsDTO = appointmentRepository.getAppoinmentsTodayStats(getStartOfToday(), getEndOfToday());

        if (appointmentTodayStatsDTO != null) {

            if (appointmentTodayStatsDTO.getTotalAmountAsFinished() == null)
                appointmentTodayStatsDTO.setTotalAmountAsFinished(0L);
            if (appointmentTodayStatsDTO.getAppointmentCount() == null)
                appointmentTodayStatsDTO.setAppointmentCount(0L);

            return appointmentTodayStatsDTO;

        } else {

            return emptyAppointmentTodayStatsDTO();
        }
    }

    @Override
    public AppointmentTomorrowStatsDTO getPendingAppointmentsStats() {

        AppointmentTomorrowStatsDTO appointmentTomorrowStatsDTO = appointmentRepository.getPendingAppointmentsStats(
                LocalDateTime.now(),
                getStartOfToday().plusDays(1),
                getEndOfToday().plusDays(1));

        if (appointmentTomorrowStatsDTO != null) {

            if (appointmentTomorrowStatsDTO.getScheduledAppointmentsTomorrow() == null)
                appointmentTomorrowStatsDTO.setScheduledAppointmentsTomorrow(0L);
            if (appointmentTomorrowStatsDTO.getTotalPendingAppointments() == null)
                appointmentTomorrowStatsDTO.setTotalPendingAppointments(0L);

            return appointmentTomorrowStatsDTO;

        } else {

            return emptyAppointmentTomorrowStatsDTO();
        }
    }

    @Override
    public AppointmentMonthlyComparisonDTO getMonthlyComparisonStats() {

        AppointmentMonthlyComparisonDTO comparisonStats = appointmentRepository.getMonthlyComparisonStats(
                getStartOfCurrentMonth().atStartOfDay(),
                getEndOfCurrentMonth().atTime(LAST_SECOND_OF_DAY),
                getStartOfCurrentMonth().minusMonths(1).atStartOfDay(),
                getEndOfCurrentMonth().minusMonths(1).atTime(LAST_SECOND_OF_DAY)
        );

        if (comparisonStats != null) {

            if (comparisonStats.getCurrentMonthAppointments() == null) comparisonStats.setCurrentMonthAppointments(0L);
            if (comparisonStats.getPreviousMonthAppointments() == null)
                comparisonStats.setPreviousMonthAppointments(0L);

            return comparisonStats;

        } else {

            return emptyAppointmentMonthlyComparisonDTO();
        }
    }

    @Override
    public AppointmentCanceledStatsDTO getCanceledStats() {

        AppointmentCanceledStatsDTO canceledStatsDTO = appointmentRepository.getCanceledAppointmentsStats(
                getStartOfCurrentMonth().atStartOfDay(),
                getEndOfCurrentMonth().atTime(LAST_SECOND_OF_DAY));

        if (canceledStatsDTO != null) {

            Double cancelationPercentage = ((double) canceledStatsDTO.getTotalAppointmentsThisMonth() * canceledStatsDTO.getCanceledAppointmentThisMonth()) / 100;

            canceledStatsDTO.setCanceledAppointmentPercentage(cancelationPercentage);

            return canceledStatsDTO;

        } else {

            return emptyAppointmentCanceledStatsDTO();
        }
    }

    @Override
    public Long getCount() {

        return appointmentRepository.count();
    }

    @Override
    @Transactional
    public void updateAppointment(Long appointmentID, AppointmentUpdateDTO updateDTO) {

        validator.validateForUpdate(updateDTO);

        Appointment appointmentToUpdate = loadAppointment(appointmentID);

        BarberService service = resolveIfBarberServiceWasModified(appointmentToUpdate, updateDTO.getNewBarberserviceID());
        Employee employee = resolveIfEmployeeWasModified(appointmentToUpdate, updateDTO.getNewEmployeeID());

        checkEmployeeAvailabilityForUpdate(employee, updateDTO.getNewStartDateTime(), updateDTO.getNewEndDateTime(), appointmentID);

        applyStatusChangeIfPresent(appointmentToUpdate, updateDTO.getNewStatus());

        appointmentRepository.save(appointmentMapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, employee, service, appointmentToUpdate));
    }

    private Client loadClient(Long clientID) {

        if (clientID == null) return null;

        return clientRepository.findById(clientID).orElseThrow(ClientNotFoundException::new);
    }

    private BarberService loadBarberService(Long barberserviceID) {

        if (barberserviceID == null) return null;

        return barberServiceRepository.findById(barberserviceID).orElseThrow(BarberServiceNotFoundException::new);
    }

    private Employee loadEmployee(Long employeeID) {

        if (employeeID == null) return null;

        return employeeRepository.findById(employeeID).orElseThrow(EmployeeNotFoundException::new);
    }

    private Appointment loadAppointment(Long appointmentID) {

        if (appointmentID == null) return null;

        return appointmentRepository.findById(appointmentID).orElseThrow(AppointmentNotFoundException::new);
    }

    private void checkIfEmployeeIsActive(Employee employee) {

        if (!employee.isActive()) throw new InactiveEmployeeException();
    }

    private void applyStatusChangeIfPresent(Appointment appointmentToUpdate, AppointmentStatus newStatus) {

        if (newStatus != null) appointmentToUpdate.changeStatus(newStatus);
    }

    private void checkEmployeeAvailabilityForCreation(Employee employee, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        if (appointmentRepository.existsOverlappingAppointmentOnCreate(employee.getEmployeeID(), startDateTime, endDateTime))
            throw new EmployeeNotAvailableException();
    }

    private void checkEmployeeAvailabilityForUpdate(Employee employee, LocalDateTime newStartDateTime, LocalDateTime newEndDateTime, Long appointmentID) {

        if (appointmentRepository.existsOverlappingAppointmentOnUpdate(employee.getEmployeeID(), newStartDateTime, newEndDateTime, appointmentID))
            throw new EmployeeNotAvailableException();
    }

    private Employee resolveIfEmployeeWasModified(Appointment appointmentToUpdate, Long newEmployeeID) {

        Employee currentAssignedEmployee = appointmentToUpdate.getEmployee();

        if (newEmployeeID == null) {

            return currentAssignedEmployee;
        }

        if (currentAssignedEmployee != null && currentAssignedEmployee.getEmployeeID().equals(newEmployeeID)) {

            return currentAssignedEmployee;
        }

        return loadEmployee(newEmployeeID);
    }

    private BarberService resolveIfBarberServiceWasModified(Appointment appointmentToUpdate, Long newBarberserviceID) {

        BarberService currentAssignedService = appointmentToUpdate.getBarberservice();

        if (newBarberserviceID == null) {

            return currentAssignedService;
        }

        if (currentAssignedService != null && currentAssignedService.getBarbershopServiceID().equals(newBarberserviceID)) {

            return currentAssignedService;
        }

        return loadBarberService(newBarberserviceID);
    }

    private AppointmentTodayStatsDTO emptyAppointmentTodayStatsDTO() {

        return AppointmentTodayStatsDTO.builder()
                .appointmentCount(0L)
                .totalAmountAsFinished(0L)
                .build();
    }

    private AppointmentTomorrowStatsDTO emptyAppointmentTomorrowStatsDTO() {

        return AppointmentTomorrowStatsDTO.builder()
                .totalPendingAppointments(0L)
                .scheduledAppointmentsTomorrow(0L)
                .build();
    }

    private AppointmentMonthlyComparisonDTO emptyAppointmentMonthlyComparisonDTO() {

        return AppointmentMonthlyComparisonDTO.builder()
                .currentMonthAppointments(0L)
                .previousMonthAppointments(0L)
                .build();
    }

    private AppointmentCanceledStatsDTO emptyAppointmentCanceledStatsDTO() {

        return AppointmentCanceledStatsDTO.builder()
                .canceledAppointmentThisMonth(0L)
                .totalAppointmentsThisMonth(0L)
                .build();
    }
}
