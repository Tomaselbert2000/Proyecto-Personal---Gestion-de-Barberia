package com.barbershop.mapper.implementation;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.exceptions.common.NullMapperInputException;
import com.barbershop.mapper.interfaces.EmployeeMapper;
import com.barbershop.model.Employee;
import com.barbershop.utils.strings.StringCleaner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    private static final Boolean DEFAULT_IS_ACTIVE_VALUE = true;
    private static final String DEFAULT_TERMINATION_DATE_STRING = "Empleado activo actualmente.";

    @Override
    public Employee mapEmployeeCreationDtoToEntity(EmployeeCreationDTO creationDTO) {

        if (creationDTO == null) throw new NullMapperInputException();

        return Employee.builder()
                .firstName(StringCleaner.formatAsProperName(creationDTO.getFirstName()))
                .lastName(StringCleaner.formatAsProperName(creationDTO.getLastName()))
                .hireDate(creationDTO.getHireDate())
                .isActive(DEFAULT_IS_ACTIVE_VALUE)
                .build();
    }

    @Override
    public Employee mapEmployeeUpdateDtoToEntity(Employee entity, EmployeeUpdateDTO updateDTO) {

        if (entity == null || updateDTO == null) throw new NullMapperInputException();

        setUpdatedDataOnEntity(entity, updateDTO);

        return entity;
    }

    @Override
    public EmployeeInfoDTO mapEmployeeToInfoDTO(Employee employee) {

        if (employee == null) throw new NullMapperInputException();

        String terminationDateAsString;

        if (employee.getTerminationDate() == null) {

            terminationDateAsString = DEFAULT_TERMINATION_DATE_STRING;

        } else {

            terminationDateAsString = employee.getTerminationDate().toString();

        }

        return EmployeeInfoDTO.builder()
                .id(employee.getEmployeeID())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .hireDateAsString(String.valueOf(employee.getHireDate()))
                .terminationDateAsString(terminationDateAsString)
                .build();
    }

    @Override
    public List<EmployeeInfoDTO> mapEmployeeToInfoDTO(List<Employee> employeeList) {

        if (employeeList == null) throw new NullMapperInputException();

        return employeeList.stream().map(this::mapEmployeeToInfoDTO).collect(Collectors.toList());
    }

    private void setUpdatedDataOnEntity(Employee entity, EmployeeUpdateDTO updateDTO) {

        if (updateDTO.getFirstName() != null)
            entity.setFirstName(StringCleaner.formatAsProperName(updateDTO.getFirstName()));

        if (updateDTO.getLastName() != null)
            entity.setLastName(StringCleaner.formatAsProperName(updateDTO.getLastName()));

        if (updateDTO.getIsActive() != null) entity.setActive(updateDTO.getIsActive());

        if (updateDTO.getTerminationDate() != null) entity.setTerminationDate(updateDTO.getTerminationDate());
    }
}
