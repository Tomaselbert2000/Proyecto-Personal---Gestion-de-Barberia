package com.mapper.employee;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.mapper.implementation.EmployeeMapperImpl;
import com.barbershop.mapper.interfaces.EmployeeMapper;
import com.barbershop.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeMapperCreationTest {

    private final EmployeeMapper mapper = new EmployeeMapperImpl();

    private EmployeeCreationDTO creationDTO;

    @BeforeEach
    void init(){

        creationDTO = new EmployeeCreationDTO();
    }

    @Test
    void givenFirstNameWithSpaces_WhenCreating_ThenIsTrimmed(){

        creationDTO.setFirstName("   Tomas Gabriel   ");

        Employee result = mapper.mapEmployeeCreationDtoToEntity(creationDTO);

        assertEquals("Tomas Gabriel", result.getFirstName());
    }

    @Test
    void givenLastNameWithSpaces_WhenCreating_ThenIsTrimmed(){

        creationDTO.setLastName("   Elbert   ");

        Employee result = mapper.mapEmployeeCreationDtoToEntity(creationDTO);

        assertEquals("Elbert", result.getLastName());
    }

    @Test
    void givenLowercaseFirstName_WhenCreating_ThenIsFullyCapitalized(){

        creationDTO.setFirstName("tomas gabriel");

        Employee result = mapper.mapEmployeeCreationDtoToEntity(creationDTO);

        assertEquals("Tomas Gabriel", result.getFirstName());
    }

    @Test
    void givenLowercaseLastName_WhenCreating_ThenIsFullyCapitalized(){

        creationDTO.setLastName("elbert");

        Employee result = mapper.mapEmployeeCreationDtoToEntity(creationDTO);

        assertEquals("Elbert", result.getLastName());
    }
}
