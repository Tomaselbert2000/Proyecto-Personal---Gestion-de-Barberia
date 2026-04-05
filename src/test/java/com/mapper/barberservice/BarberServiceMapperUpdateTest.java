package com.mapper.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceUpdateDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.mapper.implementation.BarberServiceMapperImpl;
import com.barbershop.mapper.interfaces.BarberServiceMapper;
import com.barbershop.model.BarberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarberServiceMapperUpdateTest {

    private final BarberServiceMapper mapper = new BarberServiceMapperImpl();

    private BarberService barberService = null;

    private BarberServiceUpdateDTO updateDTO;

    private static final Long ID = 1L;
    private static final String NAME = "Corte de pelo básico";
    private static final Double PRICE = 12000.0;
    private static final BarberServiceCategory CATEGORY = BarberServiceCategory.CORTE;
    private static final LocalDateTime REGISTRATION_TIMESTAMP = LocalDateTime.of(2026, 1, 1, 12, 30);
    private static final LocalDateTime MODIFICATION_TIMESTAMP = null;
    private static final String INTERNAL_NOTES = "Completar en máximo 30 minutos";

    private static final String NEW_NAME = "Corte de pelo";
    private static final String NEW_NAME_WITH_SPACES = "        Corte de pelo       ";
    private static final String LOWERCASE_NEW_NAME = "corte de pelo";
    private static final Double NEW_PRICE = 16000.0;
    private static final String NEW_INTERNAL_NOTES = "Completar en máximo 45 minutos";
    private static final String NEW_INTERNAL_NOTES_WITH_SPACES = "      Completar en máximo 45 minutos      ";
    private static final String EMPTY_INTERNAL_NOTES = "";

    @BeforeEach
    void init() {

        setupExistingEntity();
        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado cualquiera de los atributos del DTO de actualización que sean NULL, la entidad mantendrá sus valores actuales")
    void givenAnyNullValue_WhenUpdating_ThenEntityKeepsCurrentData(){

        setAllFieldsOfUpdateDtoOnNull();

        mapUpdateDtoToEntity();

        assertAll(
                "Verificación de campos",
                () -> assertEquals(ID, barberService.getBarbershopServiceID()),
                () -> assertEquals(NAME, barberService.getName()),
                () -> assertEquals(PRICE, barberService.getPrice()),
                () -> assertEquals(CATEGORY, barberService.getServiceCategory()),
                () -> assertEquals(INTERNAL_NOTES, barberService.getInternalNotes())
        );
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre con espacios en blanco innecesarios, serán limpiados al momento del mapeo")
    void givenNewNameWithSpaces_WhenUpdating_ThenIsTrimmed(){

        updateDTO.setName(NEW_NAME_WITH_SPACES);

        mapUpdateDtoToEntity();

        assertEquals(NEW_NAME, barberService.getName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en minúsculas, deberá ser correctamente formateado para incluir la mayúscula inicial")
    void givenLowerCaseNewName_WhenUpdating_ThenIsCapitalized(){

        updateDTO.setName(LOWERCASE_NEW_NAME);

        mapUpdateDtoToEntity();

        assertEquals(NEW_NAME, barberService.getName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un string de notas internas con espacios en blanco innecesarios, deberán ser limpiados al momemto del mapeo")
    void givenInternalNotesStringWithSpaces_WhenUpdating_ThenIsTrimmed(){

        updateDTO.setInternalNotes(NEW_INTERNAL_NOTES_WITH_SPACES);

        mapUpdateDtoToEntity();

        assertEquals(NEW_INTERNAL_NOTES, barberService.getInternalNotes());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un string de notas internas en blanco, se mapeará exitosamente como borrado de notas")
    void givenBlankInternalNotes_WhenUpdating_ThenIsSuccesfullyMapped(){

        updateDTO.setInternalNotes(EMPTY_INTERNAL_NOTES);

        mapUpdateDtoToEntity();

        assertEquals("", barberService.getInternalNotes());
    }

    private void setupExistingEntity() {

        barberService = BarberService.builder()
                .barbershopServiceID(ID)
                .name(NAME)
                .price(PRICE)
                .serviceCategory(CATEGORY)
                .registrationTimestamp(REGISTRATION_TIMESTAMP)
                .modifiedDate(MODIFICATION_TIMESTAMP)
                .internalNotes(INTERNAL_NOTES)
                .build();
    }

    private void setupUpdateDTO() {

        updateDTO = BarberServiceUpdateDTO.builder()
                .name(NEW_NAME)
                .price(NEW_PRICE)
                .serviceCategory(CATEGORY)
                .internalNotes(NEW_INTERNAL_NOTES)
                .build();
    }

    private void mapUpdateDtoToEntity() {

        mapper.mapBarberServiceUpdateDtoToEntity(barberService, updateDTO);
    }

    private void setAllFieldsOfUpdateDtoOnNull() {

        updateDTO.setName(null);
        updateDTO.setPrice(null);
        updateDTO.setServiceCategory(null);
        updateDTO.setInternalNotes(null);
    }
}
