package com.barbershop.service.interfaces;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.dto.employee.EmployeeUpdateDTO;

import java.util.List;

public interface EmployeeService {

    void registerNewEmployee(EmployeeCreationDTO dto);

    void deleteEmployee(Long employeeID);

    EmployeeInfoDTO getEmployeeInfo(Long employeeID);

    List<EmployeeInfoDTO> getEmployeeList();

    void updateEmployee(Long employeeID, EmployeeUpdateDTO updateDTO);

    Long getActiveEmployees();

    Long calculateActiveEmployeesVsLastMonth();
}
