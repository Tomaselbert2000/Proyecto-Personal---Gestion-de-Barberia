package com.mapper.barberservice;

import com.dto.barbershopservice.BarberServiceUpdateDTO;
import com.mapper.implementation.BarberServiceMapperImpl;
import com.mapper.interfaces.BarberServiceMapper;
import com.model.BarberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.BarberServiceTestDataFactory.buildValidBarberService;
import static com.factory.BarberServiceTestDataFactory.buildValidBarberServiceUpdateDTO;
import static com.test_constant.BarberServiceTestConstants.CreationValidData.*;
import static com.test_constant.BarberServiceTestConstants.MapperTestData.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BarberServiceMapperUpdateTest {

    private final BarberServiceMapper mapper = new BarberServiceMapperImpl();
    private BarberService barberService;
    private BarberServiceUpdateDTO updateDTO;

    @BeforeEach
    void init() {

        barberService = buildValidBarberService();
        updateDTO = buildValidBarberServiceUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con cualquier atributo NULL, la entidad original mantiene el valor actual para cada caso")
    void givenAnyNullValue_WhenUpdating_ThenEntityKeepsCurrentData() {
        setAllFieldsToNull();

        mapEntityAndUpdate(barberService, updateDTO);
        assertAll(
                "Verificación de campos",
                () -> assertEquals(BARBER_SERVICE_ID, barberService.getBarbershopServiceID()),
                () -> assertEquals(BARBER_SERVICE_NAME, barberService.getName()),
                () -> assertEquals(BARBER_SERVICE_PRICE, barberService.getPrice()),
                () -> assertEquals(BARBER_SERVICE_CATEGORY, barberService.getServiceCategory()),
                () -> assertEquals(INTERNAL_NOTES, barberService.getInternalNotes())
        );
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombre en minúsculas, se modificará agregando la mayúscula correspondiente")
    void givenLowerCaseNewName_WhenUpdating_ThenIsCapitalized() {
        updateDTO.setName(LOWERCASE_NAME);
        mapEntityAndUpdate(barberService, updateDTO);

        assertEquals(BARBER_SERVICE_NAME, barberService.getName());
    }

    @Test
    @DisplayName("Dado un DTO de actualización cuyo nombre tenga espacios innecesarios, serán limpiados al mapearse")
    void givenNewNameWithSpaces_WhenUpdating_ThenIsTrimmed() {
        updateDTO.setName(BARBER_SERVICE_NAME_WITH_SPACES);
        mapEntityAndUpdate(barberService, updateDTO);

        assertEquals(BARBER_SERVICE_NAME, barberService.getName());
    }

    private void setAllFieldsToNull() {
        updateDTO.setName(null);
        updateDTO.setPrice(null);
        updateDTO.setServiceCategory(null);
        updateDTO.setInternalNotes(null);
    }

    private void mapEntityAndUpdate(BarberService entity, BarberServiceUpdateDTO dto) {
        mapper.mapBarberServiceUpdateDtoToEntity(entity, dto);
    }
}
