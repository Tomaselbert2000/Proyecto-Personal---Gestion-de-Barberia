package com.mapper.employee;

import com.dto.employee.EmployeeUpdateDTO;
import com.mapper.implementation.EmployeeMapperImpl;
import com.mapper.interfaces.EmployeeMapper;
import com.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.EmployeeTestDataFactory.buildValidEmployee;
import static com.factory.EmployeeTestDataFactory.buildValidEmployeeUpdateDTO;
import static com.test_constant.EmployeeTestConstants.CreationValidData.EMPLOYEE_FIRST_NAME;
import static com.test_constant.EmployeeTestConstants.CreationValidData.EMPLOYEE_LAST_NAME;
import static com.test_constant.EmployeeTestConstants.MapperData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeMapperUpdateTest {

    private final EmployeeMapper mapper = new EmployeeMapperImpl();

    private final EmployeeUpdateDTO updateDTO = buildValidEmployeeUpdateDTO();

    private final Employee employeeOnDB = buildValidEmployee();

    @Test
    @DisplayName("Dado un DTO de actualización con nombre completo que incluye espacios, cuando se realiza la actualización, entonces el espacio es eliminado")
    void givenFirstNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setFirstName(EMPLOYEE_FIRST_NAME_WITH_SPACES);

        Employee result = mapEntity();

        assertEquals(EMPLOYEE_FIRST_NAME, result.getFirstName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con apellido que incluye espacios, cuando se realiza la actualización, entonces el espacio es eliminado")
    void givenLastNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setLastName(EMPLOYEE_LAST_NAME_WITH_SPACES);

        Employee result = mapEntity();

        assertEquals(EMPLOYEE_LAST_NAME, result.getLastName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombre completo en minúsculas, cuando se realiza la actualización, entonces el nombre completo es convertido a mayúsculas")
    void givenLowercaseFirstName_WhenUpdating_ThenIsFullyCapitalized() {

        updateDTO.setFirstName(EMPLOYEE_LOWERCASE_FIRST_NAME);

        Employee result = mapEntity();

        assertEquals(EMPLOYEE_FIRST_NAME, result.getFirstName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con apellido en minúsculas, cuando se realiza la actualización, entonces el nombre completo es convertido a mayúsculas")
    void givenLowercaseLastName_WhenUpdating_ThenIsFullyCapitalized() {

        updateDTO.setLastName(EMPLOYEE_LOWERCASE_LAST_NAME);

        Employee result = mapEntity();

        assertEquals(EMPLOYEE_LAST_NAME, result.getLastName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombre completo nulo, cuando se realiza la actualización, entonces el nombre del empleado en la base de datos permanece igual")
    void givenNullFirstName_WhenUpdating_ThenEntityKeepsCurrentFirstName() {

        updateDTO.setFirstName(null);

        Employee employee = mapEntity();

        assertEquals(EMPLOYEE_FIRST_NAME, employee.getFirstName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con apellido nulo, cuando se realiza la actualización, entonces el apellido del empleado en la base de datos permanece igual")
    void givenNullLastName_WhenUpdating_ThenEntityKeepsCurrentLastName() {

        updateDTO.setLastName(null);

        Employee employee = mapEntity();

        assertEquals(EMPLOYEE_LAST_NAME, employee.getLastName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con valor nulo para el atributo 'isActive', cuando se realiza la actualización, entonces el valor del atributo 'isActive' en el empleado en la base de datos permanece igual")
    void givenNullIsActive_WhenUpdating_ThenEntityKeepsCurrentIsActiveValue() {

        updateDTO.setIsActive(null);

        Employee employee = mapEntity();

        assertEquals(EMPLOYEE_IS_ACTIVE_VALUE, employee.isActive());
    }

    private Employee mapEntity() {

        return mapper.mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO);
    }
}
