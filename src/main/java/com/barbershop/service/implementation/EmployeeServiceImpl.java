package com.barbershop.service.implementation;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.enums.EmployeeStatus;
import com.barbershop.enums.HireDateRange;
import com.barbershop.exceptions.employee.EmployeeNotFoundException;
import com.barbershop.exceptions.employee.InvalidEmployeeTerminationDateException;
import com.barbershop.mapper.interfaces.EmployeeMapper;
import com.barbershop.model.Employee;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.repository.EmployeeRepository;
import com.barbershop.service.interfaces.EmployeeService;
import com.barbershop.utils.time.TimeCalculation;
import com.barbershop.validation.employee.EmployeeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.barbershop.utils.time.TimeCalculation.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AppointmentRepository appointmentRepository;

    private final EmployeeMapper mapper;

    private final EmployeeValidator validator;

    @Override
    @Transactional
    public void registerNewEmployee(EmployeeCreationDTO dto) {

        validator.validateDTO(dto);

        employeeRepository.save(mapper.mapEmployeeCreationDtoToEntity(dto));
    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeeID) {

        Employee employeeToDelete = loadEmployee(employeeID);

        employeeRepository.delete(employeeToDelete);
    }

    @Override
    public EmployeeInfoDTO getEmployeeInfo(Long employeeID) {

        Employee employeeOnDB = loadEmployee(employeeID);

        return mapper.mapEmployeeToInfoDTO(employeeOnDB);
    }

    @Override
    public List<EmployeeInfoDTO> getEmployeeList() {

        return mapper.mapEmployeeToInfoDTO(employeeRepository.findAll());
    }

    @Override
    @Transactional
    public void updateEmployee(Long employeeID, EmployeeUpdateDTO updateDTO) {

        Employee employeeOnDB = loadEmployee(employeeID);

        validator.validateDTO(updateDTO);

        checkTerminationDate(employeeOnDB, updateDTO.getTerminationDate());

        employeeRepository.save(mapper.mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO));
    }

    @Override
    public Long getActiveEmployees() {

        return employeeRepository.getActiveEmployees();
    }

    @Override
    public Long getInactiveEmployees() {

        return employeeRepository.getInactiveEmployees();
    }

    @Override
    public Long calculateActiveEmployeesVsLastMonth() {

        LocalDate startOfCurrentMonth = getStartOfCurrentMonth();
        LocalDate endOfCurrentMonth = TimeCalculation.getEndOfCurrentMonth();

        LocalDate startOfLastMonth = startOfCurrentMonth.minusMonths(1);
        LocalDate endOfLastMonth = endOfCurrentMonth.minusMonths(1);

        Long employeesActiveThisMonth = employeeRepository.countEmployeesInRange(startOfCurrentMonth, endOfCurrentMonth);
        Long employeesActiveTheLastMonth = employeeRepository.countEmployeesInRange(startOfLastMonth, endOfLastMonth);

        return employeesActiveThisMonth - employeesActiveTheLastMonth;
    }

    @Override
    public Long getEmployeeCount() {

        return employeeRepository.count();
    }

    @Override
    public Long getEmployeesTrendThisMonth() {

        LocalDate startDateTimeAfter = getStartOfCurrentMonth();
        LocalDate startDateTimeBefore = TimeCalculation.getEndOfCurrentMonth();

        return employeeRepository.countEmployeesInRange(startDateTimeAfter, startDateTimeBefore);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Override
    public List<EmployeeInfoDTO> liveSearch(String employeeName, EmployeeStatus selectedStatus, HireDateRange dateRange) {

        LocalDate startDate = null;
        LocalDate endDate = null;

        Boolean statusFlag = getBooleanFlagFromStatusValue(selectedStatus);

        if(dateRange != null){

            switch (dateRange) {

                case ULTIMOS_SEIS_MESES -> {

                    startDate = getCurrentDate().minusMonths(6);
                    endDate = getCurrentDate();
                }

                case ESTE_AÑO -> {

                    startDate = TimeCalculation.getCurrentDate().withDayOfYear(1);
                    endDate = getCurrentDate();
                }

                case MAS_DE_UN_AÑO -> endDate = getCurrentDate().minusMonths(12);
            }
        }

        return mapper.mapEmployeeToInfoDTO(employeeRepository.liveSearchWithFilters(employeeName, statusFlag, startDate, endDate));
    }

    @Override
    public double calculateAverageMonthlyProductivity() {

        LocalDateTime startDateTime = getStartOfCurrentMonth().atStartOfDay();
        LocalDateTime endDateTime = getEndOfCurrentMonth().atTime(LocalTime.MAX);

        Long employeeCount = getActiveEmployees();
        Long appointmentCount = appointmentRepository.countByStartDateTimeBetween(startDateTime, endDateTime);

        if (employeeCount == 0) return 0.0;

        return (double) appointmentCount / employeeCount;
    }

    @Override
    public double calculateProductivityTrendVsLastMonth() {

        LocalDate startOfLastMonth = LocalDate.from(TimeCalculation.getStartOfLastMonth());
        LocalDate endOfLastMonth = LocalDate.from(TimeCalculation.getEndOfLastMonth());

        Long lastMonthActiveEmployees = employeeRepository.countEmployeesInRange(startOfLastMonth, endOfLastMonth);
        Long lastMonthAppointments = appointmentRepository.countByStartDateTimeBetween(startOfLastMonth.atStartOfDay(), endOfLastMonth.atTime(LAST_SECOND_OF_DAY));

        if (lastMonthActiveEmployees == 0) return 0.0;

        double lastMonthProductivity = (double) lastMonthAppointments / lastMonthActiveEmployees;

        double currentMonthProductivity = calculateAverageMonthlyProductivity();

        if (lastMonthProductivity == 0.0) return currentMonthProductivity > 0 ? 100.0 : 0.0;

        return ((currentMonthProductivity - lastMonthProductivity) / lastMonthProductivity) * 100.0;
    }

    @Override
    @Transactional
    public void changeEmployeeIsActiveValue(Long id) {

        Employee employee = loadEmployee(id);

        boolean value = employee.isActive();

        employee.setActive(!value);

        employeeRepository.save(employee);
    }

    private Employee loadEmployee(Long employeeID) {

        return employeeRepository.findById(employeeID).orElseThrow(EmployeeNotFoundException::new);
    }

    private void checkTerminationDate(Employee employeeOnDB, LocalDate terminationDate) {

        if (terminationDate != null) {

            if (terminationDate.isBefore(employeeOnDB.getHireDate()))
                throw new InvalidEmployeeTerminationDateException();
        }
    }

    private Boolean getBooleanFlagFromStatusValue(EmployeeStatus selectedStatus) {

        Boolean statusFlag;

        if (selectedStatus == null) return null;

        switch (selectedStatus) {

            case ACTIVO -> statusFlag = true;

            case INACTIVO -> statusFlag = false;

            default -> statusFlag = null;
        }

        return statusFlag;
    }
}
