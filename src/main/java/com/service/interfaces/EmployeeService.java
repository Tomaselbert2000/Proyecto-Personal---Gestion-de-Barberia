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

    EmployeeInfoDTO getEmployeeInfo(Long employeeID);

    List<EmployeeInfoDTO> getEmployeeList();

    void updateEmployee(Long employeeID, EmployeeUpdateDTO updateDTO);

    Long getActiveEmployees();

    Long getInactiveEmployees();

    Long calculateActiveEmployeesVsLastMonth();

    Long getEmployeeCount();

    Long getEmployeesTrendThisMonth();

    List<EmployeeInfoDTO> liveSearch(String employeeName, EmployeeStatus selectedStatus, HireDateRange dateRange);

    double calculateAverageMonthlyProductivity();

    double calculateProductivityTrendVsLastMonth();

    void changeEmployeeIsActiveValue(Long id);
}
