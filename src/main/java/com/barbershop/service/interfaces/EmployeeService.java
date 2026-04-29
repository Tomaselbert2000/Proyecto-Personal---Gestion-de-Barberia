package com.barbershop.service.interfaces;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.enums.EmployeeStatus;
import com.barbershop.enums.HireDateRange;

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
