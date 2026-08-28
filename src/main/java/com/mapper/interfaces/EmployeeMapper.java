package com.mapper.interfaces;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.model.Employee;

import java.util.List;

public interface EmployeeMapper {

    Employee mapEmployeeCreationDtoToEntity(EmployeeCreationDTO dto);

    Employee mapEmployeeUpdateDtoToEntity(Employee entity, EmployeeUpdateDTO dto);

    EmployeeInfoDTO mapEmployeeToInfoDTO(Employee entity);

    List<EmployeeInfoDTO> mapEmployeeToInfoDTO(List<Employee> entityList);
}
