package com.barbershop.mapper.interfaces;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.model.Employee;

import java.util.List;

public interface EmployeeMapper {

    Employee mapEmployeeCreationDtoToEntity(EmployeeCreationDTO dto);

    Employee mapEmployeeUpdateDtoToEntity(Employee entity, EmployeeUpdateDTO updateDTO);

    EmployeeInfoDTO mapEmployeeToInfoDTO(Employee entity);

    List<EmployeeInfoDTO> mapEmployeeToInfoDTO(List<Employee> employeeList);
}
