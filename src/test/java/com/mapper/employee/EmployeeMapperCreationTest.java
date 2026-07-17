package com.mapper.employee;

import com.dto.employee.EmployeeCreationDTO;
import com.mapper.implementation.EmployeeMapperImpl;
import com.mapper.interfaces.EmployeeMapper;
import com.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.EmployeeTestDataFactory.buildValidEmployeeCreationDTO;
import static com.test_constant.EmployeeTestConstants.CreationValidData.EMPLOYEE_FIRST_NAME;
import static com.test_constant.EmployeeTestConstants.CreationValidData.EMPLOYEE_LAST_NAME;
import static com.test_constant.EmployeeTestConstants.MapperData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeMapperCreationTest {

    private final EmployeeMapper mapper = new EmployeeMapperImpl();

    private final EmployeeCreationDTO creationDTO = buildValidEmployeeCreationDTO();

    @Test
    @DisplayName("Verifica que el nombre con espacios se trimea correctamente al crear un empleado")
    void givenFirstNameWithSpaces_WhenCreating_ThenIsTrimmed() {
        creationDTO.setFirstName(EMPLOYEE_FIRST_NAME_WITH_SPACES);

        Employee result = mapEntity();
        assertEquals(EMPLOYEE_FIRST_NAME, result.getFirstName());
    }

    @Test
    @DisplayName("Verifica que el apellido con espacios se trimea correctamente al crear un empleado")
    void givenLastNameWithSpaces_WhenCreating_ThenIsTrimmed() {

        creationDTO.setLastName(EMPLOYEE_LAST_NAME_WITH_SPACES);

        Employee result = mapEntity();
        assertEquals(EMPLOYEE_LAST_NAME, result.getLastName());
    }

    @Test
    @DisplayName("Verifica que el nombre en minúsculas se convierte completamente a mayúsculas al crear un empleado")
    void givenLowercaseFirstName_WhenCreating_ThenIsFullyCapitalized() {

        creationDTO.setFirstName(EMPLOYEE_LOWERCASE_FIRST_NAME);

        Employee result = mapEntity();
        assertEquals(EMPLOYEE_FIRST_NAME, result.getFirstName());
    }

    @Test
    @DisplayName("Verifica que el apellido en minúsculas se convierte completamente a mayúsculas al crear un empleado")
    void givenLowercaseLastName_WhenCreating_ThenIsFullyCapitalized() {

        creationDTO.setLastName(EMPLOYEE_LOWERCASE_LAST_NAME);

        Employee result = mapEntity();
        assertEquals(EMPLOYEE_LAST_NAME, result.getLastName());
    }

    private Employee mapEntity() {

        return mapper.mapEmployeeCreationDtoToEntity(creationDTO);
    }
}
