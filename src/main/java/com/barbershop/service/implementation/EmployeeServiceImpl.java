package com.barbershop.service.implementation;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.exceptions.employee.EmployeeNotFoundException;
import com.barbershop.exceptions.employee.InvalidEmployeeTerminationDateException;
import com.barbershop.mapper.interfaces.EmployeeMapper;
import com.barbershop.model.Employee;
import com.barbershop.repository.EmployeeRepository;
import com.barbershop.service.interfaces.EmployeeService;
import com.barbershop.utils.time.TimeCalculation;
import com.barbershop.validation.employee.EmployeeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

        return employeeRepository.count();
    }

    @Override
    public Long calculateActiveEmployeesVsLastMonth() {

        LocalDate startOfCurrentMonth = TimeCalculation.getStartOfCurrentMonth();
        LocalDate endOfCurrentMonth = TimeCalculation.getEndOfCurrentMonth();

        LocalDate startOfLastMonth = startOfCurrentMonth.minusMonths(1);
        LocalDate endOfLastMonth = endOfCurrentMonth.minusMonths(1);

        Long employeesActiveThisMonth = employeeRepository.countEmployeesInRange(startOfCurrentMonth, endOfCurrentMonth);
        Long employeesActiveTheLastMonth = employeeRepository.countEmployeesInRange(startOfLastMonth, endOfLastMonth);

        return employeesActiveThisMonth - employeesActiveTheLastMonth;
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
}
