package com.mapper.client;

import com.dto.client.ClientCreationDTO;
import com.mapper.implementation.ClientMapperImpl;
import com.mapper.interfaces.ClientMapper;
import com.model.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.factory.ClientTestDataFactory.buildValidClientCreationDTO;
import static com.test_constant.ClientTestConstants.CreationValidData.*;
import static com.test_constant.ClientTestConstants.MapperData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientMapperCreationTest {

    private final ClientMapper mapper = new ClientMapperImpl();

    private final ClientCreationDTO createDTO = buildValidClientCreationDTO();

    private final LocalDate today = LocalDate.now();

    private Client clientMapped;

    @Test
    @DisplayName("Dado un DTO de creación cuyo valor de DNI tenga espacios, deberán ser limpiados al mapear la entidad")
    void givenNationalIdentityCardNumberWithSpaces_WhenCreating_ThenIsTrimmed() {

        createDTO.setNationalIdentityCardNumber(NATIONAL_ID_CARD_NUMBER_WITH_SPACES);

        clientMapped = mapEntity();

        assertEquals(NATIONAL_ID_CARD_NUMBER, clientMapped.getNationalIdentityCardNumber());
    }

    @Test
    @DisplayName("Dado un nombre con espacios, al mapear la entidad serán eliminados")
    void givenFirstNameWithSpaces_WhenCreating_ThenIsTrimmed() {

        createDTO.setFirstName(FIRST_NAME_WITH_SPACES);

        clientMapped = mapEntity();

        assertEquals(FIRST_NAME, clientMapped.getFirstName());
    }

    @Test
    @DisplayName("Dado un apellido con espacios, al crearlo, debe estar trimado")
    void givenLastNameWith_Spaces_WhenCreating_ThenIsTrimmed() {

        createDTO.setLastName(LAST_NAME_WITH_SPACES);

        clientMapped = mapEntity();
        assertEquals(LAST_NAME, clientMapped.getLastName());
    }

    @Test
    @DisplayName("Dado un nombre en minúsculas, al crearlo, debe estar completamente capitalizado")
    void givenLowercaseFirstName_WhenCreating_ThenIsFullyCapitalized() {

        createDTO.setFirstName(LOWERCASE_FIRST_NAME);

        clientMapped = mapEntity();
        assertEquals(FIRST_NAME, clientMapped.getFirstName());
    }

    @Test
    @DisplayName("Dado un apellido en minúsculas, al crearlo, debe estar completamente capitalizado")
    void givenLowercaseLastName_WhenCreating_ThenIsFullyCapitalized() {

        createDTO.setLastName(LOWERCASE_LAST_NAME);

        clientMapped = mapEntity();
        assertEquals(LAST_NAME, clientMapped.getLastName());
    }

    @Test
    @DisplayName("Dado un correo electrónico con espacios, al mapear la entidad serán eliminados")
    void givenEmailWithSpaces_WhenCreating_ThenIsTrimmed() {

        createDTO.setEmail(EMAIL_WITH_SPACES);

        clientMapped = mapEntity();
        assertEquals(EMAIL, clientMapped.getEmail());
    }

    @Test
    @DisplayName("Dado un número de teléfono con espacios, al mapear la entidad serán eliminados")
    void givenPhoneNumberWithSpaces_WhenCreating_ThenIsTrimmed() {

        createDTO.setPhoneNumbersList(PHONE_LIST_THAT_CONTAINS_PHONE_WITH_SPACES);

        clientMapped = mapEntity();
        assertEquals(PHONE_LIST.getFirst(), clientMapped.getPhoneNumbersList().getFirst());
    }

    @Test
    @DisplayName("Dado un string de notas opcionales con espacios innecesarios, al mapear la entidad serán eliminados")
    void givenOptionalNotesWithUnnecessarySpaces_WhenCreating_ThenIsTrimmed() {

        createDTO.setOptionalNotes(OPTIONAL_NOTES_WITH_SPACES);

        clientMapped = mapEntity();

        assertEquals(OPTIONAL_NOTES, clientMapped.getOptionalNotes());
    }

    private Client mapEntity() {

        return mapper.mapClientCreationDTOtoEntity(createDTO, today);
    }
}