package com.mapper.implementation;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.mapper.interfaces.EmployeeMapper;
import com.model.Employee;
import com.utils.strings.StringCleaner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;
import static com.presentation.constants.StringResource.DisplayString.DEFAULT_TERMINATION_DATE_STRING;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    private static final Boolean DEFAULT_IS_ACTIVE_VALUE = true;

    @Override
    public Employee mapEmployeeCreationDtoToEntity(EmployeeCreationDTO dto) {

        checkIfMapperInputIsNull(dto);

        return Employee.builder()
                .firstName(StringCleaner.formatAsProperName(dto.getFirstName()))
                .lastName(StringCleaner.formatAsProperName(dto.getLastName()))
                .hireDate(dto.getHireDate())
                .isActive(DEFAULT_IS_ACTIVE_VALUE)
                .commissionPercentage(dto.getCommissionPercentage())
                .build();
    }

    @Override
    public Employee mapEmployeeUpdateDtoToEntity(Employee entity, EmployeeUpdateDTO updateDTO) {

        checkIfMapperInputIsNull(entity, updateDTO);

        setUpdatedDataOnEntity(entity, updateDTO);

        return entity;
    }

    @Override
    public EmployeeInfoDTO mapEmployeeToInfoDTO(Employee employee) {

        checkIfMapperInputIsNull(employee);

        String terminationDateAsString = DEFAULT_TERMINATION_DATE_STRING;

        if (employee.getTerminationDate() != null) terminationDateAsString = employee.getTerminationDate().toString();

        return EmployeeInfoDTO.builder()
                .id(employee.getEmployeeID())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .hireDateAsString(String.valueOf(employee.getHireDate()))
                .terminationDateAsString(terminationDateAsString)
                .commissionPercentage(employee.getCommissionPercentage())
                .isActive(employee.isActive())
                .build();
    }

    @Override
    public List<EmployeeInfoDTO> mapEmployeeToInfoDTO(List<Employee> employeeList) {

        checkIfMapperInputIsNull(employeeList);

        return employeeList.stream().map(this::mapEmployeeToInfoDTO).collect(Collectors.toList());
    }

    private void setUpdatedDataOnEntity(Employee entity, EmployeeUpdateDTO updateDTO) {

        if (updateDTO.getFirstName() != null)
            entity.setFirstName(StringCleaner.formatAsProperName(updateDTO.getFirstName()));

        if (updateDTO.getLastName() != null)
            entity.setLastName(StringCleaner.formatAsProperName(updateDTO.getLastName()));

        if (updateDTO.getIsActive() != null) entity.setActive(updateDTO.getIsActive());

        if (updateDTO.getTerminationDate() != null) entity.setTerminationDate(updateDTO.getTerminationDate());

        if (updateDTO.getCommissionPercentage() != null)
            entity.setCommissionPercentage(updateDTO.getCommissionPercentage());
    }
}
