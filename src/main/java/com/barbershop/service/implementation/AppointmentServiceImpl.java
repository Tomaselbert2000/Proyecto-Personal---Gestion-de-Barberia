package com.barbershop.service.implementation;

import com.barbershop.dto.appointment.AppointmentCreationDTO;
import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.dto.appointment.AppointmentUpdateDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.exceptions.appointment.AppointmentNotFoundException;
import com.barbershop.exceptions.common.EmployeeNotAvailableException;
import com.barbershop.exceptions.barberservice.BarberServiceNotFoundException;
import com.barbershop.exceptions.client.ClientNotFoundException;
import com.barbershop.exceptions.employee.EmployeeNotFoundException;
import com.barbershop.exceptions.sale.InactiveEmployeeException;
import com.barbershop.mapper.interfaces.AppointmentMapper;
import com.barbershop.model.Appointment;
import com.barbershop.model.BarberService;
import com.barbershop.model.Client;
import com.barbershop.model.Employee;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.repository.BarberServiceRepository;
import com.barbershop.repository.ClientRepository;
import com.barbershop.repository.EmployeeRepository;
import com.barbershop.service.interfaces.AppointmentService;
import com.barbershop.utils.time.TimeCalculation;
import com.barbershop.validation.appointment.AppointmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final ClientRepository clientRepository;
    private final BarberServiceRepository barberServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentValidator validator;
    private final AppointmentMapper mapper;

    private static final LocalTime LAST_SECOND_OF_DAY = LocalTime.MAX;

    @Override
    @Transactional
    public void registerNewAppointment(AppointmentCreationDTO creationDTO) {

        validator.validateForCreation(creationDTO);

        Client client = loadClient(creationDTO.getClientID());
        BarberService service = loadBarberService(creationDTO.getBarberserviceID());
        Employee employee = loadEmployee(creationDTO.getEmployeeID());

        checkIfEmployeeIsActive(employee);

        checkEmployeeAvailabilityForCreation(employee, creationDTO.getStartDateTime(), creationDTO.getEndDateTime());

        Appointment newAppointment = mapper.mapAppointmentCreationDtoToAppointmentEntity(creationDTO, client, employee, service);

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

        return mapper.mapAppointmentToInfoDto(appointmentOnDB);
    }

    @Override
    public List<AppointmentInfoDTO> getAppointmentsList() {

        return mapper.mapAppointmentToInfoDto(appointmentRepository.findAll());
    }

    @Override
    public Long appointmentsByStatus(AppointmentStatus status) {

        return appointmentRepository.countByCurrentStatus(status);
    }

    @Override
    public Long appointmentsToday() {

        LocalDateTime startDateTimeAfter = TimeCalculation.getStartOfToday();
        LocalDateTime startDateTimeBefore = TimeCalculation.getEndOfToday();

        return appointmentRepository.countByStartDateTimeBetween(startDateTimeAfter, startDateTimeBefore);
    }

    @Override
    public Long completedAppointmentsToday() {

        LocalDateTime startOfToday = TimeCalculation.getStartOfToday();
        LocalDateTime endOfToday = TimeCalculation.getEndOfToday();

        return appointmentRepository.countByStartDateTimeBetweenAndCurrentStatus(startOfToday, endOfToday, AppointmentStatus.FINALIZADO);
    }

    @Override
    public Long appointmentsCreatedToday() {

        LocalDateTime startOfToday = TimeCalculation.getStartOfToday();

        return appointmentRepository.countByRegistrationTimestampAfter(startOfToday);
    }

    @Override
    public Long appointmentsDuringThisMonth() {

        LocalDateTime firstDayOfCurrentMonth = TimeCalculation.getStartOfCurrentMonth().atStartOfDay();
        LocalDateTime lastDayOfCurrentMonth = TimeCalculation.getEndOfCurrentMonth().atTime(LAST_SECOND_OF_DAY);

        return appointmentRepository.countByRegistrationTimestampBetween(firstDayOfCurrentMonth, lastDayOfCurrentMonth);
    }

    @Override
    public Long calculatePercentageOfAppointmentsVsPreviousMonth() {

        Long appointmentsThisMonth = appointmentsDuringThisMonth();

        Long appointmentsThePastMonth = appointmentsThePastMonth();

        if (appointmentsThePastMonth == 0 && appointmentsThisMonth == 0) {

            return 0L;

        } else if (appointmentsThePastMonth == 0) {

            return 100L;
        }

        return ((appointmentsThisMonth - appointmentsThePastMonth) * 100) / appointmentsThePastMonth;
    }

    @Override
    public Long canceledAppointments() {

        return appointmentRepository.countByCurrentStatus(AppointmentStatus.CANCELADO);
    }

    @Override
    public Long canceledAppointmentsVsPastWeek() {

        LocalDate today = TimeCalculation.getCurrentDate();
        LocalDate oneWeekAgo = TimeCalculation.getCurrentDate().minusDays(7);

        return appointmentRepository.countByCurrentStatusAndRegistrationTimestampBetween(AppointmentStatus.CANCELADO, today.atStartOfDay(), oneWeekAgo.atTime(LAST_SECOND_OF_DAY));
    }

    @Override
    public Long getTotalAppointmentsCount() {

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

        appointmentRepository.save(mapper.mapAppointmentUpdateDtoToAppointmentEntity(updateDTO, employee, service, appointmentToUpdate));
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

    private Long appointmentsThePastMonth() {

        LocalDate startOfPastMonth = TimeCalculation.getStartOfCurrentMonth().minusMonths(1);
        LocalDate endOfPastMonth = TimeCalculation.getEndOfCurrentMonth().minusMonths(1);

        return appointmentRepository.countByRegistrationTimestampBetween(startOfPastMonth.atStartOfDay(), endOfPastMonth.atTime(LAST_SECOND_OF_DAY));
    }
}
