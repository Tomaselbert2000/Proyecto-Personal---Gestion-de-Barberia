package com.mapper.employee;

import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.mapper.implementation.EmployeeMapperImpl;
import com.barbershop.mapper.interfaces.EmployeeMapper;
import com.barbershop.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeMapperUpdateTest {

    private final EmployeeMapper mapper = new EmployeeMapperImpl();

    private EmployeeUpdateDTO updateDTO;

    private Employee employeeOnDB;

    @BeforeEach
    void init() {

        updateDTO = new EmployeeUpdateDTO();
        employeeOnDB = Employee.builder().employeeID(1L).firstName("Tomas Gabriel").lastName("Elbert").hireDate(LocalDate.of(2026, 1, 1)).isActive(true).build();
    }

    @Test
    void givenFirstNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setFirstName("   Tomas Gabriel   ");

        Employee result = mapper.mapEmployeeUpdateDtoToEntity(new Employee(), updateDTO);

        assertEquals("Tomas Gabriel", result.getFirstName());
    }

    @Test
    void givenLastNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setFirstName("   Tomas Gabriel   ");

        Employee result = mapper.mapEmployeeUpdateDtoToEntity(new Employee(), updateDTO);

        assertEquals("Tomas Gabriel", result.getFirstName());
    }

    @Test
    void givenLowercaseFirstName_WhenUpdating_ThenIsFullyCapitalized() {

        updateDTO.setFirstName("tomas gabriel");

        Employee result = mapper.mapEmployeeUpdateDtoToEntity(new Employee(), updateDTO);

        assertEquals("Tomas Gabriel", result.getFirstName());
    }

    @Test
    void givenLowercaseLastName_WhenUpdating_ThenIsFullyCapitalized() {

        updateDTO.setLastName("elbert");

        Employee result = mapper.mapEmployeeUpdateDtoToEntity(new Employee(), updateDTO);

        assertEquals("Elbert", result.getLastName());
    }

    @Test
    void givenNullFirstName_WhenUpdating_ThenEntityKeepsCurrentFirstName() {

        updateDTO.setFirstName(null);

        String expectedFirstName = "Tomas Gabriel";
        String returnedFirstName = mapper.mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO).getFirstName();

        assertEquals(expectedFirstName, returnedFirstName);
    }

    @Test
    void givenNullLastName_WhenUpdating_ThenEntityKeepsCurrentLastName() {

        updateDTO.setLastName(null);

        String expectedLastName = "Elbert";
        String returnedLastName = mapper.mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO).getLastName();

        assertEquals(expectedLastName, returnedLastName);
    }

    @Test
    void givenNullIsActive_WhenUpdating_ThenEntityKeepsCurrentIsActiveValue() {

        updateDTO.setIsActive(null);

        Boolean expectedValue = true;
        Boolean returnedValue = mapper.mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO).isActive();

        assertEquals(expectedValue, returnedValue);
    }
}
