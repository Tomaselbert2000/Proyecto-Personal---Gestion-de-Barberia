package com.service.implementation;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;
import com.exceptions.employee.EmployeeNotFoundException;
import com.exceptions.employee.InvalidEmployeeTerminationDateException;
import com.mapper.interfaces.EmployeeMapper;
import com.model.Employee;
import com.repository.EmployeeRepository;
import com.service.interfaces.EmployeeService;
import com.utils.time.TimeCalculation;
import com.validation.employee.EmployeeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.utils.time.TimeCalculation.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

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

        Long monthlyAppointmentsCount = getMonthlyAppointmentsCountByEmployeeID(employeeID);

        EmployeeInfoDTO dto = mapper.mapEmployeeToInfoDTO(employeeOnDB);

        dto.setMonthlyAppointmentsCount(monthlyAppointmentsCount);

        return dto;
    }

    @Override
    public List<EmployeeInfoDTO> getEmployeeList() {

        List<EmployeeInfoDTO> dtos = mapper.mapEmployeeToInfoDTO(employeeRepository.findAll());

        for (EmployeeInfoDTO dto : dtos) {

            Long monthlyAppointmentsCount = getMonthlyAppointmentsCountByEmployeeID(dto.getId());

            dto.setMonthlyAppointmentsCount(monthlyAppointmentsCount);
        }

        return dtos;
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
    public Long getEmployeeCount() {

        return employeeRepository.count();
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Override
    public List<EmployeeInfoDTO> liveSearch(String employeeName, EmployeeStatus selectedStatus, HireDateRange dateRange) {

        LocalDate startDate = null;
        LocalDate endDate = null;

        Boolean statusFlag = getBooleanFlagFromStatusValue(selectedStatus);

        if (dateRange != null) {

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

    private Long getMonthlyAppointmentsCountByEmployeeID(Long employeeID) {

        return employeeRepository.getMonthlyAppointmentsByEmployee(
                employeeID,
                getStartOfCurrentMonth().atStartOfDay(),
                getEndOfCurrentMonth().atTime(LAST_SECOND_OF_DAY)
        );
    }
}
