package com.service.interfaces;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;

import java.util.List;

public interface EmployeeService {

    void registerNewEmployee(EmployeeCreationDTO dto);

    void deleteEmployee(Long employeeID);

    List<EmployeeInfoDTO> getEmployeeList();

    EmployeeInfoDTO getEmployeeInfo(Long employeeID);

    void updateEmployee(Long employeeID, EmployeeUpdateDTO updateDTO);

    Long getActiveEmployees();

    Long getEmployeeCount();

    List<EmployeeInfoDTO> liveSearch(String employeeName, EmployeeStatus selectedStatus, HireDateRange dateRange);

    void changeEmployeeIsActiveValue(Long id);

    List<String> getNames();
}
