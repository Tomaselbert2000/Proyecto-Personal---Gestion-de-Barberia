package com.mapper.barberservice;

import com.dto.barbershopservice.BarberServiceCreationDTO;
import com.mapper.implementation.BarberServiceMapperImpl;
import com.mapper.interfaces.BarberServiceMapper;
import com.model.BarberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.BarberServiceTestDataFactory.buildValidBarberServiceCreationDTO;
import static com.test_constant.BarberServiceTestConstants.CreationValidData.BARBER_SERVICE_NAME;
import static com.test_constant.BarberServiceTestConstants.CreationValidData.INTERNAL_NOTES;
import static com.test_constant.BarberServiceTestConstants.MapperTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarberServiceMapperCreationTest {

    private final BarberServiceMapper mapper = new BarberServiceMapperImpl();
    private final BarberServiceCreationDTO creationDTO = buildValidBarberServiceCreationDTO();

    @Test
    @DisplayName("Dado un nombre que contenga espacios en blanco innecesarios, deberán ser limpiados al momento del mapeo")
    public void givenANameWithSpaces_WhenCreating_ThenIsTrimmed() {

        creationDTO.setName(BARBER_SERVICE_NAME_WITH_SPACES);

        BarberService barberService = mapCreationDtoToEntity();

        assertEquals(BARBER_SERVICE_NAME, barberService.getName());
    }

    @Test
    @DisplayName("Dado un nombre en minusculas, deberá ser correctamente formateado para incluir mayúscula inicial")
    public void givenALowerCaseName_WhenCreating_ThenIsCapitalized() {

        creationDTO.setName(LOWERCASE_NAME);

        BarberService barberService = mapCreationDtoToEntity();

        assertEquals(BARBER_SERVICE_NAME, barberService.getName());
    }

    @Test
    @DisplayName("Dado un string de notas internas con espacios en blanco innecesarios, deberán ser limpiados al momento del mapeo")
    void givenInternalNotesStringWithSpaces_WhenCreating_ThenIsTrimmed() {

        creationDTO.setInternalNotes(INTERNAL_NOTES_WITH_SPACES);

        BarberService barberService = mapCreationDtoToEntity();

        assertEquals(INTERNAL_NOTES, barberService.getInternalNotes());
    }

    @Test
    @DisplayName("Dado un atributo de notas internas NULL, se reemplazará con un string vacío al momento del mapeo")
    void givenNullInternalNotes_WhenCreating_ThenIsReplacedWithAnEmptyString() {

        creationDTO.setInternalNotes(null);

        BarberService barberService = mapCreationDtoToEntity();

        assertEquals(EMPTY_INTERNAL_NOTES, barberService.getInternalNotes());
    }

    BarberService mapCreationDtoToEntity() {

        return mapper.mapBarberServiceCreationDtoToEntity(creationDTO);
    }
}
