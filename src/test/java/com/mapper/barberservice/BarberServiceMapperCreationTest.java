package com.mapper.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.mapper.implementation.BarberServiceMapperImpl;
import com.barbershop.mapper.interfaces.BarberServiceMapper;
import com.barbershop.model.BarberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarberServiceMapperCreationTest {

    private final BarberServiceMapper mapper = new BarberServiceMapperImpl();
    private BarberServiceCreationDTO creationDTO;
    private BarberService barberService = null;

    private static final String NAME = "Nuevo servicio";
    private static final String NAME_WITH_SPACES = "        Este nombre tiene espacios      ";
    private static final String LOWERCASE_NAME = "este nombre esta en minusculas";
    private static final String CAPITALIZED_NAME  = "Este nombre esta en minusculas";
    private static final String TRIMMED_NAME  = "Este nombre tiene espacios";
    private static final Double PRICE = 1000.0;
    private static final BarberServiceCategory CATEGORY = BarberServiceCategory.COLOR;
    private static final String INTERNAL_NOTES = "Completar en máximo 60 minutos";
    private static final String INTERNAL_NOTES_WITH_SPACES = "      Completar en máximo 60 minutos      ";
    private static final String NULL_INTERNAL_NOTES = null;
    private static final String EMPTY_INTERNAL_NOTES = "";

    @Test
    @DisplayName("Dado un nombre que contenga espacios en blanco innecesarios, deberán ser limpiados al momento del mapeo")
    public void givenANameWithSpaces_WhenCreating_ThenIsTrimmed(){
        
        setupCreationDTO(NAME_WITH_SPACES, INTERNAL_NOTES);

        mapCreationDtoToEntity();

        assertEquals(TRIMMED_NAME, barberService.getName());
    }

    @Test
    @DisplayName("Dado un nombre en minusculas, deberá ser correctamente formateado para incluir mayúscula inicial")
    public void givenALowerCaseName_WhenCreating_ThenIsCapitalized(){

        setupCreationDTO(LOWERCASE_NAME, INTERNAL_NOTES);

        mapCreationDtoToEntity();

        assertEquals(CAPITALIZED_NAME, barberService.getName());
    }

    @Test
    @DisplayName("Dado un string de notas internas con espacios en blanco innecesarios, deberán ser limpiados al momento del mapeo")
    void givenInternalNotesStringWithSpaces_WhenCreating_ThenIsTrimmed(){

        setupCreationDTO(NAME, INTERNAL_NOTES_WITH_SPACES);

        mapCreationDtoToEntity();

        assertEquals(INTERNAL_NOTES, barberService.getInternalNotes());
    }

    @Test
    @DisplayName("Dado un atributo de notas internas NULL, se reemplazará con un string vacío al momento del mapeo")
    void givenNullInternalNotes_WhenCreating_ThenIsReplacedWithAnEmptyString(){

        setupCreationDTO(NAME, NULL_INTERNAL_NOTES);

        mapCreationDtoToEntity();

        assertEquals(EMPTY_INTERNAL_NOTES, barberService.getInternalNotes());
    }

    void mapCreationDtoToEntity() {

        barberService = mapper.mapBarberServiceCreationDtoToEntity(creationDTO);
    }

    void setupCreationDTO(String nameString, String internalNotes) {

        creationDTO = BarberServiceCreationDTO.builder()
                .name(nameString)
                .price(BarberServiceMapperCreationTest.PRICE)
                .serviceCategory(BarberServiceMapperCreationTest.CATEGORY)
                .internalNotes(internalNotes)
                .build();
    }
}
