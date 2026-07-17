package com.service.helper;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.mapper.interfaces.EmployeeMapper;
import com.model.Employee;
import com.repository.EmployeeRepository;
import com.service.interfaces.EmployeeService;
import com.validation.employee.EmployeeValidator;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class EmployeeServiceTestHelper {

    public static void registerNewEmployee(EmployeeService employeeService, EmployeeCreationDTO creationDTO) {

        employeeService.registerNewEmployee(creationDTO);
    }

    public static void deleteEmployee(EmployeeService employeeService, Employee employee) {

        employeeService.deleteEmployee(employee.getEmployeeID());
    }

    public static void updateEmployee(EmployeeService employeeService, Employee employee, EmployeeUpdateDTO updateDTO) {

        employeeService.updateEmployee(employee.getEmployeeID(), updateDTO);
    }

    public static void verifyValidatorCreationInteraction(EmployeeValidator validator, EmployeeCreationDTO creationDTO) {

        verify(validator).validateDTO(creationDTO);
    }

    public static void verifyValidatorUpdateInteraction(EmployeeValidator validator, EmployeeUpdateDTO updateDTO) {
        verify(validator).validateDTO(updateDTO);
    }

    public static void verifyMapperCreationInteraction(EmployeeMapper mapper, EmployeeCreationDTO creationDTO) {

        verify(mapper).mapEmployeeCreationDtoToEntity(creationDTO);
    }

    public static void verifyMapperUpdateInteraction(EmployeeMapper mapper, Employee employeeOnDB, EmployeeUpdateDTO updateDTO) {

        verify(mapper).mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO);
    }

    public static void verifyMapperCreationNoInteractions(EmployeeMapper mapper, EmployeeCreationDTO creationDTO) {

        verify(mapper, never()).mapEmployeeCreationDtoToEntity(creationDTO);
    }

    public static void verifyMapperUpdateNoInteractions(EmployeeMapper mapper, Employee employeeOnDB, EmployeeUpdateDTO updateDTO) {

        verify(mapper, never()).mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO);
    }

    public static void mockEmployee(EmployeeRepository employeeRepository, Employee employee) {

        when(employeeRepository.findById(employee.getEmployeeID())).thenReturn(Optional.of(employee));
    }

    public static void mockNonExistingEmployee(EmployeeRepository employeeRepository, Employee employee) {

        when(employeeRepository.findById(employee.getEmployeeID())).thenReturn(Optional.empty());
    }

    public static void mockEmployeeList(EmployeeRepository employeeRepository, List<Employee> employeeList) {

        when(employeeRepository.findAll()).thenReturn(employeeList);
    }

    public static <T> void mockValidatorToThrowException(EmployeeValidator validator, Exception exception, T dto) {

        doThrow(exception).when(validator).validateDTO(dto);
    }
}
